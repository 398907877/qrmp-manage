package com.yanyan.core.spring.web.servlet.mvc.method.annotation;

import com.yanyan.core.spring.web.bind.annontion.RequestModel;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 请求模型对象参数解析器
 * User: Saintcy
 * Date: 2015/4/20
 * Time: 16:13
 */
public class RequestModelMethodArgumentResolver extends AbstractMessageConverterMethodArgumentResolver {
    public static final ThreadLocal<WebDataBinderFactory> currentBinderFactory = new NamedThreadLocal<WebDataBinderFactory>("Current Web Data Binder Factory");

    public RequestModelMethodArgumentResolver(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestModel.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        currentBinderFactory.set(binderFactory);//传给有需要这个对象的转换器，如FormHttpMessageConverter

        Object argument = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());

        String name = getNameForParameter(parameter);
        WebDataBinder binder = binderFactory.createBinder(webRequest, argument, name);

        if (argument != null) {
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }

        //将绑定好的数据放入model中
        Map<String, Object> bindingResultModel = binder.getBindingResult().getModel();
        mavContainer.removeAttributes(bindingResultModel);
        mavContainer.addAllAttributes(bindingResultModel);

        return argument;
    }

    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
                                                   MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException {
        final HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);

        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(inputMessage.getHeaders().getContentType())) {
            inputMessage = new ServletServerHttpRequest(servletRequest) {
                @Override
                public InputStream getBody() throws IOException {
                    return this.getServletRequest().getInputStream();//原生的会从getParameterMap里面取，大表单会没有数据
                }
            };
        }
        Object arg = super.readWithMessageConverters(inputMessage, methodParam, paramType);

        if (arg == null) {//TODO：Form的如何判别为空？
            if (methodParam.getParameterAnnotation(RequestModel.class).required()) {
                throw new HttpMessageNotReadableException("Required request model is missing: " +
                        methodParam.getMethod().toGenericString());
            }
        }

        return arg;
    }

    /**
     * Derives the model attribute name for a method parameter based on:
     * <ol>
     * <li>The parameter {@code @RequestModel} annotation value
     * <li>The parameter type
     * </ol>
     *
     * @return the derived name; never {@code null} or an empty string
     */
    protected static String getNameForParameter(MethodParameter parameter) {
        RequestModel annot = parameter.getParameterAnnotation(RequestModel.class);
        String attrName = (annot != null) ? annot.value() : null;
        return StringUtils.hasText(attrName) ? attrName : Conventions.getVariableNameForParameter(parameter);
    }
}

