package com.yanyan.core.util;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * <p>
 * 最大加密长度为 117 bytes，大于117 bytes 需要分段加密。</br>
 * 非对称加密一般都用于加密对称加密算法的密钥，
 * 而不是直接加密内容。
 * 1024位，RSA/ECB/PKCS1Padding
 * </p>
 * User: Saintcy
 * Date: 2017/3/10
 * Time: 15:59
 */
public class RSAUtils {

    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String CHARSET = "utf-8";
    private static final int KEY_SIZE = 1024;

    private RSAUtils() {
    }

    /**
     * 获取密钥
     *
     * @return
     * @throws Exception
     */
    public static KeyPair generateKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    /**
     * 获取公钥
     *
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        return new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
    }

    /**
     * 获取私钥
     *
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair) {
        return new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String encryptByPrivateKey(String data, String key) throws Exception {
        byte[] results = encryptByPrivateKey(data.getBytes(CHARSET), key.getBytes());
        return new String(Base64.encodeBase64(results));
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

        if (data.length > (KEY_SIZE / 8 - 11)) {
            throw new IllegalBlockSizeException("Rsa encryptByPrivateKey Exception:Data must not be longer than " + (KEY_SIZE / 8 - 11) + " bytes.Data length:" + data.length);
        }

        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, String key) throws Exception {
        byte[] encrypt = encryptByPublicKey(data.getBytes(CHARSET), key.getBytes());
        return new String(Base64.encodeBase64(encrypt));
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

        if (data.length > (KEY_SIZE / 8 - 11)) {
            throw new IllegalBlockSizeException("Data must not be longer than " + (KEY_SIZE / 8 - 11) + " bytes.Data length:" + data.length);
        }

        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /*********************************************解密*********************************************************/

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, String key) throws Exception {
        byte[] dataBytes = Base64.decodeBase64(data.getBytes());
        byte[] resultBytes = decryptByPrivateKey(dataBytes, key.getBytes());
        return new String(resultBytes, CHARSET);
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }


    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String data, String key) throws Exception {
        byte[] decode = Base64.decodeBase64(data.getBytes());
        byte[] byPublicKey = decryptByPublicKey(decode, key.getBytes("utf-8"));
        return new String(byPublicKey, CHARSET);
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /*********************************************签名*********************************************************/

    public static String sign(String content, String privateKey, String charset) throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                Base64.decodeBase64(privateKey));
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey priKey = keyFactory.generatePrivate(priPKCS8);

        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initSign(priKey);
        signature.update(content.getBytes(charset));

        byte[] signed = signature.sign();

        return new String(Base64.encodeBase64(signed));
    }


    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encoding  字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encoding) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        byte[] encodedKey = Base64.decodeBase64(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes(encoding));

        boolean verified = signature.verify(Base64.decodeBase64(sign));
        return verified;
    }

    public static boolean doCheck(String content, String sign, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(publicKey);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes());

        boolean verified = signature.verify(Base64.decodeBase64(sign));
        return verified;
    }
}