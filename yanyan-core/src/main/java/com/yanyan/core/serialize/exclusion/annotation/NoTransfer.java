package com.yanyan.core.serialize.exclusion.annotation;

import java.lang.annotation.*;

/**
 * 不输入也不输出
 * User: Saintcy
 * Date: 2016/5/20
 * Time: 12:42
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoTransfer {
}
