//package com.gs.supply.component.loaction;
//
///**
// * @author husky
// * create on 2019/4/17-14:26
// */
//public class MyLocation {
//    /**
//     * 纬度
//     */
//    public double latitude;
//    /**
//     * 经度
//     */
//    public double longitude;
//    /**
//     * 最后更新时间，用于做精确度择优
//     */
//    public long updateTime;
//    /**
//     * 精度
//     */
//    public float accuracy;
//    private static MyLocation myLocation;
//
//    MyLocation() {
//    }
//
//    public MyLocation(double latitude, double longitude) {
//        this.latitude = latitude;
//        this.longitude = longitude;
//    }
//
//    public static MyLocation getInstance() {
//        if (myLocation == null) {
//            myLocation = new MyLocation();
//        }
//        return myLocation;
//    }
//}
