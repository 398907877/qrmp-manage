package com.yanyan.core.serialize.json.adapter;

import com.google.gson.*;
import org.apache.commons.codec.binary.Base64;

import java.lang.reflect.Type;

/**
 * User: Saintcy
 * Date: 2015/4/18
 * Time: 23:35
 */
public class Base64TypeAdapter implements JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
    public Base64TypeAdapter() {
    }

    public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(Base64.encodeBase64URLSafeString(src));
    }

    public byte[] deserialize(JsonElement json, Type type, JsonDeserializationContext cxt) {
        return Base64.decodeBase64(json.getAsString());
    }
}