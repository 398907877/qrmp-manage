package com.yanyan.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.Validate;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;

/**
 * 密码工具
 * User: Saintcy
 * Date: 2016/3/24
 * Time: 10:51
 */
@Slf4j
public class PasswordUtils {
    private static final String SHA1 = "SHA-1";
    private static final String MD5 = "MD5";

    public static final int HASH_ITERATIONS = 1024;
    public static final int SALT_SIZE = 8;

    private static SecureRandom random = new SecureRandom();

    /**
     * 生成8字节的Salt
     *
     * @return
     */
    public static byte[] generate8ByteSalt() {
        return generateSalt(SALT_SIZE);
    }

    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);

        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            if (salt != null) {
                digest.update(salt);
            }

            byte[] result = digest.digest(input);

            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String encryptPassword(String plainPassword, byte[] salt) {
        byte[] hashPassword = digest(plainPassword.getBytes(), SHA1, salt, HASH_ITERATIONS);
        return Hex.encodeHexString(salt) + Hex.encodeHexString(hashPassword);
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param password      密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, byte[] salt, String password) {
        try {
            byte[] hashPassword = digest(plainPassword.getBytes(), SHA1, salt, HASH_ITERATIONS);
            return password.equals(Hex.encodeHexString(salt) + Hex.encodeHexString(hashPassword));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptPasswordWithRSA(RSAPrivateKey privateKey, String password){
        String decryptedPassword = RSAUtil.decryptFromHex(privateKey, password);
        log.debug("Private key modulus: {}",privateKey.getModulus().toString(16));
        log.debug("Private key exponent: {}",
                privateKey.getPrivateExponent().toString(16));
        log.debug("Decrypted password is: {}", decryptedPassword);
        return decryptedPassword;
    }

    public static void main(String[] args) {
        byte[] salt = PasswordUtils.generateSalt(8);
        String password = "000000";
        String encryptedPassword = PasswordUtils.encryptPassword(password, salt);
        System.out.println(Hex.encodeHexString(salt));
        System.out.println(encryptedPassword);
        System.out.println(PasswordUtils.validatePassword(password, salt, encryptedPassword));
        System.out.println(PasswordUtils.validatePassword(password + "1", salt, encryptedPassword));
    }
}
