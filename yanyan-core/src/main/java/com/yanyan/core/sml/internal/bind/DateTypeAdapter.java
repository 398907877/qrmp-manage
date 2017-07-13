package com.yanyan.core.sml.internal.bind;

import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.TypeAdapterFactory;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * Title: 日期类型适配器
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings("unchecked") // we use a runtime check to make sure the 'T's equal
        public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
            return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new DateTypeAdapter() : null;
        }
    };

    private final DateFormat enUsFormat
            = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
    private final DateFormat localFormat
            = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
    private final DateFormat iso8601Format = buildIso8601Format();

    private static DateFormat buildIso8601Format() {
        DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return iso8601Format;
    }

    @Override
    public Date fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
        if (xml == null) {
            return null;
        }
        String date = xml.getTextTrim();
        try {
            return localFormat.parse(date);
        } catch (ParseException ignored) {
        }
        try {
            return enUsFormat.parse(date);
        } catch (ParseException ignored) {
        }
        try {
            return iso8601Format.parse(date);
        } catch (ParseException e) {
            throw new JsonSyntaxException(date, e);
        }
    }

    @Override
    public void toXml(Date src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        Element element = parent.addElement(name);
        if (src != null) {
            String dateFormatAsString = enUsFormat.format(src);
            element.setText(dateFormatAsString);
        }
    }

}
