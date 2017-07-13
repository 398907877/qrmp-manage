package com.yanyan.core.spring.web.servlet.view.xml;


import com.yanyan.core.sml.Sml;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Saintcy
 * Date: 2015/4/18
 * Time: 8:37
 */
public class SmlXmlView extends AbstractView {
    public static final String DEFAULT_CONTENT_TYPE = "application/xml";

    public final static Charset UTF8 = Charset.forName("UTF-8");

    private Charset charset = UTF8;

    private Set<String> modelKeys;

    private boolean disableCaching = true;

    private boolean updateContentLength = false;

    private boolean extractValueFromSingleKeyModel = false;

    private Sml sml = new Sml();

    private String rootName;

    public SmlXmlView() {
        setContentType(DEFAULT_CONTENT_TYPE);
        setExposePathVariables(false);
    }

    /**
     * Set the attribute in the model that should be rendered by this view.
     * When set, all other model attributes will be ignored.
     */
    public void setModelKey(String modelKey) {
        this.modelKeys = Collections.singleton(modelKey);
    }

    /**
     * Set the attributes in the model that should be rendered by this view.
     * When set, all other model attributes will be ignored.
     */
    public void setModelKeys(Set<String> modelKeys) {
        this.modelKeys = modelKeys;
    }

    /**
     * Return the attributes in the model that should be rendered by this view.
     */
    public final Set<String> getModelKeys() {
        return this.modelKeys;
    }

    /**
     * Set the attributes in the model that should be rendered by this view.
     * When set, all other model attributes will be ignored.
     *
     * @deprecated use {@link #setModelKeys(Set)} instead
     */
    @Deprecated
    public void setRenderedAttributes(Set<String> renderedAttributes) {
        this.modelKeys = renderedAttributes;
    }

    /**
     * Return the attributes in the model that should be rendered by this view.
     *
     * @deprecated use {@link #getModelKeys()} instead
     */
    @Deprecated
    public final Set<String> getRenderedAttributes() {
        return this.modelKeys;
    }

    /**
     * Set whether to serialize models containing a single attribute as a map or whether to
     * extract the single value from the model and serialize it directly.
     * <p>The effect of setting this flag is similar to using {@code MappingJacksonHttpMessageConverter}
     * with an {@code @ResponseBody} request-handling method.
     * <p>Default is {@code false}.
     */
    public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel) {
        this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
    }

    /**
     * Disables caching of the generated XML.
     * <p>Default is {@code true}, which will prevent the client from caching the generated XML.
     */
    public void setDisableCaching(boolean disableCaching) {
        this.disableCaching = disableCaching;
    }

    /**
     * Whether to update the 'Content-Length' header of the response. When set to
     * {@code true}, the response is buffered in order to determine the content
     * length and set the 'Content-Length' header of the response.
     * <p>The default setting is {@code false}.
     */
    public void setUpdateContentLength(boolean updateContentLength) {
        this.updateContentLength = updateContentLength;
    }

    /**
     * Defined the root name of xml
     *
     * @param rootName
     */
    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public Sml getSml() {
        return sml;
    }

    public void setSml(Sml sml) {
        this.sml = sml;
    }

    @Override
    protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
        setResponseContentType(request, response);
        response.setCharacterEncoding(this.charset.name());
        if (this.disableCaching) {
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
            response.addDateHeader("Expires", 1L);
        }
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream stream = (this.updateContentLength ? createTemporaryOutputStream() : response.getOutputStream());
        Object value = filterModel(model);
        String text = sml.toXml(value, StringUtils.isEmpty(rootName) ? (value == null ? "root" : value.getClass().getName()) : rootName);
        byte[] bytes = text.getBytes(charset);
        stream.write(bytes);
        if (this.updateContentLength) {
            writeToResponse(response, (ByteArrayOutputStream) stream);
        }
    }

    /**
     * Filter out undesired attributes from the given model.
     * The return value can be either another {@link Map} or a single value object.
     * <p>The default implementation removes {@link BindingResult} instances and entries
     * not included in the {@link #setRenderedAttributes renderedAttributes} property.
     *
     * @param model the model, as passed on to {@link #renderMergedOutputModel}
     * @return the value to be rendered
     */
    protected Object filterModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<String, Object>(model.size());
        Set<String> renderedAttributes = (!CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet());
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return (this.extractValueFromSingleKeyModel && result.size() == 1 ? result.values().iterator().next() : result);
    }
}
