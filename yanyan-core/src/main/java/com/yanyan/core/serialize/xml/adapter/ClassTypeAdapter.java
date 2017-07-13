package com.yanyan.core.serialize.xml.adapter;

import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlDeserializer;
import com.yanyan.core.sml.XmlSerializationContext;
import com.yanyan.core.sml.XmlSerializer;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;

/**
 * User: Saintcy
 * Date: 2015/10/22
 * Time: 17:11
 */
public class ClassTypeAdapter implements XmlSerializer<Class>, XmlDeserializer<Class> {
    public Class deserialize(Element xml, Type typeOfT, XmlDeserializationContext context) {
        try {
            return Class.forName(xml.getText());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void serialize(Class src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        if (src != null) {
            Element element = parent.addElement(name);
            element.setText(src.toString());
        }
    }
}
