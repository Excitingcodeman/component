package com.gs.supply.component.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * @author husky
 * create on 2019/4/12-13:39
 */
public class NetStateReceiver extends BroadcastReceiver {
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final static String TAG = NetStateReceiver.class.getSimpleName();

    private static boolean isNetAvailable = false;

    private static String mNetType;
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();
    private static BroadcastReceiver mBroadcastReceiver;


    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = this;
        if (!TextUtils.isEmpty(intent.getAction()) && ANDROID_NET_CHANGE_ACTION.equalsIgnoreCase(intent.getAction())) {
            if (!NetUtils.isNetworkAvailable(context)) {

                isNetAvailable = false;
            } else {
                isNetAvailable = true;
                mNetType = NetUtils.getAPNType(context);
            }
            notifyObserver();
        }
    }

    /**
     * 注册广播
     *
     * @param context context
     */
    public static void registerNetworkStateReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        context.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    /**
     * 清除
     *
     * @param context
     */
    public static void checkNetworkState(Context context) {
        Intent intent = new Intent();
        context.sendBroadcast(intent);
    }

    /**
     * 反注册
     *
     * @param context
     */
    public static void unRegisterNetworkStateReceiver(Context context) {
        try {
            context.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {

        }
    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }


    public static @NetType
    String getAPNType() {
        return mNetType;
    }

    /**
     * 更改状态
     */
    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }

    /**
     * 添加网络监听
     *
     * @param observer
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    /**
     * 移除网络监听
     *
     * @param observer
     */
    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }
}
