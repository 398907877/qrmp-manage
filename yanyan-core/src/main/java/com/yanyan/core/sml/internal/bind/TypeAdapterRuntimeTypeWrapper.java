package com.yanyan.core.sml.internal.bind;

import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import com.google.gson.reflect.TypeToken;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 11:02
 */
final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
    private final Sml context;
    private final TypeAdapter<T> delegate;
    private final Type type;

    TypeAdapterRuntimeTypeWrapper(Sml context, TypeAdapter<T> delegate, Type type) {
        this.context = context;
        this.delegate = delegate;
        this.type = type;
    }

    /**
     * Finds a compatible runtime type if it is more specific
     */
    private Type getRuntimeTypeIfMoreSpecific(Type type, Object value) {
        if (value != null
                && (type == Object.class || type instanceof TypeVariable<?> || type instanceof Class<?>)) {
            type = value.getClass();
        }
        return type;
    }

    @Override
    public void toXml(T src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        // Order of preference for choosing type adapters
        // First preference: a type adapter registered for the runtime type
        // Second preference: a type adapter registered for the declared type
        // Third preference: reflective type adapter for the runtime type (if it is a sub class of the declared type)
        // Fourth preference: reflective type adapter for the declared type

        TypeAdapter chosen = delegate;
        Type runtimeType = getRuntimeTypeIfMoreSpecific(type, src);
        if (runtimeType != type) {
            TypeAdapter runtimeTypeAdapter = this.context.getAdapter(TypeToken.get(runtimeType));
            if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                // The user registered a type adapter for the runtime type, so we will use that
                chosen = runtimeTypeAdapter;
            } else if (!(delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                // The user registered a type adapter for Base class, so we prefer it over the
                // reflective type adapter for the runtime type
                chosen = delegate;
            } else {
                // Use the type adapter for runtime type
                chosen = runtimeTypeAdapter;
            }
        }
        chosen.toXml(src, typeOfSrc, name, parent, context);
    }

    @Override
    public T fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
        return delegate.fromXml(xml, typeOfT, context);
    }
}