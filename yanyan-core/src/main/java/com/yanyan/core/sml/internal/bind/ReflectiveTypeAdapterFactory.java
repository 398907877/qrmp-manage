package com.yanyan.core.sml.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.*;
import com.yanyan.core.sml.internal.Excluder;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * <p>
 * Title: 普通对象类型适配器
 * </p>
 * <p>
 * Description: 逐个适配子对象
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;


    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
    }

    protected boolean serializeField(Class<?> declaringClazz, Field f, Type declaredType) {
        return !f.isSynthetic();
    }

    protected boolean deserializeField(Class<?> declaringClazz, Field f, Type declaredType) {
        return !f.isSynthetic();
    }

    protected String getFieldName(Class<?> declaringClazz, Field f, Type declaredType) {
        return f.getName();
    }

    public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
        Class<? super T> raw = typeToken.getRawType();

        if (!Object.class.isAssignableFrom(raw)) {
            return null; // it's a primitive!
        }

        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);
        return new Adapter<T>(constructor, typeToken, raw);
    }

    public final class Adapter<T> extends TypeAdapter<T> {
        private final ObjectConstructor<T> constructor;
        private final TypeToken<?> type;
        private final Class<?> raw;

        private Adapter(ObjectConstructor<T> constructor, TypeToken<?> type, Class<?> raw) {
            this.constructor = constructor;
            this.type = type;
            this.raw = raw;
        }

        public T fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            T object = null;
            if (xml != null) {
                object = constructor.construct();

                TypeToken<?> token = this.type;
                Class<?> clazz = this.raw;
                while (clazz != Object.class) {
                    Field[] fields = clazz.getDeclaredFields();
                    if (fields != null) {
                        for (Field field : fields) {
                            // System.out.println(field.toString());
                            field.setAccessible(true);
                            if (!excluder.excludeField(field, false)) {
                                try {
                                    Element child = xml.element(field.getName());

                                    Type type = $Gson$Types.resolve(token.getType(), clazz, field.getGenericType()); // 获取实际的类型，可以获取到泛型的具体信息
                                    field.set(object, context.deserialize(child, type));
                                } catch (Exception e) {
                                    throw new XmlConvertException("Set Field [" + object.getClass().getName() + "." + field.getName() + "] error", e);
                                }
                            }
                        }
                    }
                    //找父类中申明的字段，直到父类是Object
                    token = TypeToken.get($Gson$Types.resolve(token.getType(), clazz, clazz.getGenericSuperclass()));
                    clazz = token.getRawType();
                }
            }

            return object;
        }

        public void toXml(Object src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);

            TypeToken<?> token = this.type;
            Class<?> clazz = this.raw;
            while (clazz != Object.class) {
                Field[] fields = clazz.getDeclaredFields();
                if (fields != null) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        if (!excluder.excludeField(field, true)) {
                            // System.out.println(field.getType() + "==" + src.getClass());
                            try {
                                // System.out.println(field.get(src));
                                Object value = field.get(src);
                                context.serialize(value, value == null ? String.class : value.getClass(), field.getName(), self);
                            } catch (Exception e) {
                                throw new XmlConvertException(e);
                            }
                        }
                    }
                }
                token = TypeToken.get($Gson$Types.resolve(token.getType(), clazz, clazz.getGenericSuperclass()));
                clazz = token.getRawType();
            }
        }
    }
}
