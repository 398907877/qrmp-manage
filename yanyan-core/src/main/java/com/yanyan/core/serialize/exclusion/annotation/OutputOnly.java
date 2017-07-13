package com.yanyan.core.serialize.exclusion.annotation;

import java.lang.annotation.*;

/**
 * 输入类型
 * User: Saintcy
 * Date: 2016/5/19
 * Time: 17:28
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OutputOnly {
}
