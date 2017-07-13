package com.yanyan.core.sml.internal.bind;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.Sml;
import com.yanyan.core.sml.TypeAdapterFactory;
import com.yanyan.core.sml.XmlDeserializationContext;
import com.yanyan.core.sml.XmlSerializationContext;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * Title: 集合类型适配器
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public final class CollectionTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public CollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!Collection.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);

        // create() doesn't define a type parameter
        TypeAdapter<T> result = new Adapter(constructor, elementType);
        return result;
    }

    private final class Adapter<E> extends TypeAdapter<Collection<E>> {
        private final Type elementType;
        private final ObjectConstructor<? extends Collection<E>> constructor;

        private Adapter(ObjectConstructor<? extends Collection<E>> constructor, Type elementType) {
            this.elementType = elementType;
            this.constructor = constructor;
        }

        @SuppressWarnings("unchecked")
        public Collection<E> fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            Collection<E> collection = null;
            if (xml != null) {
                collection = constructor.construct();

                List<Element> selives = xml.getParent().elements(xml.getName());// 从父节点取出本节点
                for (Element self : selives) {
                    if (this.elementType != null && this.elementType != Object.class) {
                        collection.add((E) context.deserialize(self, elementType));
                    } else {// 没有指定泛型
                        if (self.isTextOnly()) {// 如果没有子节点，则作为字符串
                            collection.add((E) context.deserialize(self, String.class));
                        } else {// 如果有子元素
                            List<Element> children = self.elements();
                            if (children.size() > 0 && StringUtils.equals(children.get(0).getName(), xml.getName())) {// 如果第一个子节点名称与本节点相同，则作为Collection, 即多维数组
                                // TODO:如果恰好子对象第一个名称与本节点相同怎么办？NND都怪xml没有数组概念，还是JSON好
                                collection.add((E) context.deserialize(children.get(0), typeOfT));
                            } else {// 如果子节点与本节点名称不同，则作为map对象
                                collection.add((E) context.deserialize(self, Map.class));
                            }
                        }
                    }
                }
            }

            return collection;
        }

        public void toXml(Collection<E> src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            for (Object o : src) {// 当下一层还是数组时，需要建立本层节点
                if (o != null && Collection.class.isAssignableFrom(o.getClass())) {
                    Element self = parent.addElement(name);
                    context.serialize(o, o == null ? String.class : o.getClass(), name, self);
                } else {// 否则无需建立本层节点
                    context.serialize(o, o == null ? String.class : o.getClass(), name, parent);
                }
            }
        }
    }
}
