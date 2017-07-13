package com.yanyan.core.sml.internal.bind;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.TypeAdapterFactory;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Title:数组类型适配器
 * </p>
 * <p>
 * Description: 转入时从XML父节点取得所有的相同的兄弟节点转成数组；转出时由XML父节点建立本节点
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class ArrayTypeAdapter<E> extends TypeAdapter<Object> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
            Type type = typeToken.getType();
            if (!(type instanceof GenericArrayType || type instanceof Class && ((Class<?>) type).isArray())) {
                return null;
            }

            Type componentType = $Gson$Types.getArrayComponentType(type);
            TypeAdapter<?> componentTypeAdapter = sml.getAdapter(TypeToken.get(componentType));
            return new ArrayTypeAdapter(sml, componentTypeAdapter, $Gson$Types.getRawType(componentType));
        }
    };

    private final Class<E> componentType;
    private final TypeAdapter<E> componentTypeAdapter;

    public ArrayTypeAdapter(Sml context, TypeAdapter<E> componentTypeAdapter, Class<E> componentType) {
        this.componentTypeAdapter =
                new TypeAdapterRuntimeTypeWrapper<E>(context, componentTypeAdapter, componentType);
        this.componentType = componentType;
    }

    @SuppressWarnings("unchecked")
    public Object fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
        Class<?> componentType = ((Class<?>) typeOfT).getComponentType();

        List<Object> list = new ArrayList<Object>();
        List<Element> selives = xml.getParent().elements(xml.getName());// 通过父节点找到所有与相同的兄弟节点

        if (componentType.isArray()) {// 多维数组，需要继续遍历子数组
            for (Element self : selives) {
                list.add(context.deserialize(self.element(xml.getName()), componentType));// 找到第一个孩子即可
            }
        } else {
            for (Element self : selives) {
                list.add(context.deserialize(self, componentType));
            }
        }

        Object array = Array.newInstance(componentType, list.size());

        for (int i = 0; i < list.size(); i++) {
            // System.out.println(list.get(i));
            Array.set(array, i, list.get(i));
        }
        return array;
    }

    public void toXml(Object src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
        Class<?> componentType = ((Class<?>) typeOfSrc).getComponentType();
        // System.out.print(typeOfSrc+" ");
        // System.out.println(componentType);
        if (componentType != null && componentType.isArray()) {// 当下一层还是数组时，需要建立本层节点
            for (int i = 0, length = Array.getLength(src); i < length; i++) {
                Object value = Array.get(src, i);
                Element self = parent.addElement(name);
                context.serialize(value, value == null ? String.class : value.getClass(), name, self);
            }
        } else {// 否则不需要建立本层加点
            for (int i = 0, length = Array.getLength(src); i < length; i++) {
                Object value = Array.get(src, i);
                context.serialize(value, value == null ? String.class : value.getClass(), name, parent);
            }
        }
    }

}
