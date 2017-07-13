package com.yanyan.core.serialize.xml.adapter;

import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlDeserializer;
import com.yanyan.core.sml.XmlSerializationContext;
import com.yanyan.core.sml.XmlSerializer;
import com.yanyan.core.util.DateUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.util.Calendar;

public class CalendarTypeAdapter implements XmlSerializer<Calendar>, XmlDeserializer<Calendar> {
    private String dateFormatPattern;

    public CalendarTypeAdapter() {
        dateFormatPattern = "yyyy/MM/dd HH:mm:ss";
    }

    public CalendarTypeAdapter(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public Calendar deserialize(Element xml, Type typeOfT,
                                XmlDeserializationContext context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(xml.getTextTrim()));
        return calendar;
    }

    public void serialize(Calendar src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        Element element = parent.addElement(name);
        element.setText(DateFormatUtils.format(src, dateFormatPattern));
    }
}
