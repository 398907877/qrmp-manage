package com.yanyan.core.serialize.json.adapter;

import com.google.gson.*;
import com.yanyan.core.util.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * 针对时间字符串的定制转换器
 *
 * @author Saintcy
 */
public class DateTypeAdapter implements JsonDeserializer<Date>,
        JsonSerializer<Date> {

    private String dateFormatPattern;

    public DateTypeAdapter() {
        dateFormatPattern = "yyyy/MM/dd HH:mm:ss";
    }

    public DateTypeAdapter(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public Date deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        return DateUtils.parseDate(json.getAsString());
    }

    public JsonElement serialize(Date src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        return new JsonPrimitive(DateFormatUtils.format(src, dateFormatPattern));
    }
}
