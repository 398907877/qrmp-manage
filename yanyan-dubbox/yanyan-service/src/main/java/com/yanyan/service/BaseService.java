package com.yanyan.service;

import com.yanyan.core.spring.validator.BeanValidators;
import com.yanyan.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 服务基类
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 11:36
 */
public abstract class BaseService {
    /**
     * 验证Bean实例对象
     */
    @Autowired
    protected Validator validator;

    /**
     * 验证参数有效性
     *
     * @param object
     * @param groups
     */
    protected void validate(Object object, Class<?>... groups) {
        BeanValidators.validateWithException(validator, object, groups);
    }

    /**
     * 初始化数据绑定
     * 将字段中Date类型转换为String类型
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
        });
        // Date 类型转换
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(Timestamp.valueOf(text));
            }
        });
    }
}
