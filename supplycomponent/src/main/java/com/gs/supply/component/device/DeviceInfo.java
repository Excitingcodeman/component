package com.gs.supply.component.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.google.gson.Gson;
import com.gs.supply.component.Component;
import com.gs.supply.component.broadcast.NetUtils;
import com.gs.supply.component.resources.VersionUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gs.supply.component.device.DeviceUtils.IsAirModeOn;
import static com.gs.supply.component.device.DeviceUtils.getAppVersion;
import static com.gs.supply.component.device.DeviceUtils.getBaseband_Ver;
import static com.gs.supply.component.device.DeviceUtils.getBatteryLevel;
import static com.gs.supply.component.device.DeviceUtils.getBrand;
import static com.gs.supply.component.device.DeviceUtils.getCountry;
import static com.gs.supply.component.device.DeviceUtils.getGmtTime;
import static com.gs.supply.component.device.DeviceUtils.getIPAddress;
import static com.gs.supply.component.device.DeviceUtils.getInner_Ver;
import static com.gs.supply.component.device.DeviceUtils.getLanguage;
import static com.gs.supply.component.device.DeviceUtils.getMac;
import static com.gs.supply.component.device.DeviceUtils.getMobileCountryCode;
import static com.gs.supply.component.device.DeviceUtils.getMobileNetworkCode;
import static com.gs.supply.component.device.DeviceUtils.getOsName;
import static com.gs.supply.component.device.DeviceUtils.getOsVersion;
import static com.gs.supply.component.device.DeviceUtils.getPhoneModel;
import static com.gs.supply.component.device.DeviceUtils.getResolution;
import static com.gs.supply.component.device.DeviceUtils.getSerialNumber;
import static com.gs.supply.component.device.DeviceUtils.getSize;
import static com.gs.supply.component.device.DeviceUtils.getTimeZone;
import static com.gs.supply.component.device.DeviceUtils.getWifi;
import static com.gs.supply.component.device.DeviceUtils.getWifiMac;

/**
 * @author husky
 * create on 2019/4/12-11:51
 */
public class DeviceInfo {

    private static final String TAG = DeviceInfo.class.getSimpleName();

    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private Context mContext;

    public DeviceInfo(Context context) {
        mContext = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * IMSI
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public String getImsi() {
        String imsi;
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                return "";
            }
            imsi = telephonyManager.getSubscriberId();
        } catch (Exception e) {
            imsi = "";
        }
        return imsi == null ? "" : imsi;
    }


    /**
     * Imei
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public String getImei() {
        String imei;
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                return "";
            }
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            imei = "";
        }
        return imei == null ? "" : imei;
    }

    @SuppressLint("MissingPermission")
    public String getIMEI() {
        String imei = "";
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_PHONE_STATE}, 1000);
                return "";
            }
            TelephonyManager tm = (TelephonyManager)
                    mContext.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei = tm.getImei();
            } else {
                imei = tm.getDeviceId();
            }
        } catch (Exception e) {
            imei = "";
        }
        Log.d(TAG, "imei-->" + imei);
        return imei;
    }


    /**
     * 获取经纬度
     * <p>
     * 默认从优先选择从网络获取
     *
     * @param lat 1：经度 2：纬度
     * @return
     */
    public String getLocation(String lat) {
        String provider;
        //获取当前可用的位置控制器
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER;

        } else if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER;
        } else {
            return "";
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return "";
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            if ("1".equals(lat)) {
                // 经度
                return location.getLatitude() == 0 ? "" : location.getLatitude() + "";
            } else {
                // 纬度
                return location.getLongitude() == 0 ? "" : location.getLatitude() + "";
            }
        } else {
            return "";
        }
    }


    /**
     * 默认从网络返回
     *
     * @return
     */
    public String getLocationType() {
        return "NET";
    }


    /**
     * 是否越狱
     *
     * @return
     */
    public boolean isRoot() {
        if (isRootSystem1() || isRootSystem2()) {
            //TODO 可加其他判断 如是否装了权限管理的apk，大多数root 权限 申请需要app配合，也有不需要的，这个需要改su源码。因为管理su权限的app太多，无法列举所有的app，特别是国外的，暂时不做判断是否有root权限管理app
            //多数只要su可执行就是root成功了，但是成功后用户如果删掉了权限管理的app，就会造成第三方app无法申请root权限，此时是用户删root权限管理app造成的。
            //市场上常用的的权限管理app的包名   com.qihoo.permmgr  com.noshufou.android.su  eu.chainfire.supersu   com.kingroot.kinguser  com.kingouser.com  com.koushikdutta.superuser
            //com.dianxinos.superuser  com.lbe.security.shuame com.geohot.towelroot 。。。。。。
            return true;
        } else {
            return false;
        }
    }

    private boolean isRootSystem1() {
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (int i = 0; i < kSuSearchPaths.length; i++) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private boolean isRootSystem2() {
        List<String> pros = getPath();
        File f = null;
        try {
            for (int i = 0; i < pros.size(); i++) {
                f = new File(pros.get(i), "su");
                System.out.println("f.getAbsolutePath():" + f.getAbsolutePath());
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    private List<String> getPath() {
        return Arrays.asList(System.getenv("PATH").split(":"));
    }

    /**
     * 基站信息
     *
     * @return
     */
    public String getBaseStnInfo() {

        int mcc = -1, mnc = -1, lac = -1, cellId = -1;
        // 返回值MCC + MNC
        try {
            String operator = telephonyManager.getNetworkOperator();
            mcc = Integer.parseInt(operator.substring(0, 3));
            mnc = Integer.parseInt(operator.substring(3));

            // 中国移动和中国联通获取LAC、CID的方式
            if (ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return " MCC = " + mcc + " MNC = " + mnc + " LAC = " + lac + " CID = " + cellId;
            }
            GsmCellLocation location = (GsmCellLocation) telephonyManager.getCellLocation();
            lac = location.getLac();
            cellId = location.getCid();
        } catch (Exception e) {
            return " MCC = " + mcc + " MNC = " + mnc + " LAC = " + lac + " CID = " + cellId;
        }

        return " MCC = " + mcc + " MNC = " + mnc + " LAC = " + lac + " CID = " + cellId;
    }

    /**
     * 基站位置信息
     *
     * @return
     */
    public String getBaseStnLocation() {
        return "";
    }


    /**
     * 是否触屏
     *
     * @return
     */
    public boolean isSupportTouchscreen() {
        return true;
    }


    /**
     * 应用标志bundle identifier
     *
     * @return
     */
    public String getBundleId() {
        return Build.FINGERPRINT;
    }


    /**
     * 屏幕颜色位数
     *
     * @return
     */
    public String getColorBits() {
        return "";
    }

    /**
     * 手机内应用个数
     *
     * @return
     */
    public String getApplicationsNumber() {
        PackageManager pm = mContext.getPackageManager();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        return applicationInfos == null
                ? ""
                : applicationInfos.size() + "";
    }

    /**
     * 手机内照片个数
     *
     * @return
     */
    @SuppressLint("Recycle")
    public String getPhotosNumber() {
        int i = 0;
        try {
            Cursor cursor = mContext.getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    //获取图片的名称
                    i++;
                }
            }
            return i + "";
        } catch (Exception e) {
            return "";
        }

    }


    public static boolean gpsIsOpen() {
        LocationManager alm = (LocationManager) Component.mApplicationContext.getSystemService(Context.LOCATION_SERVICE);
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        }
        return false;
    }

    /**
     * 获取设备信息
     *
     * @return
     */
    public String getDeviceInfo() {
//        StringBuffer strDeviceInfo = new StringBuffer();
//        strDeviceInfo.append("{");
//        strDeviceInfo.append("\"appRandom\":" + "\"" + DeviceUtils.getAppRandom() + "\",");
//        strDeviceInfo.append("\"imsi\":" + "\"" + getImsi() + "\",");
//        strDeviceInfo.append("\"mac\":" + "\"" + getMac() + "\",");
//        strDeviceInfo.append("\"imei\":" + "\"" + getImei() + "\",");
//        strDeviceInfo.append("\"country\":" + "\"" + getCountry() + "\",");
//        strDeviceInfo.append("\"language\":" + "\"" + getLanguage() + "\",");
//        strDeviceInfo.append("\"timeZone\":" + "\"" + getTimeZone() + "\",");
//        strDeviceInfo.append("\"locationLat\":" + "\"" + getLocation("1") + "\",");
//        strDeviceInfo.append("\"locationLon\":" + "\"" + getLocation("2") + "\",");
//        strDeviceInfo.append("\"locationType\":" + "\"" + getLocationType() + "\",");
//        strDeviceInfo.append("\"osName\":" + "\"" + getOsName() + "\",");
//        strDeviceInfo.append("\"osVersion\":" + "\"" + getOsVersion() + "\",");
//        strDeviceInfo.append("\"isRoot\":" + "\"" + isRoot() + "\",");
//        strDeviceInfo.append("\"gmtTime\":" + "\"" + getGmtTime() + "\",");
//        strDeviceInfo.append("\"size\":" + "\"" + getSize() + "\",");
//        strDeviceInfo.append("\"resolution\":" + "\"" + getResolution() + "\",");
//        strDeviceInfo.append("\"brand\":" + "\"" + getBrand() + "\",");
//        strDeviceInfo.append("\"phoneModel\":" + "\"" + getPhoneModel() + "\",");
//        strDeviceInfo.append("\"wifi\":" + "\"" + getWifi() + "\",");
//        strDeviceInfo.append("\"wifiMac\":" + "\"" + getWifiMac() + "\",");
//        strDeviceInfo.append("\"baseStnInfo\":" + "\"" + getBaseStnInfo() + "\",");
//        strDeviceInfo.append("\"baseStnLocation\":" + "\"" + getBaseStnLocation() + "\",");
//        strDeviceInfo.append("\"serialNumber\":" + "\"" + getSerialNumber() + "\",");
//        strDeviceInfo.append("\"deviceNum\":" + "\"" + SharedPreferencesUtil.getInstance().getString("device_Id", "") + "\",");
//        strDeviceInfo.append("\"supportTouchscreen\":" + "\"" + isSupportTouchscreen() + "\",");
//        strDeviceInfo.append("\"mobileCountryCode\":" + "\"" + getMobileCountryCode() + "\",");
//        strDeviceInfo.append("\"mobileNetworkCode\":" + "\"" + getMobileNetworkCode() + "\",");
//        strDeviceInfo.append("\"batteryLevel\":" + "\"" + getBatteryLevel() + "\",");
//        strDeviceInfo.append("\"appVersion\":" + "\"" + getAppVersion() + "\",");
//        strDeviceInfo.append("\"bundleId\":" + "\"" + getBundleId() + "\",");
//        strDeviceInfo.append("\"colorBits\":" + "\"" + getColorBits() + "\",");
//        strDeviceInfo.append("\"photosNumber\":" + "\"" + getPhotosNumber() + "\",");
//        strDeviceInfo.append("\"applicationsNumber\":" + "\"" + getApplicationsNumber() + "\"");
//        strDeviceInfo.append("}");
        Map<String, String> map = new HashMap<>();
        map.put("appRandom", DeviceUtils.getAppRandom());
        map.put("imsi", getImsi());
        map.put("mac", getMac());
        map.put("imei", getImei());
        map.put("IMEI", getIMEI());
        map.put("country", getCountry());
        map.put("language", getLanguage());
        map.put("timeZone", getTimeZone());
        map.put("locationLat", getLocation("1"));
        map.put("locationLon", getLocation("2"));
        map.put("locationType", getLocationType());
        map.put("osName", getOsName());
        map.put("osVersion", getOsVersion());
        map.put("isRoot", String.valueOf(isRoot()));
        map.put("gmtTime", getGmtTime());
        map.put("size", getSize());
        map.put("resolution", getResolution());
        map.put("brand", getBrand());
        map.put("phoneModel", getPhoneModel());
        map.put("wifi", getWifi());
        map.put("wifiMac", getWifiMac());
        map.put("baseStnInfo", getBaseStnInfo());
        map.put("baseStnLocation", getBaseStnLocation());
        map.put("serialNumber", getSerialNumber());
        map.put("deviceNum", getSerialNumber());
        map.put("supportTouchscreen", String.valueOf(isSupportTouchscreen()));
        map.put("mobileCountryCode", getMobileCountryCode());
        map.put("mobileNetworkCode", getMobileNetworkCode());
        map.put("batteryLevel", getBatteryLevel());
        map.put("appVersion", getAppVersion());
        map.put("bundleId", getBundleId());
        map.put("colorBits", getColorBits());
        map.put("photosNumber", getPhotosNumber());
        map.put("applicationsNumber", getApplicationsNumber());
        map.put("deviceModel", Build.DEVICE);
        map.put("platform", Build.HARDWARE);
        map.put("networkType", NetUtils.getAPNType(mContext));
        map.put("dataNetworkIp", getIPAddress());
        map.put("cpuMaxFrequency", CpuManager.getMaxCpuFreq());
        map.put("memory", RoomSizeManager.getRamMemory()[0] + "");
        map.put("totalSpace", RoomSizeManager.getRomMemory()[0] + "");
        map.put("freeSpace", RoomSizeManager.getRomMemory()[1] + "");
        map.put("gpsSwitch", String.valueOf(gpsIsOpen()));
        map.put("basebandVersion", getBaseband_Ver());
        map.put("productInternalCode", getInner_Ver());
        map.put("romTags", Build.TAGS);
        map.put("firmwareNum", android.os.Build.VERSION.CODENAME + android.os.Build.VERSION.RELEASE);
        map.put("signMd5", SignUtil.getCertificateMD5());
        map.put("appName", VersionUtils.getPackageInfo().packageName);
        map.put("appVersion", VersionUtils.getVersionName());
        map.put("deviceType", Build.TYPE);
        map.put("como", Build.BOARD + Build.BOOTLOADER);
        map.put("isOffline", String.valueOf(IsAirModeOn(Component.mApplicationContext)));
        map.put("bootTimeLength", String.valueOf(System.currentTimeMillis() - SystemClock.elapsedRealtime()));
        map.put("bootTime", String.valueOf(SystemClock.elapsedRealtime()));
        String deviceInfo = new Gson().toJson(map);
        Log.d(TAG, deviceInfo);
        return deviceInfo;
    }

}
