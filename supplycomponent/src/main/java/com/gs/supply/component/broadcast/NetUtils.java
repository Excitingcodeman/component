package com.gs.supply.component.broadcast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresPermission;

import java.util.Locale;

/**
 * @author husky
 * create on 2019/4/12-13:41
 */
public class NetUtils {


    /**
     * 网络是否可用
     *
     * @param context 上下文
     * @return true可用 false不可用
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != mgr) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = mgr.getAllNetworks();
                NetworkInfo networkInfo;
                if (null != networks) {
                    for (Network mNetwork : networks) {
                        networkInfo = mgr.getNetworkInfo(mNetwork);
                        if (null != networkInfo
                                && null != networkInfo.getState()
                                && networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                            return true;
                        }
                    }
                }
            } else {
                NetworkInfo[] info = mgr.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo networkInfo : info) {
                        if (null != networkInfo
                                && null != networkInfo.getState()
                                && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 网络是否连接
     *
     * @param context 上下文
     * @return true 连接 false 没有连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否是wifi连接
     *
     * @param context 上下文
     * @return true是WiFi  false不是WiFi
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 是否是手机流量
     *
     * @param context 上下文
     * @return true是手机流量 false不是手机流量
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取网络连接类型
     *
     * @param context 上下文
     * @return types defined by {@link ConnectivityManager}
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 获取NetType类型
     *
     * @param context
     * @return types defined by {@link NetType}
     */
    public static @NetType
    String getAPNType(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return NetTypeConfig.NONE_TYPE;
        }
        int nType = networkInfo.getType();

        if (nType == ConnectivityManager.TYPE_MOBILE) {
            if (null == networkInfo.getExtraInfo()) return NetTypeConfig.NONE_TYPE;
            if (networkInfo.getExtraInfo().toLowerCase(Locale.getDefault()).equals("cmnet")) {
                return NetTypeConfig.CMNET_TYPE;
            } else {
                return NetTypeConfig.CMWAP_TYPE;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            return NetTypeConfig.WIFI_TYPE;
        }
        return NetTypeConfig.NONE_TYPE;
    }
}
