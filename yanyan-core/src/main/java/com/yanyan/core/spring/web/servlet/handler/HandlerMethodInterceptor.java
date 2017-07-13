package com.yanyan.core.spring.web.servlet.handler;

import com.yanyan.core.spring.web.bind.annontion.ResponseModel;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * mvc 控制器中处理方法拦截器
 * 1、对结果模型数据进行过滤
 * 2、对结果中的错误信息进行识别抛出异常给异常处理器处理
 * User: Saintcy
 * Date: 2016/1/27
 * Time: 10:00
 */
public class HandlerMethodInterceptor extends HandlerInterceptorAdapter implements InitializingBean {
    protected Logger logger = LoggerFactory.getLogger(HandlerMethodInterceptor.class);
    private String errorsKey = "errors";//页面获取错误信息的关键字

    private ThreadLocal<Long> startTimeLocal = new NamedThreadLocal<Long>("start-time");//开始调用时间
    private Map<String, Object> attributes;

    public String getErrorsKey() {
        return errorsKey;
    }

    public void setErrorsKey(String errorsKey) {
        Assert.notNull(errorsKey, "errorsKey can't not be null");
        this.errorsKey = errorsKey;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void afterPropertiesSet() {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("" + handler + " begin");
        }
        request.setAttribute("home", request.getContextPath());
        request.setAttribute("referer", request.getHeader("Referer"));
        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                request.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        startTimeLocal.set(System.currentTimeMillis());//记录开始调用时间
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        /*if (modelAndView != null) {
            //取错误信息
            Map<String, List<String>> errors = getErrors(modelAndView.getModel());
            if (isReturnPageView(request)) {//若是返回页面，将错误信息加入Model信息中，方便读取
                modelAndView.addObject(errorsKey, errors);
            } else {//若返回的非页面，需要把不是返回数据（如：输入参数）的移除
                filterModel((HandlerMethod) handler, modelAndView.getModel());
            }

            //检查错误信息，如果有错误将错误抛出给错误处理器处理
            if (errors != null && !errors.isEmpty()) {
                throw new ModelAndViewException(modelAndView, errors);
            }
        }*/
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        if (logger.isDebugEnabled()) {
            long startTime = startTimeLocal.get();
            long endTime = System.currentTimeMillis();
            logger.debug("" + handler + " handle complete in {}ms.", endTime - startTime);
        }
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    /**
     * 获取模型中的错误信息
     *
     * @param model
     */
    protected final Map<String, List<String>> getErrors(Map<String, Object> model) {
        Map<String, List<String>> errorsMap = new HashMap<String, List<String>>();
        if (model != null) {
            for (Map.Entry<String, Object> entry : model.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Errors) {
                    Errors errors = (Errors) entry.getValue();
                    if (errors.hasFieldErrors()) {
                        for (FieldError fieldError : errors.getFieldErrors()) {
                            String key = fieldError.getObjectName() + "." + fieldError.getField();
                            List<String> errorMessages = errorsMap.get(key);
                            if (errorMessages == null) {
                                errorMessages = new ArrayList<String>();
                            }
                            errorMessages.add(fieldError.getDefaultMessage());
                            errorsMap.put(key, errorMessages);
                        }
                    }
                    if (errors.hasGlobalErrors()) {
                        for (ObjectError objectError : errors.getGlobalErrors()) {
                            String key = objectError.getObjectName();
                            List<String> errorMessages = errorsMap.get(key);
                            if (errorMessages == null) {
                                errorMessages = new ArrayList<String>();
                            }
                            errorMessages.add(objectError.getDefaultMessage());
                            errorsMap.put(objectError.getObjectName(), errorMessages);
                        }
                    }
                }
            }
        }
        return errorsMap;
    }

    /**
     * 过滤模型数据中不需要返回的数据
     *
     * @param handlerMethod
     * @param model
     */
    protected final void filterModel(HandlerMethod handlerMethod, Map<String, Object> model) {
        List<String> keys = new ArrayList<String>();
        ResponseModel responseModel = handlerMethod.getMethodAnnotation(ResponseModel.class);
        if (responseModel == null) return;
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (!(entry.getValue() instanceof BindingResult) && !ArrayUtils.contains(responseModel.value(), entry.getKey())) {
                //model.remove(entry.getKey());//会报错
                keys.add(entry.getKey());
            }
        }
        for (String key : keys) {
            model.remove(key);
        }
    }

    @SuppressWarnings("unchecked")
    private List<MediaType> getProducibleMediaTypes(HttpServletRequest request) {
        Set<MediaType> mediaTypes = (Set<MediaType>)
                request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        if (!CollectionUtils.isEmpty(mediaTypes)) {
            return new ArrayList<MediaType>(mediaTypes);
        } else {
            return Collections.singletonList(MediaType.ALL);
        }
    }


}
