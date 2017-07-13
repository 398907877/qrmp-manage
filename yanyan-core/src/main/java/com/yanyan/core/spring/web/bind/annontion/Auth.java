package com.yanyan.core.spring.web.bind.annontion;

import java.lang.annotation.*;

/**
 * 访问认证
 * User: Saintcy
 * Date: 2016/1/29
 * Time: 16:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
    Level value() default Level.RIGHT;
    String[] id() default {};

    enum Level {
        NONE, //无需任何认证
        LOGIN, //需要登录
        RIGHT //需要权限
    }
}
