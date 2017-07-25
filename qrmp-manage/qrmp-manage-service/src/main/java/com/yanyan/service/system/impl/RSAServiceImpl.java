package com.yanyan.service.system.impl;

import com.yanyan.Configs;
import com.yanyan.core.util.RSAUtil;
import com.yanyan.data.domain.system.RSAPrivateKey;
import com.yanyan.data.domain.system.RSAPublicKey;
import com.yanyan.service.system.RSAService;
import com.yanyan.service.BaseService;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

/**
 * RSA服务类
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 15:39
 */
@Service
public class RSAServiceImpl extends BaseService implements RSAService {
    public static KeyPair RSA_KEY_PAIR = RSAUtil.generateKeyPair();//从properties加载？

    public RSAPublicKey getPublicKey() {
        RSAPublicKey publicKey = new RSAPublicKey();
        publicKey.setModulus(((java.security.interfaces.RSAPublicKey) RSA_KEY_PAIR.getPublic()).getModulus()
                .toString(16));
        publicKey.setPublicExponent(((java.security.interfaces.RSAPublicKey) RSA_KEY_PAIR.getPublic())
                .getPublicExponent().toString(16));
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        RSAPrivateKey privateKey = new RSAPrivateKey();
        privateKey.setModulus(((java.security.interfaces.RSAPrivateKey) RSA_KEY_PAIR.getPrivate()).getModulus()
                .toString(16));
        privateKey.setPrivateExponent(((java.security.interfaces.RSAPrivateKey) RSA_KEY_PAIR.getPrivate())
                .getPrivateExponent().toString(16));
        return privateKey;
    }
}
