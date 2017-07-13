package com.yanyan.core.sml.internal.bind;

import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.TypeAdapterFactory;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import com.google.gson.reflect.TypeToken;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 9:40
 */
public class ObjectTypeAdapter extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> type) {
            if (type.getRawType() == Object.class) {
                return (TypeAdapter<T>) new ObjectTypeAdapter(sml);
            }
            return null;
        }
    };

    private final Sml sml;

    public ObjectTypeAdapter(Sml sml) {
        this.sml = sml;
    }

    @Override
    public void toXml(Object src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {

    }

    @Override
    public Object fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
        return null;
    }
}
