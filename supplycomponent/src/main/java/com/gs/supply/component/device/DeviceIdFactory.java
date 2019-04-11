package com.gs.supply.component.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author husky
 * create on 2019/4/11-14:35
 */
public class DeviceIdFactory {
    /**
     * 保存文件的路径
     */
    private static final String CACHE_IMAGE_DIR = "array/cache/devices";
    /**
     * 保存的文件 采用隐藏文件的形式进行保存
     */
    private static final String DEVICES_FILE_NAME = ".DEVICES";

    /**
     * 获取设备id
     *
     * @param context context
     * @return 设备id
     */
    public static String getDeviceId(Context context) {
        //读取保存的在sd卡中的唯一标识符
        String deviceId = readDeviceID(context);
        //用于生成最终的唯一标识符
        StringBuffer s = new StringBuffer();
        //判断是否已经生成过,
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        try {
            //获取IMES(也就是常说的DeviceId)
            deviceId = getIMIEStatus(context);
            s.append(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //获取设备的MACAddress地址 去掉中间相隔的冒号
            deviceId = getLocalMac().replace(":", "");
            s.append(deviceId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果以上搜没有获取相应的则自己生成相应的UUID作为相应设备唯一标识符
        if (s == null || s.length() <= 0) {
            UUID uuid = UUID.randomUUID();
            deviceId = uuid.toString().replace("-", "");
            s.append(deviceId);
        }
        //为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串
        String md5 = getMD5(s.toString(), false);
        if (s.length() > 0) {
            //持久化操作, 进行保存到SD卡中
            saveDeviceId(md5, context);
        }
        return md5;

    }

    /**
     * 获取设备的DeviceId(IMES) 这里需要相应的权限<br/>
     * 需要 READ_PHONE_STATE 权限
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    private static String getIMIEStatus(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    /**
     * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
     *
     * @param context
     * @return
     */
    public static String readDeviceID(Context context) {
        File file = getDevicesDir(context);
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            Reader in = new BufferedReader(isr);
            int i;
            while ((i = in.read()) > -1) {
                buffer.append((char) i);
            }
            in.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     * 02:00:00:00:00:02   这个值是没有意义的
     *
     * @return
     */
    private static String getLocalMac() {
        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "";
            }
            byte[] address = networkInterface.getHardwareAddress();


            for (byte b : address) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "";
        }
        return macAddress;
    }


    /**
     * 保存内容到 文件
     *
     * @param src
     * @param context
     */
    public static void saveDeviceId(String src, Context context) {
        File file = getDevicesDir(context);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            Writer out = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
            out.write(src);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统一处理设备唯一标识  保存的文件的地址
     *
     * @param context
     * @return
     */
    public static File getDevicesDir(Context context) {
        File mCropFile;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cropDirectory = new File(Environment.getExternalStorageDirectory(), CACHE_IMAGE_DIR);
            if (!cropDirectory.exists()) {
                cropDirectory.mkdirs();
            }
            mCropFile = new File(cropDirectory, DEVICES_FILE_NAME);
        } else {
            File cropDirectory = new File(context.getFilesDir(), CACHE_IMAGE_DIR);
            if (!cropDirectory.exists()) {
                cropDirectory.mkdirs();
            }
            mCropFile = new File(cropDirectory, DEVICES_FILE_NAME);
        }
        return mCropFile;
    }

    /**
     * 对挺特定的 内容进行 md5 加密
     *
     * @param message   加密明文
     * @param upperCase 加密以后的字符串是是大写还是小写  true 大写  false 小写
     * @return
     */
    public static String getMD5(String message, boolean upperCase) {
        String md5str = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] input = message.getBytes();

            byte[] buff = md.digest(input);

            md5str = bytesToHex(buff, upperCase);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }


    public static String bytesToHex(byte[] bytes, boolean upperCase) {
        StringBuffer md5str = new StringBuffer();
        int digital;
        for (int i = 0; i < bytes.length; i++) {
            digital = bytes[i];

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        if (upperCase) {
            return md5str.toString().toUpperCase();
        }
        return md5str.toString().toLowerCase();
    }

}
