package com.gs.supply.component.encryption;

import android.util.Base64;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * @author husky
 * create on 2019/4/11
 */
public class AESEncrypt {

    /**
     * AES 加密数据
     * 默认 AES/CBC/PKCS5Padding类型
     *
     * @param data 需要加密的数据源
     * @param key  加密的key
     * @param iv   加密的偏移量
     * @return 加密后的数据
     */
    public static String encode(String data, String key, byte[] iv) {
        return encode(data, key, iv, EncryptionConstant.AES_CBC_PKCS5Padding);
    }

    /**
     * AES 解密数据
     * 默认 AES/CBC/PKCS5Padding类型
     *
     * @param data 需要解密的数据
     * @param key  key
     * @param iv   偏移量
     * @return 解密后的数据
     */
    public static String decode(String data, String key, byte[] iv) {
        return decode(data, key, iv, EncryptionConstant.AES_CBC_PKCS5Padding);

    }

    /**
     * AES 加密数据
     *
     * @param data     需要加密的数据源
     * @param key      加密的key
     * @param iv       加密的偏移量
     * @param instance AES加密的类型
     * @return 加密后的数据
     */
    public static String encode(String data, String key, byte[] iv, String instance) {
        try {
            byte[] bytes = data.getBytes(EncryptionConstant.ENCODING);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(EncryptionConstant.ENCODING), EncryptionConstant.AES);
            Cipher cipher = Cipher.getInstance(instance);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            return Base64.encodeToString(cipher.doFinal(bytes), Base64.DEFAULT).trim();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * AES 解密数据
     *
     * @param data     需要解密的数据
     * @param key      key
     * @param iv       加密的偏移量
     * @param instance AES加密的类型
     * @return 解密后的数据
     */
    public static String decode(String data, String key, byte[] iv, String instance) {
        try {
            byte[] bytes = Base64.decode(data, Base64.DEFAULT);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(EncryptionConstant.ENCODING), EncryptionConstant.AES);
            Cipher cipher = Cipher.getInstance(instance);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            return Base64.encodeToString(cipher.doFinal(bytes), Base64.DEFAULT).trim();
        } catch (Exception e) {
            return null;
        }

    }
}
