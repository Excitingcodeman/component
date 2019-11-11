package com.gs.supply.component.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.gs.supply.component.Component;
import com.gs.supply.component.resources.VersionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * @author husky
 * create on 2019/4/12-11:52
 */
public class DeviceUtils {


    /**
     * 屏幕分辨率
     *
     * @return
     */
    public static String getResolution() {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((WindowManager) Component.mApplicationContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(point);
        } else {
            return "";
        }
        return point.toString() == null ? "" : point.toString();
    }

    /**
     * 32位随机数
     *
     * @return
     */
    public static String getAppRandom() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "") == null ? "" : uuid.toString().replace("-", "");
    }


    /**
     * mac 地址
     *
     * @return
     */
    public static String getMac() {

        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            if (null != addr) {
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }

    /**
     * 国家
     *
     * @return
     */

    public static String getCountry() {
        TelephonyManager telephonyManager = (TelephonyManager) Component.mApplicationContext.getSystemService(Context.TELEPHONY_SERVICE);
        String country = "";
        String countryID = telephonyManager.getSimCountryIso().toUpperCase();
        int getRes2 = Component.mApplicationContext.getResources().getIdentifier("CountryCodes", "array", Component.mApplicationContext.getPackageName());
        String[] rl = Component.mApplicationContext.getResources().getStringArray(getRes2);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(countryID.trim())) {
                country = g[0];
                break;
            }
        }
        return country == null ? "" : country;
    }

    /**
     * 语言
     *
     * @return
     */
    public static String getLanguage() {
        return Locale.getDefault().getLanguage() == null ? "" : Locale.getDefault().getLanguage();
    }


    /**
     * 时区
     *
     * @return
     */
    public static String getTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        return timeZone.getDisplayName(false, TimeZone.SHORT).replaceAll("\\+", "") == null ? "" : timeZone.getDisplayName(false, TimeZone.SHORT).replaceAll("\\+", "");
    }

    /**
     * 操作系统
     *
     * @return
     */
    public static String getOsName() {
        return Build.ID == null ? "" : Build.ID;
    }

    /**
     * 操作系统版本
     *
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE == null ? "" : Build.VERSION.RELEASE;
    }

    /**
     * 当前时间
     *
     * @return
     */
    public static String getGmtTime() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 屏幕尺寸
     *
     * @return
     */
    public static String getSize() {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((WindowManager) Component.mApplicationContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealSize(point);
        } else {
            return "";
        }
        DisplayMetrics dm = Component.mApplicationContext.getResources().getDisplayMetrics();
        double x = Math.pow(point.x / dm.xdpi, 2);
        double y = Math.pow(point.y / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches == 0 ? "" : screenInches + "";
    }

    /**
     * 手机品牌
     *
     * @return
     */
    public static String getBrand() {
        return Build.BRAND == null ? "" : Build.BRAND;
    }

    /**
     * 手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL == null ? "" : Build.MODEL;
    }

    /**
     * wifi
     *
     * @return
     */
    public static String getWifi() {
        WifiInfo wifiInfo = ((WifiManager) Component.mApplicationContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        return wifiInfo.getSSID().replaceAll("\"", "") == null ? "" : wifiInfo.getSSID().replaceAll("\"", "");
    }


    /**
     * wifiMac
     *
     * @return
     */
    public static String getWifiMac() {
        return getMac();
    }


    /**
     * 手机序列号
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getSerialNumber() {
        return Settings.Secure.getString(Component.mApplicationContext.getContentResolver(), Settings.Secure.ANDROID_ID) == null
                ? ""
                : Settings.Secure.getString(Component.mApplicationContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 设备唯一标识
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceNum() {
        return Build.SERIAL == null ? "" : Build.SERIAL;
    }

    /**
     * 移动国家网码
     *
     * @return
     */
    public static String getMobileCountryCode() {
        int mcc = -1;
        try {
            String operator = ((TelephonyManager) Component.mApplicationContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
            mcc = Integer.parseInt(operator.substring(0, 3));
        } catch (Exception e) {
            return "" + mcc;
        }
        return "" + mcc;
    }

    /**
     * 移动网码
     *
     * @return
     */
    public static String getMobileNetworkCode() {

        int mnc = -1;
        try {
            String operator = ((TelephonyManager) Component.mApplicationContext.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperator();
            mnc = Integer.parseInt(operator.substring(3));
        } catch (Exception e) {
            return "" + mnc;
        }

        return "" + mnc;
    }


    /**
     * 电池电量
     *
     * @return
     */
    public static String getBatteryLevel() {
        Intent batteryInfoIntent = Component.mApplicationContext
                .registerReceiver(null,
                        new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return batteryInfoIntent.getIntExtra("level", 0) == 0
                ? "" :
                batteryInfoIntent.getIntExtra("level", 0) + "%";
    }


    /**
     * 应用版本号
     *
     * @return
     */
    public static String getAppVersion() {
        return VersionUtils.getVersionName() == null
                ? ""
                : VersionUtils.getVersionName();

    }

    public static PackageInfo getPackageInfo() {
        PackageInfo pi = null;

        try {
            PackageManager pm = Component.mApplicationContext.getPackageManager();
            pi = pm.getPackageInfo(Component.mApplicationContext.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }


    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getIPAddress() {
        NetworkInfo info = ((ConnectivityManager) Component.mApplicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                //当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            Log.e("ipv6Test", "拿到的ipv6地址是 = " + (inetAddress.getHostAddress() == null ? "" : inetAddress.getHostAddress()));
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress() == null ? "" : inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //当前使用无线网络
                WifiManager wifiManager = (WifiManager) Component.mApplicationContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (null != wifiManager) {
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    //得到IPV4地址
                    return intIP2StringIP(wifiInfo.getIpAddress());
                }
            }
        } else {
            //当前无网络连接,请在设置中打开网络
            return "";
        }
        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */

    public static String getBaseband_Ver() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
// System.out.println(">>>>>>><<<<<<<" +(String)result);
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CORE-VER
     * 内核版本
     * return String
     */

    public static String getLinuxCore_Ver() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


// get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);


        String result = "";
        String line;
        // get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return kernelVersion;
    }

    /**
     * INNER-VER
     * 内部版本
     * return String
     */

    public static String getInner_Ver() {
        String ver = "";

        if (android.os.Build.DISPLAY.contains(android.os.Build.VERSION.INCREMENTAL)) {
            ver = android.os.Build.DISPLAY;
        } else {
            ver = android.os.Build.VERSION.INCREMENTAL;
        }
        return ver;

    }


    public static void setSystemLight(Activity activity, int light) {
        ContentResolver cr = activity.getContentResolver();
        Settings.System.putInt(cr, "screen_brightness", light);
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        float flevel = light;
        attributes.screenBrightness = flevel / 255;
        float x = attributes.screenBrightness;
        activity.getWindow().setAttributes(attributes);
    }


    public static boolean IsAirModeOn(Context context) {
        return (Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1 ? true : false);
    }

}
