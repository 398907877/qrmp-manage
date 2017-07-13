package com.yanyan.core.sml.internal.bind;

import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.TypeAdapterFactory;
import com.yanyan.core.sml.annontions.XmlAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 11:14
 */
public class XmlAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {

    private final ConstructorConstructor constructorConstructor;

    public XmlAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> targetType) {
        XmlAdapter annotation = targetType.getRawType().getAnnotation(XmlAdapter.class);
        if (annotation == null) {
            return null;
        }
        return (TypeAdapter<T>) getTypeAdapter(constructorConstructor, sml, targetType, annotation);
    }

    @SuppressWarnings("unchecked") // Casts guarded by conditionals.
    static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Sml sml,
                                                         TypeToken<?> fieldType, XmlAdapter annotation) {
        Class<?> value = annotation.value();
        if (TypeAdapter.class.isAssignableFrom(value)) {
            Class<TypeAdapter<?>> typeAdapter = (Class<TypeAdapter<?>>) value;
            return constructorConstructor.get(TypeToken.get(typeAdapter)).construct();
        }
        if (TypeAdapterFactory.class.isAssignableFrom(value)) {
            Class<TypeAdapterFactory> typeAdapterFactory = (Class<TypeAdapterFactory>) value;
            return constructorConstructor.get(TypeToken.get(typeAdapterFactory))
                    .construct()
                    .create(sml, fieldType);
        }

        throw new IllegalArgumentException(
                "@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
    }
}
