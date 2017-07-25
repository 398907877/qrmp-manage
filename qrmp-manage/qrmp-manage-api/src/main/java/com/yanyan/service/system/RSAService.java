package com.yanyan.service.system;

import com.yanyan.data.domain.system.RSAPublicKey;
import com.yanyan.data.domain.system.RSAPrivateKey;

/**
 * RSA服务
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 15:38
 */
public interface RSAService {
    /**
     * 获取RSA公钥
     *
     * @return
     */
    RSAPublicKey getPublicKey();

    /**
     * 获取RSA私钥
     * @return
     */
    RSAPrivateKey getPrivateKey();
}
