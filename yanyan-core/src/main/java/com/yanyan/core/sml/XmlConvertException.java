package com.yanyan.core.sml;

public class XmlConvertException extends RuntimeException {
    private static final long serialVersionUID = -6768568404599557734L;

    public XmlConvertException(String msg) {
        super(msg);
    }

    public XmlConvertException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public XmlConvertException(Throwable cause) {
        super(cause);
    }
}
