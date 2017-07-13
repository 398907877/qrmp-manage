package com.yanyan.core.serialize.json.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

public class NumberTypeAdapter implements JsonDeserializer<Number>, JsonSerializer<Number> {
    private Class<Number> clazz;

    public NumberTypeAdapter(Class<Number> clazz) {
        this.clazz = clazz;
    }

    public Number deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        Number result = null;

        if (json.getAsString() == null || json.getAsString().trim().length() == 0) {
            if (clazz.isPrimitive()) {
                return 0;
            } else {
                return null;
            }
        } else {
            String value = json.getAsString().trim();
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

    public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement result;
        if (src == null) {
            result = null;
        } else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
            result = new JsonPrimitive(new Byte(src.toString()));
        } else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
            result = new JsonPrimitive(new Long(src.toString()));
        } else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
            result = new JsonPrimitive(new Integer(src.toString()));
        } else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
            result = new JsonPrimitive(new Short(src.toString()));
        } else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
            result = new JsonPrimitive(new Double(src.toString()));
        } else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
            result = new JsonPrimitive(new Float(src.toString()));
        } else {
            result = new JsonPrimitive(src.toString());
        }

        return result;
    }
}
