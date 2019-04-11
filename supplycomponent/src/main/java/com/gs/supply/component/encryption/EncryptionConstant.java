package com.gs.supply.component.encryption;

/**
 * 加密解密相关的常量
 * @author husky
 * create on 2019/4/11
 */
public class EncryptionConstant {
    /**
     * 编码规范
     */
    public static final String ENCODING = "UTF-8";
    /**
     * aes加密相关
     */
    public static final String AES = "AES";

    public static final String AES_CBC_PKCS5Padding = "AES/CBC/PKCS5Padding";
    /**
     * des加密相关
     */
    public static final String ENCRYPTION_MANNER = "desede";

    public final static String ECB_MODE_flag = "desede/CBC/PKCS5Padding ";
    /**
     * 默认的偏移量
     */
    public final static byte[] DEFAULT_KEY_IV = {1, 2, 3, 4, 5, 6, 7, 8};
}
