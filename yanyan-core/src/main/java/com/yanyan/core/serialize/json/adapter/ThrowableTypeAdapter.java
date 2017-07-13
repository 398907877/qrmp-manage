package com.yanyan.core.serialize.json.adapter;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

/**
 * 异常JSON格式适配器
 * User: Saintcy
 * Date: 2015/12/2
 * Time: 9:24
 */
public class ThrowableTypeAdapter implements JsonDeserializer<Throwable>,
        JsonSerializer<Throwable> {
    public Throwable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TypeToken<Throwable> typeToken = (TypeToken<Throwable>) TypeToken.get(typeOfT);
        try {
            Class clazz = typeToken.getRawType();
            Constructor constructor = clazz.getConstructor(String.class);
            if (constructor == null) {
                return (Throwable) clazz.newInstance();
            } else {
                return (Throwable) constructor.newInstance(json.getAsString());
            }
        } catch (Exception e) {
            if (Error.class == typeOfT) {
                return new Error(json.getAsString());
            } else {
                return new Exception(json.getAsString());
            }
        }
    }

    public JsonElement serialize(Throwable src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(ExceptionUtils.getRootCauseMessage(src));
    }
}
