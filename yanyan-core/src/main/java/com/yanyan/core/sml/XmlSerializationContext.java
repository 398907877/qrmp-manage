package com.yanyan.core.sml;

import org.dom4j.Branch;

import java.lang.reflect.Type;

/**
 * <p>
 * Title: 序列化上下文环境
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public interface XmlSerializationContext {
    void serialize(Object src, Type typeOfT, String name, Branch parent);
}
