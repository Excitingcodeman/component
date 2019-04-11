package com.gs.supply.component.encryption;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 *
 * @author husky
 * create on 2019/4/11
 */
public class MD5Util {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static MessageDigest messageDigest = null;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String getMD5String(String str) {
        return getMD5String(str.getBytes());
    }


    public static String getMD5String(byte[] bytes) {
        messageDigest.update(bytes);
        return bytesToHex(messageDigest.digest());
    }


    public static String bytesToHex(byte bytes[]) {
        return bytesToHex(bytes, 0, bytes.length);
    }


    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }

        return sb.toString();
    }


    public static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
    }

    /**
     * 获取文件的md5值
     *
     * @param file 指定的文件
     * @return
     */
    public static String getFileMd5(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, len);
            }
            byte[] bytes = messageDigest.digest();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 对字符进行多次md5加密
     *
     * @param data  需要加密的数据
     * @param times 次数
     * @return
     */
    public static String getMD5String(String data, int times) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String md5 = getMD5String(data);
        for (int i = 0; i < times - 1; i++) {
            md5 = getMD5String(md5);
        }
        return getMD5String(md5);
    }

    public static String getMD5String(String data, String slat) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        byte[] bytes = messageDigest.digest((data + slat).getBytes());
        return bytesToHex(bytes);

    }
}
