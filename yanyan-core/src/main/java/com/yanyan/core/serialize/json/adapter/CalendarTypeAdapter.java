package com.yanyan.core.serialize.json.adapter;

import com.google.gson.*;
import com.yanyan.core.util.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;

/**
 * 针对时间字符串的定制转换器
 *
 * @author Saintcy
 */
public class CalendarTypeAdapter implements JsonDeserializer<Calendar>,
        JsonSerializer<Calendar> {

    private String dateFormatPattern;

    public CalendarTypeAdapter() {
        dateFormatPattern = "yyyy/MM/dd HH:mm:ss";
    }

    public CalendarTypeAdapter(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public Calendar deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(json.getAsString()));
        return calendar;
    }

    public JsonElement serialize(Calendar src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        return new JsonPrimitive(DateFormatUtils.format(src, dateFormatPattern));
    }
}
