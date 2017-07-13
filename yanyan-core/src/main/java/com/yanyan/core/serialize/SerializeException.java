package com.yanyan.core.serialize;

/**
 * 序列化异常
 * User: Saintcy
 * Date: 2015/4/3
 * Time: 9:46
 */
public class SerializeException extends RuntimeException {

    public SerializeException(String s, Throwable e) {
        super(s, e);
    }
}
