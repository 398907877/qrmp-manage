package com.yanyan.core.sml.annontions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 9:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface XmlAdapter {
    Class<?> value();
}
