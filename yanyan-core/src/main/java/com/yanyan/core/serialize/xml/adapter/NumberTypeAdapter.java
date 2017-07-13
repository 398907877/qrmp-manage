package com.yanyan.core.serialize.xml.adapter;

import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlDeserializer;
import com.yanyan.core.sml.XmlSerializationContext;
import com.yanyan.core.sml.XmlSerializer;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;

public class NumberTypeAdapter implements XmlDeserializer<Number>, XmlSerializer<Number> {
    private Class<Number> clazz;

    public NumberTypeAdapter(Class<Number> clazz) {
        this.clazz = clazz;
    }

    public Number deserialize(Element xml, Type typeOfT, XmlDeserializationContext context) {
        Number result = null;

        if (xml.getTextTrim() == null || xml.getTextTrim().length() == 0) {
            if (clazz.isPrimitive()) {
                return 0;
            } else {
                return null;
            }
        } else {
            String value = xml.getTextTrim();
            int index = value.indexOf(".");
            String preValue = value;
            if (index >= 0) {
                if (index > 0) preValue = value.substring(0, index);
                else preValue = "0";
            }

            if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                result = new Byte(preValue);
            } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                result = new Long(preValue);
            } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                result = new Integer(preValue);
            } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                result = new Short(preValue);
            } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
                result = new Double(value);
            } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                result = new Float(value);
            }
        }

        return result;
    }

    public void serialize(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        if (src == null) {
            return;
        } else {
            Element result = parent.addElement(name);
            if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
                result.setText(new Byte(src.toString()).toString());
            } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
                result.setText(new Long(src.toString()).toString());
            } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
                result.setText(new Integer(src.toString()).toString());
            } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
                result.setText(new Short(src.toString()).toString());
            } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
                result.setText(new Double(src.toString()).toString());
            } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
                result.setText(new Float(src.toString()).toString());
            } else {
                result.setText(src.toString());
            }
        }
    }
}
