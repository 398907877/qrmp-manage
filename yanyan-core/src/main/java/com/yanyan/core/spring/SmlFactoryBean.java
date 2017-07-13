package com.yanyan.core.spring;

import com.yanyan.core.serialize.exclusion.AnnotationDeserializeExclusionStrategy;
import com.yanyan.core.serialize.exclusion.AnnotationSerializeExclusionStrategy;
import com.yanyan.core.serialize.xml.adapter.CalendarTypeAdapter;
import com.yanyan.core.serialize.xml.adapter.ClassTypeAdapter;
import com.yanyan.core.serialize.xml.adapter.DateTypeAdapter;
import com.yanyan.core.serialize.xml.adapter.NumberTypeAdapter;
import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.SmlBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: Saintcy
 * Date: 2015/4/18
 * Time: 23:29
 */
public class SmlFactoryBean implements FactoryBean<Sml>, InitializingBean {
    private boolean base64EncodeByteArrays = false;
    private boolean serializeNulls = false;
    private boolean prettyPrinting = false;
    private boolean disableHtmlEscaping = false;
    private String dateFormatPattern;
    private Sml sml;

    public SmlFactoryBean() {
    }

    public void setBase64EncodeByteArrays(boolean base64EncodeByteArrays) {
        this.base64EncodeByteArrays = base64EncodeByteArrays;
    }

    public void setSerializeNulls(boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public void setPrettyPrinting(boolean prettyPrinting) {
        this.prettyPrinting = prettyPrinting;
    }

    public void setDisableHtmlEscaping(boolean disableHtmlEscaping) {
        this.disableHtmlEscaping = disableHtmlEscaping;
    }

    public void setDateFormatPattern(String dateFormatPattern) {
        this.dateFormatPattern = dateFormatPattern;
    }

    public void afterPropertiesSet() {
        SmlBuilder builder = new SmlBuilder();
        if (this.serializeNulls) {
            builder.serializeNulls();
        }

        if (this.prettyPrinting) {
            builder.setPrettyPrinting();
        }

        if (this.disableHtmlEscaping) {
            builder.disableHtmlEscaping();
        }

        if (this.dateFormatPattern != null) {
            builder.setDateFormat(this.dateFormatPattern);
        }

        if(this.base64EncodeByteArrays){
            builder.registerTypeAdapter(byte[].class, new DateTypeAdapter());
        }

        builder.addDeserializationExclusionStrategy(new AnnotationDeserializeExclusionStrategy());
        builder.addSerializationExclusionStrategy(new AnnotationSerializeExclusionStrategy());

        builder.registerTypeHierarchyAdapter(java.util.Date.class, new DateTypeAdapter(dateFormatPattern));
        builder.registerTypeAdapter(java.util.Calendar.class, new CalendarTypeAdapter(dateFormatPattern));
        Class[] classes = new Class[]{int.class, Integer.class,
                double.class, Double.class, long.class, Long.class,
                float.class, Float.class, byte.class, Byte.class, short.class,
                Short.class};
        for (int i = 0; i < classes.length; i++) {
            builder.registerTypeHierarchyAdapter(classes[i], new NumberTypeAdapter(classes[i]));
        }
        builder.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter());

        this.sml = builder.create();
    }

    public Sml getObject() {
        return this.sml;
    }

    public Class<?> getObjectType() {
        return Sml.class;
    }

    public boolean isSingleton() {
        return true;
    }
}