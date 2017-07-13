package com.yanyan.core;

import org.apache.commons.lang3.StringUtils;

/**
 * 业务逻辑错误
 * User: Saintcy
 * Date: 2015/10/20
 * Time: 17:31
 */
public class ClassBasedBusinessException extends BusinessException {
    public ClassBasedBusinessException(String message, Throwable cause) {
        super("", message, cause);
        generateCode();
    }

    public ClassBasedBusinessException(String message) {
        super("", message);
        generateCode();
    }

    public ClassBasedBusinessException(Throwable cause) {
        super("", cause);
        generateCode();
    }

    public ClassBasedBusinessException() {
        super();
        generateCode();
    }

    protected void generateCode() {
        //默认错误代码为类名
        String[] aCode = StringUtils.splitByCharacterTypeCamelCase(getClass().getSimpleName());
        this.code = "ERR_" + StringUtils.upperCase(StringUtils.join(aCode, "_", 0, aCode.length - 1));//按类名去掉Exception
    }
}
