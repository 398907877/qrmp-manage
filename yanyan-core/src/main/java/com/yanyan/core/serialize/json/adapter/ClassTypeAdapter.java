package com.yanyan.core.serialize.json.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * User: Saintcy
 * Date: 2015/10/22
 * Time: 15:13
 */
public class ClassTypeAdapter implements JsonDeserializer<Class>,
        JsonSerializer<Class> {

    public Class deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return Class.forName(json.getAsString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonElement serialize(Class src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}
