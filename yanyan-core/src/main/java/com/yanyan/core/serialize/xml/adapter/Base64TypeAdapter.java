package com.yanyan.core.serialize.xml.adapter;

import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlDeserializer;
import com.yanyan.core.sml.XmlSerializationContext;
import com.yanyan.core.sml.XmlSerializer;
import org.apache.commons.codec.binary.Base64;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;

/**
 * User: Saintcy
 * Date: 2015/4/18
 * Time: 23:35
 */
public class Base64TypeAdapter implements XmlSerializer<byte[]>, XmlDeserializer<byte[]> {
    public Base64TypeAdapter() {
    }

    public byte[] deserialize(Element xml, Type typeOfT, XmlDeserializationContext context) {
        return Base64.decodeBase64(xml.getText());
    }

    public void serialize(byte[] src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        if (src != null) {
            Element element = parent.addElement(name);
            element.setText(Base64.encodeBase64URLSafeString(src));
        }
    }
}