package com.yanyan.core.spring.http.converter.form;

import com.google.gson.reflect.TypeToken;
import com.yanyan.core.spring.web.servlet.mvc.method.annotation.RequestModelMethodArgumentResolver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.util.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 对@RequestModel的参数，若消息类型为表单的，则将表单转换为对象。
 * 抄袭自{@link org.springframework.http.converter.FormHttpMessageConverter}
 * 本方法不适用于RequestResponseBodyMethodProcessor
 * User: Saintcy
 * Date: 2015/4/22
 * Time: 11:03
 */
public class FormHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> implements GenericHttpMessageConverter<Object> {
    private static final byte[] BOUNDARY_CHARS =
            new byte[]{'-', '_', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
                    'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
                    'V', 'W', 'X', 'Y', 'Z'};

    private final Random rnd = new Random();

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();

    private List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();

    /**
     * Construct a new {@code FormHttpMessageConverter}.
     */
    public FormHttpMessageConverter() {
        this.supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        this.supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);

        this.partConverters.add(new ByteArrayHttpMessageConverter());
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        stringHttpMessageConverter.setWriteAcceptCharset(false);
        this.partConverters.add(stringHttpMessageConverter);
        this.partConverters.add(new ResourceHttpMessageConverter());
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

    public boolean canWrite(Type type, Class<?> aClass, MediaType mediaType) {
        return canWrite(mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
        // should not be called, since we override canRead/Write instead
        //throw new UnsupportedOperationException();
    }

    /**
     * Set the list of {@link MediaType} objects supported by this converter.
     */
    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return Collections.unmodifiableList(this.supportedMediaTypes);
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
     * @param type the type for which to return the TypeToken
     * @return the type token
     */
    protected TypeToken<?> getTypeToken(Type type) {
        return TypeToken.get(type);
    }

    private Object readTypeToken(TypeToken<?> token, HttpInputMessage inputMessage) throws IOException {
        try {
            //这里需要从RequestModelMethodProcessor获取当前的dataBinderFactory
            //因为那里的对象是从RequestMappingHandlerAdapter.getDataBinderFactory获得的，在RequestMappingHandlerAdapter中传入了initializer和binderMethods
            //这样才能用到全局设置的conversionService，以及controller里面的@InitBinder
            WebDataBinderFactory dataBinderFactory = RequestModelMethodArgumentResolver.currentBinderFactory.get();
            Assert.notNull(dataBinderFactory);

            WebDataBinder binder = dataBinderFactory.createBinder(new ServletWebRequest(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()), BeanUtils.instantiateClass(token.getRawType()), "");

            Charset charset = getCharset(inputMessage.getHeaders());
            String body = StreamUtils.copyToString(inputMessage.getBody(), charset);

            String[] pairs = StringUtils.tokenizeToStringArray(body, "&");
            MutablePropertyValues mpvs = new MutablePropertyValues();
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                if (idx == -1) {
                    mpvs.add(URLDecoder.decode(pair, charset.name()), null);
                } else {
                    String name = URLDecoder.decode(pair.substring(0, idx), charset.name());
                    String value = URLDecoder.decode(pair.substring(idx + 1), charset.name());
                    mpvs.add(name, value);
                }
            }

            binder.bind(mpvs);

            if (binder.getBindingResult().hasErrors()) {
                throw new BindException(binder.getBindingResult());
            }

            return binder.getTarget();
        } catch (Exception ex) {
            throw new HttpMessageNotReadableException("Could not read FORM: " + ex.getMessage(), ex);
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
        try {
            MediaType contentType = outputMessage.getHeaders().getContentType();
            MultiValueMap<String, ?> map = convertObjectToMap(o, type);
            if (!isMultipart(map, contentType)) {
                writeForm((MultiValueMap<String, String>) map, contentType, outputMessage);
            } else {
                writeMultipart((MultiValueMap<String, Object>) map, outputMessage);
            }
        } catch (Exception ex) {
            throw new HttpMessageNotWritableException("Could not write FORM: " + ex.getMessage(), ex);
        }
    }

    protected MultiValueMap<String, Object> convertObjectToMap(Object o, Type type) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        convertFieldToMap("", o, type, map);
        return map;
    }

    protected void convertFieldToMap(String namePrefix, Object value, Type type, MultiValueMap<String, Object> map) {
        Class clazz = type == null ? (value == null ? null : value.getClass()) : TypeToken.get(type).getRawType();
        if (clazz != null) {
            if (clazz.isPrimitive()) {
                map.add(namePrefix, value);
                return;
            } else if (clazz.isAssignableFrom(Collection.class) || clazz.isArray()) {
                Collection valueList;
                if (clazz.isArray()) {
                    valueList = Arrays.asList(value);
                } else {
                    valueList = (Collection) value;
                }

                for (Object v : valueList) {
                    convertFieldToMap(namePrefix, v, null, map);
                }
            } else if (clazz.isAssignableFrom(Map.class)) {
                Map<String, Object> valueMap = (Map) value;
                for (Map.Entry entry : valueMap.entrySet()) {
                    convertFieldToMap(namePrefix + entry.getKey() + ".", entry.getValue(), null, map);
                }
            } else {
                Field[] fields = clazz.getFields();
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        if (field.getClass().isPrimitive()) {
                            map.add(namePrefix + field.getName(), field.get(value).toString());

                        } else {
                            convertFieldToMap(namePrefix + field.getName() + ".", field.get(value), null, map);
                        }
                    } catch (IllegalAccessException e) {
                        logger.warn("can't access field " + field.getName(), e);
                    }
                }
            }
        }
    }

    private boolean isMultipart(MultiValueMap<String, ?> map, MediaType contentType) {
        if (contentType != null) {
            return MediaType.MULTIPART_FORM_DATA.equals(contentType);
        }
        for (String name : map.keySet()) {
            for (Object value : map.get(name)) {
                if (value != null && !(value instanceof String)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void writeForm(MultiValueMap<String, String> form, MediaType contentType, HttpOutputMessage outputMessage)
            throws IOException {
        Charset charset = getCharset(outputMessage.getHeaders());
        if (contentType != null) {
            outputMessage.getHeaders().setContentType(contentType);
        } else {
            outputMessage.getHeaders().setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        StringBuilder builder = new StringBuilder();
        for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
            String name = nameIterator.next();
            for (Iterator<String> valueIterator = form.get(name).iterator(); valueIterator.hasNext(); ) {
                String value = valueIterator.next();
                builder.append(URLEncoder.encode(name, charset.name()));
                if (value != null) {
                    builder.append('=');
                    builder.append(URLEncoder.encode(value, charset.name()));
                    if (valueIterator.hasNext()) {
                        builder.append('&');
                    }
                }
            }
            if (nameIterator.hasNext()) {
                builder.append('&');
            }
        }
        byte[] bytes = builder.toString().getBytes(charset.name());
        outputMessage.getHeaders().setContentLength(bytes.length);
        StreamUtils.copy(bytes, outputMessage.getBody());
    }

    private void writeMultipart(MultiValueMap<String, Object> parts, HttpOutputMessage outputMessage)
            throws IOException {
        byte[] boundary = generateMultipartBoundary();

        Map<String, String> parameters = Collections.singletonMap("boundary", new String(boundary, "US-ASCII"));
        MediaType contentType = new MediaType(MediaType.MULTIPART_FORM_DATA, parameters);
        outputMessage.getHeaders().setContentType(contentType);

        writeParts(outputMessage.getBody(), parts, boundary);
        writeEnd(boundary, outputMessage.getBody());
    }

    private void writeParts(OutputStream os, MultiValueMap<String, Object> parts, byte[] boundary) throws IOException {
        for (Map.Entry<String, List<Object>> entry : parts.entrySet()) {
            String name = entry.getKey();
            for (Object part : entry.getValue()) {
                if (part != null) {
                    writeBoundary(boundary, os);
                    HttpEntity entity = getEntity(part);
                    writePart(name, entity, os);
                    writeNewLine(os);
                }
            }
        }
    }

    private void writeBoundary(byte[] boundary, OutputStream os) throws IOException {
        os.write('-');
        os.write('-');
        os.write(boundary);
        writeNewLine(os);
    }

    @SuppressWarnings("unchecked")
    private HttpEntity getEntity(Object part) {
        if (part instanceof HttpEntity) {
            return (HttpEntity) part;
        } else {
            return new HttpEntity(part);
        }
    }

    @SuppressWarnings("unchecked")
    private void writePart(String name, HttpEntity partEntity, OutputStream os) throws IOException {
        Object partBody = partEntity.getBody();
        Class<?> partType = partBody.getClass();
        HttpHeaders partHeaders = partEntity.getHeaders();
        MediaType partContentType = partHeaders.getContentType();
        for (HttpMessageConverter messageConverter : partConverters) {
            if (messageConverter.canWrite(partType, partContentType)) {
                HttpOutputMessage multipartOutputMessage = new MultipartHttpOutputMessage(os);
                multipartOutputMessage.getHeaders().setContentDispositionFormData(name, getFilename(partBody));
                if (!partHeaders.isEmpty()) {
                    multipartOutputMessage.getHeaders().putAll(partHeaders);
                }
                messageConverter.write(partBody, partContentType, multipartOutputMessage);
                return;
            }
        }
        throw new HttpMessageNotWritableException(
                "Could not write request: no suitable HttpMessageConverter found for request type [" +
                        partType.getName() + "]");
    }

    private void writeEnd(byte[] boundary, OutputStream os) throws IOException {
        os.write('-');
        os.write('-');
        os.write(boundary);
        os.write('-');
        os.write('-');
        writeNewLine(os);
    }

    private void writeNewLine(OutputStream os) throws IOException {
        os.write('\r');
        os.write('\n');
    }

    /**
     * Generate a multipart boundary.
     * <p>The default implementation returns a random boundary.
     * Can be overridden in subclasses.
     */
    protected byte[] generateMultipartBoundary() {
        byte[] boundary = new byte[rnd.nextInt(11) + 30];
        for (int i = 0; i < boundary.length; i++) {
            boundary[i] = BOUNDARY_CHARS[rnd.nextInt(BOUNDARY_CHARS.length)];
        }
        return boundary;
    }

    /**
     * Return the filename of the given multipart part. This value will be used for the
     * {@code Content-Disposition} header.
     * <p>The default implementation returns {@link Resource#getFilename()} if the part is a
     * {@code Resource}, and {@code null} in other cases. Can be overridden in subclasses.
     *
     * @param part the part to determine the file name for
     * @return the filename, or {@code null} if not known
     */
    protected String getFilename(Object part) {
        if (part instanceof Resource) {
            Resource resource = (Resource) part;
            return resource.getFilename();
        } else {
            return null;
        }
    }


    /**
     * Implementation of {@link org.springframework.http.HttpOutputMessage} used for writing multipart data.
     */
    private class MultipartHttpOutputMessage implements HttpOutputMessage {

        private final HttpHeaders headers = new HttpHeaders();

        private final OutputStream os;

        private boolean headersWritten = false;

        public MultipartHttpOutputMessage(OutputStream os) {
            this.os = os;
        }

        public HttpHeaders getHeaders() {
            return headersWritten ? HttpHeaders.readOnlyHttpHeaders(headers) : this.headers;
        }

        public OutputStream getBody() throws IOException {
            writeHeaders();
            return this.os;
        }

        private void writeHeaders() throws IOException {
            if (!this.headersWritten) {
                for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
                    byte[] headerName = getAsciiBytes(entry.getKey());
                    for (String headerValueString : entry.getValue()) {
                        byte[] headerValue = getAsciiBytes(headerValueString);
                        os.write(headerName);
                        os.write(':');
                        os.write(' ');
                        os.write(headerValue);
                        writeNewLine(os);
                    }
                }
                writeNewLine(os);
                this.headersWritten = true;
            }
        }

        protected byte[] getAsciiBytes(String name) {
            try {
                return name.getBytes("US-ASCII");
            } catch (UnsupportedEncodingException ex) {
                // should not happen, US-ASCII is always supported
                throw new IllegalStateException(ex);
            }
        }
    }
}
