package com.yanyan.core.sml;

import com.yanyan.core.sml.internal.bind.TypeAdapter;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.reflect.TypeToken;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 11:30
 */
final class TreeTypeAdapter<T> extends TypeAdapter<T> {
    private final XmlSerializer<T> serializer;
    private final XmlDeserializer<T> deserializer;
    private final Sml sml;
    private final TypeToken<T> typeToken;
    private final TypeAdapterFactory skipPast;

    /** The delegate is lazily created because it may not be needed, and creating it may fail. */
    private TypeAdapter<T> delegate;

    private TreeTypeAdapter(XmlSerializer<T> serializer, XmlDeserializer<T> deserializer,
                            Sml sml, TypeToken<T> typeToken, TypeAdapterFactory skipPast) {
        this.serializer = serializer;
        this.deserializer = deserializer;
        this.sml = sml;
        this.typeToken = typeToken;
        this.skipPast = skipPast;
    }

    @Override
    public void toXml(T src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        if (serializer == null) {
            delegate().toXml(src, typeOfSrc, name, parent, context);
            return;
        }
        if (src == null) {
            return;
        }
        serializer.serialize(src, typeToken.getType(), name, parent, context);
    }

    @Override
    public T fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
        if (deserializer == null) {
            return delegate().fromXml(xml, typeOfT, context);
        }

        if(xml==null){
            return null;
        }

        return deserializer.deserialize(xml, typeOfT, context);
    }


    private TypeAdapter<T> delegate() {
        TypeAdapter<T> d = delegate;
        return d != null
                ? d
                : (delegate = sml.getDelegateAdapter(skipPast, typeToken));
    }

    /**
     * Returns a new factory that will match each type against {@code exactType}.
     */
    public static TypeAdapterFactory newFactory(TypeToken<?> exactType, Object typeAdapter) {
        return new SingleTypeFactory(typeAdapter, exactType, false, null);
    }

    /**
     * Returns a new factory that will match each type and its raw type against
     * {@code exactType}.
     */
    public static TypeAdapterFactory newFactoryWithMatchRawType(
            TypeToken<?> exactType, Object typeAdapter) {
        // only bother matching raw types if exact type is a raw type
        boolean matchRawType = exactType.getType() == exactType.getRawType();
        return new SingleTypeFactory(typeAdapter, exactType, matchRawType, null);
    }

    /**
     * Returns a new factory that will match each type's raw type for assignability
     * to {@code hierarchyType}.
     */
    public static TypeAdapterFactory newTypeHierarchyFactory(
            Class<?> hierarchyType, Object typeAdapter) {
        return new SingleTypeFactory(typeAdapter, null, false, hierarchyType);
    }

    private static class SingleTypeFactory implements TypeAdapterFactory {
        private final TypeToken<?> exactType;
        private final boolean matchRawType;
        private final Class<?> hierarchyType;
        private final XmlSerializer<?> serializer;
        private final XmlDeserializer<?> deserializer;

        private SingleTypeFactory(Object typeAdapter, TypeToken<?> exactType, boolean matchRawType,
                                  Class<?> hierarchyType) {
            serializer = typeAdapter instanceof XmlSerializer
                    ? (XmlSerializer<?>) typeAdapter
                    : null;
            deserializer = typeAdapter instanceof XmlDeserializer
                    ? (XmlDeserializer<?>) typeAdapter
                    : null;
            $Gson$Preconditions.checkArgument(serializer != null || deserializer != null);
            this.exactType = exactType;
            this.matchRawType = matchRawType;
            this.hierarchyType = hierarchyType;
        }

        @SuppressWarnings("unchecked") // guarded by typeToken.equals() call
        public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> type) {
            boolean matches = exactType != null
                    ? exactType.equals(type) || matchRawType && exactType.getType() == type.getRawType()
                    : hierarchyType.isAssignableFrom(type.getRawType());
            return matches
                    ? new TreeTypeAdapter<T>((XmlSerializer<T>) serializer,
                    (XmlDeserializer<T>) deserializer, sml, type, this)
                    : null;
        }
    }
}
