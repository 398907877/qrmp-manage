package com.yanyan.data.domain.system;

import lombok.Data;

import java.io.Serializable;

/**
 * RSA私钥
 * User: Saintcy
 * Date: 2017/5/27
 * Time: 10:16
 */
@Data
public class RSAPrivateKey implements Serializable {
    /**
     * 模数
     */
    private String modulus;
    /**
     * 指数
     */
    private String privateExponent;
}
