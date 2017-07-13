package com.yanyan.core.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;

/**
 * <p>
 * Title:DES 工具类。提供加密，解密，生成密钥等方法。
 * </p>
 * <p>
 * Description: 其中加密时先使用Base64.encodeBase64URLSafe编码，再加密，以避免乱码与HTTP传输空格问题
 * </p>
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class DESUtil {
    private static final String DES = "DES";

    /**
     * 生成密钥
     *
     * @return
     */
    public static Key generateKey() {
        try {
            KeyGenerator _generator = KeyGenerator.getInstance(DES);
            _generator.init(new SecureRandom());
            Key key = _generator.generateKey();

            return key;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成密钥
     *
     * @param value
     * @return
     */
    public static Key generateKey(byte[] value) {
        SecretKeyFactory keyFac = null;
        try {
            keyFac = SecretKeyFactory.getInstance(DES);
            DESKeySpec keySpec = new DESKeySpec(value);

            return keyFac.generateSecret(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 写密匙到指定文件
     *
     * @param key
     * @param fileName
     */
    public static void writeKey(Key key, String fileName) {
        try {
            ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(
                    fileName));
            keyFile.writeObject(key);
            keyFile.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 从密匙文件中读出密匙
     *
     * @param fileName
     * @return
     */
    public static Key readKey(String fileName) {
        try {
            ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream(
                    fileName));
            Key key = (Key) keyFile.readObject();
            keyFile.close();

            return key;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 加密byte[]明文输入,byte[]密文输出
     *
     * @param key
     * @param data
     * @return
     */
    public static byte[] encrypt(Key key, byte[] data) {
        byte[] raw = null;

        try {
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            raw = cipher.doFinal(data);

            raw = Base64.encodeBase64URLSafe(raw);

            return raw;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 解密 以byte[]密文输入,byte[]明文输出
     *
     * @param key
     * @param raw
     * @return
     */
    public static byte[] decrypt(Key key, byte[] raw) {
        byte[] data = null;

        try {
            Cipher cipher = Cipher.getInstance(DES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            raw = Base64.decodeBase64(raw);
            data = cipher.doFinal(raw);

            return data;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 由16进制字符生成公钥
     *
     * @param value
     * @return
     */
    public static Key generateKeyFromHex(String value) {
        SecretKeyFactory keyFac = null;
        try {
            keyFac = SecretKeyFactory.getInstance(DES);
            DESKeySpec keySpec = new DESKeySpec(
                    new BigInteger(value, 16).toByteArray());

            return keyFac.generateSecret(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 将字符串加密成16进制表示的字符串
     *
     * @param key
     * @param data
     * @return
     */
    public static String encryptToHex(Key key, String data) {
        byte[] byRaw = encrypt(key, data.getBytes());
        return new BigInteger(byRaw).toString(16);
    }

    /**
     * 将16进制表示的加密字符串解密成原始字符串
     *
     * @param key
     * @param data
     * @return
     */
    public static String decryptFromHex(Key key, String data) {
        byte[] byRaw = new BigInteger(data, 16).toByteArray();
        return new String(decrypt(key, byRaw));
    }
}
