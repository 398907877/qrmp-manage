package com.yanyan.core.sml;

import org.dom4j.Element;

import java.lang.reflect.Type;

/**
 * <p>
 * Title: 反序列化上下文环境
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public interface XmlDeserializationContext {
    <T> T deserialize(Element xml, Type typeOfT) throws XmlConvertException;
}
