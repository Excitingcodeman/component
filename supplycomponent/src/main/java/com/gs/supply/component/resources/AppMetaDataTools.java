package com.gs.supply.component.resources;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

/**
 * @author husky
 * create on 2019/4/11-16:19
 */
public class AppMetaDataTools {

    /**
     * 获取 AndroidManifest里的meta-data原始数据
     *
     * @param mContext     上下文
     * @param key          meta-data 节点的key
     * @param defaultValue 给定一个默认值
     * @return AndroidManifest里的meta-data原始数据
     */
    public static String getAppMetaData(@NonNull Context mContext, String key, String defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(resultData)) {
            resultData = defaultValue;
        }
        return resultData;
    }
}
