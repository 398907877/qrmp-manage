package com.yanyan.core.sml.internal.bind;

import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;


/**
 * <p>
 * Title: 类型转换器
 * </p>
 * <p>
 * Description: 由检测器检查是否符合条件，符合条件的返回适配器，不符合返回空
 * </p>
 *
 * @param <T>
 * @author Saintcy Don
 * @version 1.0
 */
public abstract class TypeAdapter<T> {

    public abstract void toXml(T src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context);

    public abstract T fromXml(Element xml, Type typeOfT, XmlDeserializationContext context);
}
