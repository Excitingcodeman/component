//package com.gs.supply.component.loaction;
//
//import android.support.annotation.StringDef;
//
///**
// * @author husky
// * create on 2019/4/17-13:43
// */
//public class SmartLocationManagerConfig {
//
//    /**
//     * google map 的key
//     */
//    public static final String GOOGLE_API_KEY = "AIzaSyCm-FEbn5dpo2UWxhTohO02uz9niGNx7zI";
//    /**
//     * 中国境内的坐标是否自动转换成火星坐标系
//     */
//    public static final boolean autoChina = true;
//    /**
//     * 当前APP对GPS要求高不高，普通APP只需要了解大概位置的话应填false，对位置要求严格的APP比如地图的话填true
//     */
//    public static final boolean isGeoApp = true;
//
//    /**
//     * 首次获取最小精确度限制
//     */
//    public static final int MAX_deviation = isGeoApp ? 60 : 100;
//    /**
//     * 10 sec 平均更新时间,同时也是没有获取成功的刷新间隔，是耗电量的重要参数，普通APP 20s,敏感型10s
//     */
//    public static final int FAST_UPDATE_INTERVAL = isGeoApp ? 10000 : 20000;
//    /**
//     * 5 sec 最短更新时间
//     */
//    public static final int FATEST_INTERVAL = 5000;
//    /**
//     * 10 meters 为最小侦听距离,如果当前APP位置敏感，那就填1m
//     */
//    public static final int FAST_DISPLACEMENT = isGeoApp ? 1 : 10;
//
//    /**
//     * 60 sec 平均更新时间
//     */
//    public static final int SLOW_UPDATE_INTERVAL = 60000;
//    /**
//     * 30 sec 最短更新时间
//     */
//    public static final int SLOW_INTERVAL = 30000;
//    /**
//     * 500 meters 为最小侦听距离
//     */
//    public static final int SLOW_DISPLACEMENT = 500;
//    /**
//     * 没有连接相关硬件成功
//     */
//    public static final String STATUS_NOT_CONNECT = "NOT_CONNECT";
//    /**
//     * 第一次获取地理位置，此时gps指示图标闪烁，耗电量大
//     */
//    public static final String STATUS_TRYING_FIRST = "TRYING_FIRST";
//    /**
//     * 开启app拿到精确度的GPS之后，开启省电模式
//     */
//    public static final String STATUS_LOW_POWER = "LOW_POWER";
//
//    /**
//     * 当前没有开启跟踪模式
//     */
//    public static final String STATUS_NOT_TRACK = "NOT_TRACK";
//
//    @StringDef({STATUS_NOT_CONNECT, STATUS_TRYING_FIRST, STATUS_LOW_POWER, STATUS_NOT_TRACK})
//    public @interface STATUS {
//    }
//
//    public static double pi = 3.1415926535897932384626;
//    public static double a = 6378245.0;
//    public static double ee = 0.00669342162296594323;
//}
