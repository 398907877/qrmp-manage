package com.yanyan.core.serialize.exclusion;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.yanyan.core.serialize.exclusion.annotation.InputOnly;
import com.yanyan.core.serialize.exclusion.annotation.NoTransfer;

/**
 * 通过注解方式的序列化排除策略
 * User: Saintcy
 * Date: 2016/5/19
 * Time: 17:26
 */
public class AnnotationSerializeExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(InputOnly.class) != null || f.getAnnotation(NoTransfer.class) != null;
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
