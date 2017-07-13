package com.yanyan.core.sml.internal.bind;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.TypeAdapterFactory;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: Map转换类
 * </p>
 * <p>
 * Description: 如果是对象转为Map，如果是列表转为ArrayList，其余是String
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class MapTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    @SuppressWarnings("unused")
    private final boolean complexMapKeySerialization;// 暂时不用

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean complexMapKeySerialization) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = complexMapKeySerialization;
    }

    public <T> TypeAdapter<T> create(final Sml sml, TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        if (!Map.class.isAssignableFrom(rawType)) {
            return null;
        }

        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

        @SuppressWarnings({"unchecked", "rawtypes"})
        // we don't define a type parameter for the key or value types
                TypeAdapter<T> result = new Adapter(constructor);
        return result;
    }

    private final class Adapter<K, V> extends TypeAdapter<Map<K, V>> {
        private final ObjectConstructor<? extends Map<K, V>> constructor;

        public Adapter(ObjectConstructor<? extends Map<K, V>> constructor) {
            this.constructor = constructor;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        public Map<K, V> fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {

            Map<K, V> map = null;
            if (xml != null) {
                map = constructor.construct();

                List<Element> children = xml.elements();// 从父节点取出本节点
                for (Element child : children) {
                    Object value1 = map.get(child.getName());
                    Object value2 = null;
                    if (child.isTextOnly()) {
                        value2 = context.deserialize(child, String.class);
                    } else {
                        value2 = context.deserialize(child, typeOfT);
                    }
                    if (value1 == null) {// 单个名称一致的节点，暂不放入列表
                        map.put((K) child.getName(), (V) value2);
                    } else if (value1 instanceof List) {// 已经有多个了，直接放入已创建的列表中
                        ((List) value1).add(value2);
                    } else {// 已经有一个了，需要建一个列表，一起放进去
                        List list = new ArrayList();
                        list.add(value1);
                        map.put((K) child.getName(), (V) list);
                        list.add(value2);
                    }
                }
            }

            return map;
        }

        public void toXml(Map<K, V> src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            for (Map.Entry<K, V> entry : src.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                context.serialize(value, value == null ? String.class : value.getClass(), (String) key, self);
            }
        }
    }
}
