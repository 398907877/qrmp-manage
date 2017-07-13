package com.yanyan.core.spring;

import com.yanyan.core.serialize.exclusion.AnnotationDeserializeExclusionStrategy;
import com.yanyan.core.serialize.exclusion.AnnotationSerializeExclusionStrategy;
import com.yanyan.core.serialize.json.adapter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * User: Saintcy
 * Date: 2015/4/18
 * Time: 23:15
 */
public class GsonFactoryBean implements FactoryBean<Gson>, InitializingBean {
    private boolean base64EncodeByteArrays = false;
    private boolean serializeNulls = false;
    private boolean prettyPrinting = false;
    private boolean disableHtmlEscaping = false;
    private String dateFormatPattern;
    private Gson gson;

    public GsonFactoryBean() {
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
        GsonBuilder builder = new GsonBuilder();
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
            builder.registerTypeAdapter(byte[].class, new Base64TypeAdapter());
        }

        builder.addDeserializationExclusionStrategy(new AnnotationDeserializeExclusionStrategy());
        builder.addSerializationExclusionStrategy(new AnnotationSerializeExclusionStrategy());

        builder.registerTypeHierarchyAdapter(java.util.Date.class, new DateTypeAdapter(dateFormatPattern));
        builder.registerTypeAdapter(java.util.Calendar.class, new CalendarTypeAdapter(dateFormatPattern));
        Class<Number>[] classes = new Class[]{int.class, Integer.class,
                double.class, Double.class, long.class, Long.class,
                float.class, Float.class, byte.class, Byte.class, short.class,
                Short.class};
        for (int i = 0; i < classes.length; i++) {
            builder.registerTypeHierarchyAdapter(classes[i], new NumberTypeAdapter(classes[i]));
        }

        builder.registerTypeHierarchyAdapter(Class.class, new ClassTypeAdapter());
        builder.registerTypeHierarchyAdapter(Throwable.class, new ThrowableTypeAdapter());

        this.gson = builder.create();
    }

    public Gson getObject() {
        return this.gson;
    }

    public Class<?> getObjectType() {
        return Gson.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
