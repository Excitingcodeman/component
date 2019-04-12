package com.gs.supply.component.resources;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.gs.supply.component.Component;

/**
 * @author husky
 * create on 2019/4/12-09:33
 */
public class VersionUtils {


    /**
     * @return 版本名
     */
    public static String getVersionName() {
        if (null == getPackageInfo()) {
            return "";
        }
        return getPackageInfo().versionName;
    }

    /**
     * @return 版本号
     */
    public static int getVersionCode() {
        if (null == getPackageInfo()) {
            return -1;
        }
        return getPackageInfo().versionCode;
    }

    /**
     * 获取包信息
     *
     * @return PackageInfo
     */
    public static PackageInfo getPackageInfo() {
        try {
            PackageManager pm = Component.mApplicationContext.getPackageManager();
            return pm.getPackageInfo(Component.mApplicationContext.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
