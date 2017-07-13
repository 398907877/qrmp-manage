package com.yanyan.core.sml;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.yanyan.core.sml.internal.Excluder;
import com.yanyan.core.sml.internal.bind.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * <p>
 * Title: XML与对象转换器
 * </p>
 * <p>
 * Description: tangsS's xML converter, 向Google学习:-)
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class Sml {
    static final boolean DEFAULT_XML_NON_EXECUTABLE = false;//XML没用

    /**
     * This thread local guards against reentrant calls to getAdapter(). In
     * certain object graphs, creating an adapter for a type may recursively
     * require an adapter for the same type! Without intervention, the recursive
     * lookup would stack overflow. We cheat by returning a proxy type adapter.
     * The proxy is wired up once the initial adapter has been created.
     */
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls
            = new ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>>();

    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache
            = Collections.synchronizedMap(new HashMap<TypeToken<?>, TypeAdapter<?>>());

    private final List<TypeAdapterFactory> factories;
    private final ConstructorConstructor constructorConstructor;

    private final boolean serializeNulls;
    private final boolean htmlSafe;
    private final boolean generateNonExecutableJson;
    private final boolean prettyPrinting;

    public Sml() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY,
                Collections.<Type, InstanceCreator<?>>emptyMap(), false, false, DEFAULT_XML_NON_EXECUTABLE,
                true, false, false, LongSerializationPolicy.DEFAULT,
                Collections.<TypeAdapterFactory>emptyList());
    }

    Sml(final Excluder excluder, final FieldNamingStrategy fieldNamingPolicy,
        final Map<Type, InstanceCreator<?>> instanceCreators, boolean serializeNulls,
        boolean complexMapKeySerialization, boolean generateNonExecutableSml, boolean htmlSafe,
        boolean prettyPrinting, boolean serializeSpecialFloatingPointValues,
        LongSerializationPolicy longSerializationPolicy,
        List<TypeAdapterFactory> typeAdapterFactories) {
        this.constructorConstructor = new ConstructorConstructor(instanceCreators);
        this.serializeNulls = serializeNulls;
        this.generateNonExecutableJson = generateNonExecutableSml;
        this.htmlSafe = htmlSafe;
        this.prettyPrinting = prettyPrinting;

        List<TypeAdapterFactory> factories = new ArrayList<TypeAdapterFactory>();

        // built-in type adapters that cannot be overridden
        factories.add(TypeAdapters.XML_ELEMENT_FACTORY);
        factories.add(ObjectTypeAdapter.FACTORY);

        // the excluder must precede all adapters that handle user-defined types
        factories.add(excluder);

        // user's type adapters
        factories.addAll(typeAdapterFactories);

        // type adapters for basic platform types
        factories.add(TypeAdapters.STRING_FACTORY);
        factories.add(TypeAdapters.INTEGER_FACTORY);
        factories.add(TypeAdapters.BOOLEAN_FACTORY);
        factories.add(TypeAdapters.BYTE_FACTORY);
        factories.add(TypeAdapters.SHORT_FACTORY);
        factories.add(TypeAdapters.newFactory(long.class, Long.class,
                longAdapter(longSerializationPolicy)));
        factories.add(TypeAdapters.newFactory(double.class, Double.class,
                doubleAdapter(serializeSpecialFloatingPointValues)));
        factories.add(TypeAdapters.newFactory(float.class, Float.class,
                floatAdapter(serializeSpecialFloatingPointValues)));
        factories.add(TypeAdapters.NUMBER_FACTORY);
        factories.add(TypeAdapters.CHARACTER_FACTORY);
        factories.add(TypeAdapters.STRING_BUILDER_FACTORY);
        factories.add(TypeAdapters.STRING_BUFFER_FACTORY);
        factories.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        factories.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        //factories.add(TypeAdapters.URL_FACTORY);
        //factories.add(TypeAdapters.URI_FACTORY);
        //factories.add(TypeAdapters.UUID_FACTORY);
        //factories.add(TypeAdapters.LOCALE_FACTORY);
        //factories.add(TypeAdapters.INET_ADDRESS_FACTORY);
        //factories.add(TypeAdapters.BIT_SET_FACTORY);
        factories.add(DateTypeAdapter.FACTORY);
        //factories.add(TypeAdapters.CALENDAR_FACTORY);
        //factories.add(TimeTypeAdapter.FACTORY);
        //factories.add(SqlDateTypeAdapter.FACTORY);
        //factories.add(TypeAdapters.TIMESTAMP_FACTORY);
        factories.add(ArrayTypeAdapter.FACTORY);
        //factories.add(TypeAdapters.CLASS_FACTORY);

        // type adapters for composite and user-defined types
        factories.add(new CollectionTypeAdapterFactory(constructorConstructor));
        factories.add(new MapTypeAdapterFactory(constructorConstructor, complexMapKeySerialization));
        factories.add(new XmlAdapterAnnotationTypeAdapterFactory(constructorConstructor));
        factories.add(TypeAdapters.ENUM_FACTORY);
        factories.add(new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingPolicy, excluder));

        this.factories = Collections.unmodifiableList(factories);
    }

    private TypeAdapter<Number> doubleAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>() {
            @Override
            public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
                Element element = parent.addElement(name);
                if (src != null) {
                    double doubleValue = src.doubleValue();
                    checkValidFloatingPoint(doubleValue);
                    element.setText(String.valueOf(doubleValue));
                }
            }

            @Override
            public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
                if (xml == null || StringUtils.isEmpty(xml.getTextTrim())) {
                    return null;
                }
                return NumberUtils.toDouble(xml.getTextTrim());
            }
        };
    }

    private TypeAdapter<Number> floatAdapter(boolean serializeSpecialFloatingPointValues) {
        if (serializeSpecialFloatingPointValues) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>() {
            @Override
            public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
                Element element = parent.addElement(name);
                if (src != null) {
                    float floatValue = src.floatValue();
                    checkValidFloatingPoint(floatValue);
                    element.setText(String.valueOf(floatValue));
                }
            }

            @Override
            public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
                if (xml == null || StringUtils.isEmpty(xml.getTextTrim())) {
                    return null;
                }
                return NumberUtils.toFloat(xml.getTextTrim());
            }
        };
    }

    private void checkValidFloatingPoint(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(value
                    + " is not a valid double value as per XML specification. To override this"
                    + " behavior, use SmlBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }

    private TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>() {

            @Override
            public void toXml(Number src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
                Element element = parent.addElement(name);
                if (src != null) {
                    element.setText(src.toString());
                }
            }

            @Override
            public Number fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
                if (xml == null) {
                    return null;
                } else {
                    return NumberUtils.toLong(xml.getTextTrim());
                }
            }
        };
    }

    /**
     * Returns the type adapter for {@code} type.
     *
     * @throws IllegalArgumentException if this GSON cannot serialize and
     *                                  deserialize {@code type}.
     */
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getAdapter(TypeToken<T> type) {
        TypeAdapter<?> cached = typeTokenCache.get(type);
        if (cached != null) {
            return (TypeAdapter<T>) cached;
        }

        Map<TypeToken<?>, FutureTypeAdapter<?>> threadCalls = calls.get();
        boolean requiresThreadLocalCleanup = false;
        if (threadCalls == null) {
            threadCalls = new HashMap<TypeToken<?>, FutureTypeAdapter<?>>();
            calls.set(threadCalls);
            requiresThreadLocalCleanup = true;
        }

        // the key and value type parameters always agree
        FutureTypeAdapter<T> ongoingCall = (FutureTypeAdapter<T>) threadCalls.get(type);
        if (ongoingCall != null) {
            return ongoingCall;
        }

        try {
            FutureTypeAdapter<T> call = new FutureTypeAdapter<T>();
            threadCalls.put(type, call);

            for (TypeAdapterFactory factory : factories) {
                TypeAdapter<T> candidate = factory.create(this, type);
                if (candidate != null) {
                    call.setDelegate(candidate);
                    typeTokenCache.put(type, candidate);
                    return candidate;
                }
            }
            throw new IllegalArgumentException("GSON cannot handle " + type);
        } finally {
            threadCalls.remove(type);

            if (requiresThreadLocalCleanup) {
                calls.remove();
            }
        }
    }

    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory skipPast, TypeToken<T> type) {
        boolean skipPastFound = false;
        // Skip past if and only if the specified factory is present in the factories.
        // This is useful because the factories created through JsonAdapter annotations are not
        // registered in this list.
        if (!factories.contains(skipPast)) skipPastFound = true;

        for (TypeAdapterFactory factory : factories) {
            if (!skipPastFound) {
                if (factory == skipPast) {
                    skipPastFound = true;
                }
                continue;
            }

            TypeAdapter<T> candidate = factory.create(this, type);
            if (candidate != null) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("GSON cannot serialize " + type);
    }

    /**
     * Returns the type adapter for {@code} type.
     *
     * @throws IllegalArgumentException if this GSON cannot serialize and
     *                                  deserialize {@code type}.
     */
    public <T> TypeAdapter<T> getAdapter(Class<T> type) {
        return getAdapter(TypeToken.get(type));
    }


    /**
     * XML转为对象
     *
     * @param xml
     * @param classOfT
     * @return
     */
    public <T> T fromXml(String xml, Class<T> classOfT) {
        return fromXml(xml, (Type) classOfT);
    }

    public <T> T fromXml(String xml, Type typeOfT) {
        return fromXml(new StringReader(xml), typeOfT);
    }


    public <T> T fromXml(Reader xml, Class<T> classOfT) {
        return fromXml(xml, (Type) classOfT);
    }

    public <T> T fromXml(Reader xml, Type typeOfT) {
        XmlDeserializationContext deserializationContext = new XmlDeserializationContext() {

            public <TT> TT deserialize(Element xml, Type typeOfT) throws XmlConvertException {

                if (xml == null) {
                    return null;
                }

                return fromXml(xml, typeOfT, this);
            }
        };

        org.dom4j.io.SAXReader xmlreader = new org.dom4j.io.SAXReader();
        // org.dom4j.io.XPPReader xmlreader = new org.dom4j.io.XPPReader();
        //org.dom4j.io.XPP3Reader xmlreader = new org.dom4j.io.XPP3Reader();// xpp3貌似最快 对特殊字符读取有问题
        Document document;
        try {
            document = xmlreader.read(xml);
        } catch (Exception e) {
            throw new XmlConvertException(e);
        }

        return deserializationContext.deserialize(document.getRootElement(), typeOfT);
    }

    @SuppressWarnings("unchecked")
    private <T> T fromXml(Element parent, Type typeOfT, XmlDeserializationContext deserializationContext) {
        TypeAdapter<T> adapter = (TypeAdapter<T>) getAdapter(TypeToken.get(typeOfT));
        if (adapter == null) {
            throw new XmlConvertException("Can't convert type " + typeOfT);
        }
        // System.out.println(adapter);
        return (T) adapter.fromXml(parent, typeOfT, deserializationContext);
    }

    public void toXml(Object src, final Type typeOfT, String name, Writer writer) {
        XmlSerializationContext serializationContext = new XmlSerializationContext() {
            public void serialize(Object src, Type typeOfT, String name, Branch parent) {
                if (src == null) {
                    if (serializeNulls) {
                        toXmlTree("", String.class, name, parent, this);
                    }
                } else {
                    toXmlTree(src, typeOfT, name, parent, this);
                }
            }
        };

        Document document = DocumentHelper.createDocument();
        serializationContext.serialize(src, typeOfT, name, document);// 先加一个默认根，后面再去掉


        XMLWriter out = new XMLWriter(writer, OutputFormat.createPrettyPrint());

        try {
            out.write(document.getRootElement());
            out.flush();
        } catch (IOException e) {
            throw new XmlConvertException(e);
        }
    }

    public void toXml(Object src, String name, Writer writer) {
        if (src != null) {
            toXml(src, src.getClass(), name, writer);
        } else {
            //toXml(XmlNull.INSTANCE, writer);
        }
    }

    /**
     * 对象转为XML
     *
     * @param src
     * @return
     */
    public String toXml(Object src, String name) {
        StringWriter writer = new StringWriter();
        toXml(src, name, writer);
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    private <T> void toXmlTree(T src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext serializationContext) {
        TypeAdapter<T> adapter = (TypeAdapter<T>) getAdapter(TypeToken.get(typeOfSrc));

        if (adapter == null) {
            throw new XmlConvertException("Can't convert type " + typeOfSrc);
        }
        // System.out.println(adapter);
        adapter.toXml(src, typeOfSrc, name, parent, serializationContext);
    }

    static class FutureTypeAdapter<T> extends TypeAdapter<T> {
        private TypeAdapter<T> delegate;

        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (delegate != null) {
                throw new AssertionError();
            }
            delegate = typeAdapter;
        }

        @Override
        public void toXml(T src, Type typeOfSrc, String name, Branch parent, XmlSerializationContext context) {
            if (delegate == null) {
                throw new IllegalStateException();
            }
            delegate.toXml(src, typeOfSrc, name, parent, context);
        }

        @Override
        public T fromXml(Element xml, Type typeOfT, XmlDeserializationContext context) {
            if (delegate == null) {
                throw new IllegalStateException();
            }
            return delegate.fromXml(xml, typeOfT, context);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder("{serializeNulls:")
                .append(serializeNulls)
                .append("factories:").append(factories)
                .append(",instanceCreators:").append(constructorConstructor)
                .append("}")
                .toString();
    }
}
