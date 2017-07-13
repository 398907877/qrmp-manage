package com.yanyan.core.db;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Saintcy
 * Date: 2015/7/7
 * Time: 15:05
 */
public class ParameterBuilder {
    private Map<String, Object> params = new HashMap<String, Object>();

    public ParameterBuilder() {

    }

    public ParameterBuilder(Object bean) {
        put(bean);
    }

    public ParameterBuilder(Map<String, Object> params) {
        put(params);
    }

    public ParameterBuilder(String name, Object value) {
        put(name, value);
    }


    public Map<String, Object> create() {
        return new HashMap<String, Object>(params);
    }


    /**
     * 加入一个bean作为参数
     *
     * @param bean
     * @return
     */
    public ParameterBuilder put(Object bean) {
        if (bean != null) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
            for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
                params.put(propertyDescriptor.getName(), beanWrapper.getPropertyValue(propertyDescriptor.getName()));
            }
        }

        return this;
    }

    /**
     * 加入一个map作为参数
     *
     * @param params
     * @return
     */
    public ParameterBuilder put(Map<String, Object> params) {
        this.params.putAll(params);

        return this;
    }

    /**
     * 加入参数
     *
     * @param name
     * @param value
     * @return
     */
    public ParameterBuilder put(String name, Object value) {
        params.put(name, value);

        return this;
    }

    /**
     * 移除参数
     *
     * @param name
     * @return
     */
    public ParameterBuilder remove(String name) {
        params.remove(name);

        return this;
    }

    /**
     * 清空参数
     *
     * @return
     */
    public ParameterBuilder clear() {
        params.clear();

        return this;
    }
}
