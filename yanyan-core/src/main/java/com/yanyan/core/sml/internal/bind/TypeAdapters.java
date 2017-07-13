package com.yanyan.core.sml.internal.bind;

import com.yanyan.core.sml.*;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Branch;
import org.dom4j.Element;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * <p>
 * Title: 基础数据类型适配器
 * </p>
 * <p>
 * Description:
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class TypeAdapters {
    public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
        public Boolean fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                return Boolean.valueOf(value);
            }
            return null;
        }

        public void toXml(Boolean src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#BOOLEAN";
        }
    };

    public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(boolean.class, Boolean.class, BOOLEAN);

    public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return null;
                } else {
                    return (byte) Integer.parseInt(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#BYTE";
        }
    };

    public static final TypeAdapterFactory BYTE_FACTORY = newFactory(byte.class, Byte.class, BYTE);

    public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return 0;
                } else {
                    return Integer.parseInt(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#SHORT";
        }
    };

    public static final TypeAdapterFactory SHORT_FACTORY = newFactory(short.class, Short.class, SHORT);

    public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return 0;
                } else {
                    return Integer.parseInt(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#INTEGER";
        }
    };

    public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(int.class, Integer.class, INTEGER);

    public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return 0L;
                } else {
                    return Long.parseLong(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#LONG";
        }
    };

    public static final TypeAdapterFactory LONG_FACTORY = newFactory(long.class, Long.class, LONG);

    public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return 0.0F;
                } else {
                    return Float.parseFloat(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#FLOAT";
        }
    };

    public static final TypeAdapterFactory FLOAT_FACTORY = newFactory(float.class, Float.class, FLOAT);

    public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return 0.0D;
                } else {
                    return Double.parseDouble(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#DOUBLE";
        }
    };

    public static final TypeAdapterFactory DOUBLE_FACTORY = newFactory(double.class, Double.class, DOUBLE);

    public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>() {
        public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isBlank(value)) {
                    return 0;
                } else {
                    return new LazilyParsedNumber(value);
                }
            }
            return null;
        }

        public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.toString());
        }

        public String toString() {
            return super.toString() + "#NUMBER";
        }
    };

    public static final TypeAdapterFactory NUMBER_FACTORY = newFactory(Number.class, NUMBER);

    public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
        public Character fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                String value = xml.getText();
                if (StringUtils.isEmpty(value)) {
                    return null;
                } else {
                    return value.charAt(0);
                }
            }
            return null;
        }

        public void toXml(Character src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : XmlHelper.escape(src.toString()));
        }

        public String toString() {
            return super.toString() + "#CHARACTER";
        }
    };

    public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(char.class, Character.class, CHARACTER);

    public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
        public String fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                return xml.getText();
            }
            return null;
        }

        public void toXml(String src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : XmlHelper.escape(src.toString()));
        }

        public String toString() {
            return super.toString() + "#STRING";
        }
    };

    public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>(){

        @Override
        public void toXml(BigDecimal src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : XmlHelper.escape(src.toString()));
        }

        @Override
        public BigDecimal fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if(xml==null&&StringUtils.isEmpty(xml.getTextTrim())){
                return null;
            }else {
                return  new BigDecimal(xml.getTextTrim());
            }
        }
    };

    public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>(){

        @Override
        public void toXml(BigInteger src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : XmlHelper.escape(src.toString()));
        }

        @Override
        public BigInteger fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if(xml==null&&StringUtils.isEmpty(xml.getTextTrim())){
                return null;
            }else {
                return  new BigInteger(xml.getTextTrim());
            }
        }
    };

    public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);

    public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>() {
        public StringBuilder fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                return new StringBuilder(xml.getText());
            }
            return null;
        }

        public void toXml(StringBuilder src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : XmlHelper.escape(src.toString()));
        }

        public String toString() {
            return super.toString() + "#STRING_BUILDER";
        }
    };

    public static final TypeAdapterFactory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);

    public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>() {
        public StringBuffer fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                return new StringBuffer(xml.getText());
            }
            return null;
        }

        public void toXml(StringBuffer src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : XmlHelper.escape(src.toString()));
        }

        public String toString() {
            return super.toString() + "#STRING_BUFFER";
        }
    };

    public static final TypeAdapterFactory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);

    @SuppressWarnings("rawtypes")
    public static final TypeAdapter<Enum> ENUM = new TypeAdapter<Enum>() {
        @SuppressWarnings("unchecked")
        public Enum fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (xml != null) {
                return Enum.valueOf((Class<Enum>) typeOfT, xml.getText());
            }
            return null;
        }

        public void toXml(Enum src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            Element self = parent.addElement(name);
            self.setText(src == null ? "" : src.name());
        }

        public String toString() {
            return super.toString() + "#ENUM";
        }
    };

    public static final TypeAdapterFactory ENUM_FACTORY = newTypeHierarchyFactory(Enum.class, ENUM);

    public static final TypeAdapter<Element> XML_ELEMENT = new TypeAdapter<Element>() {

        @Override
        public void toXml(Element src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            //TODO
        }

        @Override
        public Element fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            return null;
        }
    };

    public static final TypeAdapterFactory XML_ELEMENT_FACTORY
            = newTypeHierarchyFactory(Element.class, XML_ELEMENT);

    public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            // we use a runtime check to make sure the 'T's equal
            public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
                return typeToken.equals(type) ? (TypeAdapter<T>) typeAdapter : null;
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            // we use a runtime check to make sure the 'T's equal
            public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
                return typeToken.getRawType() == type ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            // we use a runtime check to make sure the 'T's equal
            public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == unboxed || rawType == boxed) ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + boxed.getName() + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub,
                                                                     final TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            // we use a runtime check to make sure the 'T's equal
            public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
                Class<? super T> rawType = typeToken.getRawType();
                return (rawType == base || rawType == sub) ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[type=" + base.getName() + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }

    public static <TT> TypeAdapterFactory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory() {
            @SuppressWarnings("unchecked")
            public <T> TypeAdapter<T> create(Sml sml, TypeToken<T> typeToken) {
                return clazz.isAssignableFrom(typeToken.getRawType()) ? (TypeAdapter<T>) typeAdapter : null;
            }

            @Override
            public String toString() {
                return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
            }
        };
    }
}
