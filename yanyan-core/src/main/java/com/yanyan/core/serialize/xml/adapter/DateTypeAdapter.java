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
import java.util.Date;

public class DateTypeAdapter implements XmlSerializer<Date>, XmlDeserializer<Date> {
    private String dateFormatPattern;

    public DateTypeAdapter() {
        dateFormatPattern = "yyyy/MM/dd HH:mm:ss";
    }

    public DateTypeAdapter(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public Date deserialize(Element xml, Type typeOfT,
                            XmlDeserializationContext context) {
            return DateUtils.parseDate(xml.getTextTrim());
    }

    public void serialize(Date src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        Element element = parent.addElement(name);
        element.setText(DateFormatUtils.format(src, dateFormatPattern));
    }
}
