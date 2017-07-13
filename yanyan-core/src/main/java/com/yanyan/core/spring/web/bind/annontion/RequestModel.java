package com.yanyan.core.spring.web.bind.annontion;

import java.lang.annotation.*;

/**
 * 跟@ModelAttribute+@RequestBody一样
 * User: Saintcy
 * Date: 2015/12/1
 * Time: 15:40
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestModel {
    String value() default "";//Model.attributes里面默认用类名作为key，如果指定value，则使用value作为key

    boolean required() default true;
}
