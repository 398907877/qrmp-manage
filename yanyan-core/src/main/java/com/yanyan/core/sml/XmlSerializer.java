package com.yanyan.core.sml;

import org.dom4j.Branch;

import java.lang.reflect.Type;

/**
 * <p>
 * Title:序列化接口
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @param <T>
 * @author Saintcy Don
 * @version 1.0
 */
public interface XmlSerializer<T> {
    /**
     * 序列化对象为XML
     *
     * @param src       对象
     * @param typeOfSrc 对象类型
     * @param name      对象名称
     * @param context   转换上下文
     * @return 返回XML Element节点
     */
    public void serialize(T src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context);
}
