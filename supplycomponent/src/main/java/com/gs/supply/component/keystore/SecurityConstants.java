package com.gs.supply.component.keystore;

/**
 * @author husky
 * create on 2018/11/27-15:49
 *
 * keystore 的常量
 */
public final class SecurityConstants {

    public static final String KEYSTORE_PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";

    public static final String TYPE_RSA = "RSA";

    public static final String TYPE_DSA = "DSA";

    public static final String TYPE_BKS = "BKS";

    public static final String SIGNATURE_SHA256withRSA = "SHA256withRSA";

    public static final String SIGNATURE_SHA512withRSA = "SHA512withRSA";

    public static final String RSA_PADDING="RSA/ECB/PKCS1Padding";

    public static final String ENCODE="UTF-8";
    /**
     * 自己给你的别名，方便在keystore中查找秘钥
     */
    public static final String SAMPLE_ALIAS = "HUSKY";
}
