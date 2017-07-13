package com.yanyan.core.spring.http.converter.xml;

import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.XmlConvertException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * User: Saintcy
 * Date: 2015/4/17
 * Time: 23:19
 */
public class SmlHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private String rootName;

    private Sml sml = new Sml();


    /**
     * Construct a new {@code SmlHttpMessageConverter}.
     */
    public SmlHttpMessageConverter() {
        super(new MediaType("application", "xml", DEFAULT_CHARSET),
                new MediaType("application", "*+xml", DEFAULT_CHARSET));
    }


    /**
     * Set the {@code Sml} instance to use.
     * If not set, a default {@link Sml#Sml() Sml} instance is used.
     * <p>Setting a custom-configured {@code Sml} is one way to take further
     * control of the XML serialization process.
     */
    public void setSml(Sml sml) {
        Assert.notNull(sml, "'sml' is required");
        this.sml = sml;
    }

    /**
     * Return the configured {@code Sml} instance for this converter.
     */
    public Sml getSml() {
        return this.sml;
    }

    /**
     * Defined the root name of xml
     *
     * @param rootName
     */
    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return canRead(mediaType);
    }

    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return canRead(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return canWrite(mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // should not be called, since we override canRead/Write instead
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        TypeToken<?> token = getTypeToken(clazz);
        return readTypeToken(token, inputMessage);
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

        TypeToken<?> token = getTypeToken(type);
        return readTypeToken(token, inputMessage);
    }

    /**
     * Return the Sml {@link com.google.gson.reflect.TypeToken} for the specified type.
     * <p>The default implementation returns {@code TypeToken.get(type)}, but
     * this can be overridden in subclasses to allow for custom generic
     * collection handling. For instance:
     * <pre class="code">
     * protected TypeToken<?> getTypeToken(Type type) {
     * if (type instanceof Class && List.class.isAssignableFrom((Class<?>) type)) {
     * return new TypeToken<ArrayList<MyBean>>() {};
     * }
     * else {
     * return super.getTypeToken(type);
     * }
     * }
     * </pre>
     *
     * @param type the type for which to return the TypeToken
     * @return the type token
     */
    protected TypeToken<?> getTypeToken(Type type) {
        return TypeToken.get(type);
    }

    private Object readTypeToken(TypeToken<?> token, HttpInputMessage inputMessage) throws IOException {
        Reader xml = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
        try {
            return this.sml.fromXml(xml, token.getType());
        } catch (XmlConvertException ex) {
            throw new HttpMessageNotReadableException("Could not read XML: " + ex.getMessage(), ex);
        }
    }

    private Charset getCharset(HttpHeaders headers) {
        if (headers == null || headers.getContentType() == null || headers.getContentType().getCharSet() == null) {
            return DEFAULT_CHARSET;
        }
        return headers.getContentType().getCharSet();
    }

    @Override
    protected void writeInternal(Object o, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        Charset charset = getCharset(outputMessage.getHeaders());
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
        try {
            if (type != null) {
                this.sml.toXml(o, type, StringUtils.isEmpty(rootName) ? (o == null ? "root" : o.getClass().getName()) : rootName, writer);
            } else {
                this.sml.toXml(o, StringUtils.isEmpty(rootName) ? (o == null ? "root" : o.getClass().getName()) : rootName, writer);
            }
            writer.close();
        } catch (XmlConvertException ex) {
            throw new HttpMessageNotWritableException("Could not write XML: " + ex.getMessage(), ex);
        }
    }

}
