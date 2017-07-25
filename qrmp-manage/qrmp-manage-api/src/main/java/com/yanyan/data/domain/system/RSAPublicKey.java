package com.yanyan.data.domain.system;

import lombok.Data;

import java.io.Serializable;

/**
 * RSA公钥
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Data
public class RSAPublicKey implements Serializable {
    /**
     * 模数
     */
    private String modulus;
    /**
     * 指数
     */
    private String publicExponent;
}