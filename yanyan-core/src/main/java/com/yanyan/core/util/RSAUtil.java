package com.yanyan.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA 工具类。提供加密，解密，生成密钥对等方法。
 * 1024位，RSA/ECB/PKCS1Padding
 * <p/>
 * 注意：中文要用URLEncode等方法转换后加密，解密时在用URLDecode转回来 因为计算时要把字符串转成的byte转成BigInteger,汉字的byte有cd,ef这种,所以转BigInteger的时候就会是复数,解密的时候就不准了
 *
 * @author Saintcy Don
 */
public class RSAUtil {

    /**
     * 生成密钥对
     *
     * @return
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            final int KEY_SIZE = 1024;// 没什么好说的了，这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
            keyPairGen.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();

            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 写密匙对到指定文件
     *
     * @param keyPair
     * @param fileName
     */
    public static void writeKeyPair(KeyPair keyPair, String fileName) {
        try {
            ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(fileName));
            keyFile.writeObject(keyPair);
            keyFile.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 从密匙文件中读出密匙对
     *
     * @param fileName
     * @return
     */
    public static KeyPair readKeyPair(String fileName) {
        try {
            ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream(fileName));
            KeyPair keyPair = (KeyPair) keyFile.readObject();
            keyFile.close();

            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 写公匙到指定文件
     *
     * @param key
     * @param fileName
     */
    public static void writePublicKey(PublicKey key, String fileName) {
        try {
            ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(fileName));
            keyFile.writeObject(key);
            keyFile.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 从公匙文件中读出公匙
     *
     * @param fileName
     * @return
     */
    public static PublicKey readPublicKey(String fileName) {
        try {
            ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream(fileName));
            PublicKey key = (PublicKey) keyFile.readObject();
            keyFile.close();

            return key;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 写私匙到指定文件
     *
     * @param key
     * @param fileName
     */
    public static void writePrivateKey(PrivateKey key, String fileName) {
        try {
            ObjectOutputStream keyFile = new ObjectOutputStream(new FileOutputStream(fileName));
            keyFile.writeObject(key);
            keyFile.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 从私匙文件中读出私匙
     *
     * @param fileName
     * @return
     */
    public static PrivateKey readPrivateKey(String fileName) {
        try {
            ObjectInputStream keyFile = new ObjectInputStream(new FileInputStream(fileName));
            PrivateKey key = (PrivateKey) keyFile.readObject();
            keyFile.close();

            return key;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成公钥
     *
     * @param modulus
     * @param publicExponent
     * @return
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 生成私钥
     *
     * @param modulus
     * @param privateExponent
     * @return
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 加密 * B=A^e1 mod n
     *
     * @param pk   加密的密钥
     * @param data 待加密的明文数据
     * @return 加密后的数据
     */
    public static byte[] encrypt(PublicKey pk, byte[] data) {
        try {
            BigInteger b = new BigInteger(data);
            BigInteger e1 = ((RSAPublicKey) pk).getPublicExponent();
            BigInteger m = ((RSAPublicKey) pk).getModulus();

            BigInteger a = b.modPow(e1, m);
            return a.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 解密 * A=B^e2 mod n
     *
     * @param pk  解密的密钥
     * @param raw 已经加密的数据
     * @return 解密后的明文
     */
    public static byte[] decrypt(PrivateKey pk, byte[] raw) {
        try {
            BigInteger a = new BigInteger(raw);
            BigInteger e2 = ((RSAPrivateKey) pk).getPrivateExponent();
            BigInteger m = ((RSAPrivateKey) pk).getModulus();

            BigInteger b = a.modPow(e2, m);
            return b.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

  /*=======================================-十六进制数据操作-=================================*/

    /**
     * 由16进制数据生成公钥
     *
     * @param modulus
     * @param publicExponent
     * @return
     */
    public static RSAPublicKey generateRSAPublicKeyFromHex(String modulus, String publicExponent) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus, 16), new BigInteger(publicExponent, 16));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 由16进制数据生成私钥
     *
     * @param modulus
     * @param privateExponent
     * @return
     */
    public static RSAPrivateKey generateRSAPrivateKeyFromHex(String modulus, String privateExponent) {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus, 16), new BigInteger(privateExponent, 16));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * 将字符串加密成16进制表示的字符串
     *
     * @param pk
     * @param data
     * @return
     */
    public static String encryptToHex(PublicKey pk, String data) {
        return new BigInteger(encrypt(pk, data.getBytes())).toString(16);
    }

    /**
     * 将16进制表示的加密字符串解密成原始字符串
     *
     * @param pk
     * @param data
     * @return
     */
    public static String decryptFromHex(PrivateKey pk, String data) {
        return new String(decrypt(pk, new BigInteger(data, 16).toByteArray()));
    }
}
