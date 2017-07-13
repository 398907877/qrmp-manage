package com.yanyan.core.sml;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 11:20
 */

import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This type adapter supports three subclasses of date: Date, Timestamp, and
 * java.sql.Date.
 *
 * @author Inderjeet Singh
 * @author Joel Leitch
 */
final class DefaultDateTypeAdapter implements XmlSerializer<Date>, XmlDeserializer<Date> {

    // TODO: migrate to streaming adapter

    private final DateFormat enUsFormat;
    private final DateFormat localFormat;
    private final DateFormat iso8601Format;

    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US),
                DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT));
    }

    DefaultDateTypeAdapter(String datePattern) {
        this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
    }

    DefaultDateTypeAdapter(int style) {
        this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
    }

    public DefaultDateTypeAdapter(int dateStyle, int timeStyle) {
        this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US),
                DateFormat.getDateTimeInstance(dateStyle, timeStyle));
    }

    DefaultDateTypeAdapter(DateFormat enUsFormat, DateFormat localFormat) {
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;
        this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        this.iso8601Format.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    // These methods need to be synchronized since JDK DateFormat classes are not thread-safe
    // See issue 162
    public void serialize(Date src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        synchronized (localFormat) {
            String dateFormatAsString = enUsFormat.format(src);
            parent.addElement(name).setText(dateFormatAsString);
        }
    }

    public Date deserialize(Element xml, Type typeOfT, XmlDeserializationContext context) {
        Date date = deserializeToDate(xml);
        if (typeOfT == Date.class) {
            return date;
        } else if (typeOfT == Timestamp.class) {
            return new Timestamp(date.getTime());
        } else if (typeOfT == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        } else {
            throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
        }
    }

    private Date deserializeToDate(Element xml) {
        synchronized (localFormat) {
            try {
                return localFormat.parse(xml.getTextTrim());
            } catch (ParseException ignored) {
            }
            try {
                return enUsFormat.parse(xml.getTextTrim());
            } catch (ParseException ignored) {
            }
            try {
                return iso8601Format.parse(xml.getTextTrim());
            } catch (ParseException e) {
                throw new XmlConvertException(xml.getTextTrim(), e);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(DefaultDateTypeAdapter.class.getSimpleName());
        sb.append('(').append(localFormat.getClass().getSimpleName()).append(')');
        return sb.toString();
    }


}