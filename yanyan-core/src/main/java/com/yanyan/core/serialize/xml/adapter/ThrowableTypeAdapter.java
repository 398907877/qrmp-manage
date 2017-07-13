package com.yanyan.core.serialize.xml.adapter;

import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlDeserializer;
import com.yanyan.core.sml.XmlSerializationContext;
import com.yanyan.core.sml.XmlSerializer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * 异常XML格式适配器
 * User: Saintcy
 * Date: 2015/12/2
 * Time: 11:53
 */
public class ThrowableTypeAdapter implements XmlSerializer<Throwable>, XmlDeserializer<Throwable> {
    public Throwable deserialize(Element xml, Type typeOfT, XmlDeserializationContext context) {
        TypeToken<Throwable> typeToken = (TypeToken<Throwable>) TypeToken.get(typeOfT);
        try {
            Class clazz = typeToken.getRawType();
            Constructor constructor = clazz.getConstructor(String.class);
            if (constructor == null) {
                return (Throwable) clazz.newInstance();
            } else {
                return (Throwable) constructor.newInstance(xml.getText());
            }
        } catch (Exception e) {
            if (Error.class == typeOfT) {
                return new Error(xml.getText());
            } else {
                return new Exception(xml.getText());
            }
        }
    }

    public void serialize(Throwable src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        if (src != null) {
            Element element = parent.addElement(name);
            element.setText(ExceptionUtils.getRootCauseMessage(src));
        }
    }
}