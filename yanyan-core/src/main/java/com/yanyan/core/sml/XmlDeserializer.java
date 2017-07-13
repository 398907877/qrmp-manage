package com.yanyan.core.sml;

import org.dom4j.Element;

import java.lang.reflect.Type;

public interface XmlDeserializer<T> {
    /**
     * 反序列化XML为对象
     *
     * @param xml     父节点xml
     * @param typeOfT 要转换的类型
     * @param context 转换上下文
     * @return 返回转换后的对象
     */
    public T deserialize(Element xml, Type typeOfT, XmlDeserializationContext context);
}
