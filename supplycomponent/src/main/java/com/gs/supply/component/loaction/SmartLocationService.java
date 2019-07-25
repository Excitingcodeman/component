//package com.gs.supply.component.loaction;
//
//import android.Manifest;
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.util.Log;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.gs.supply.component.loaction.LocationTools.isWrongPosition;
//import static com.gs.supply.component.loaction.LocationTools.recordLocation;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.FAST_UPDATE_INTERVAL;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.STATUS_NOT_TRACK;
//
///**
// * @author husky
// * create on 2019/4/17-14:03
// */
//public class SmartLocationService extends IntentService {
//
//    public static final String TAG = SmartLocationService.class.getSimpleName();
//
//    private ArrayList<String> PROVIDER_ARRAY;
//
//    public static boolean isDestory;
//
//    private String locationProvider;
//
//    private LocationManager locationManager;
//
//    private LocationListener gpsLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.d(TAG, "GPS -> onProviderEnabled");
//            getBestLocationProvider();
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.d(TAG, "GPS -> onProviderDisabled");
//            getBestLocationProvider();
//        }
//    };
//    private LocationListener networkLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.d(TAG, "Network -> onProviderEnabled");
//            getBestLocationProvider();
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.d(TAG, "Network -> onProviderDisabled");
//            getBestLocationProvider();
//        }
//    };
//    private LocationListener passiveLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            Log.d(TAG, "Passive -> onProviderEnabled");
//            getBestLocationProvider();
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            Log.d(TAG, "Passive -> onProviderDisabled");
//            getBestLocationProvider();
//        }
//    };
//
//    public SmartLocationService() {
//        super("GPS");
//        PROVIDER_ARRAY = new ArrayList<>();
//        PROVIDER_ARRAY.add(LocationManager.GPS_PROVIDER);
//        PROVIDER_ARRAY.add(LocationManager.NETWORK_PROVIDER);
//        PROVIDER_ARRAY.add(LocationManager.PASSIVE_PROVIDER);
//        isDestory = false;
//    }
//
//
//    @Override
//    protected void onHandleIntent(@NonNull Intent intent) {
//        locationProvider = null;
//        locationManager = null;
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager == null) {
//            return;
//        }
//        List<String> allProviders = locationManager.getAllProviders();
//        if (allProviders != null) {
//            for (String provider : allProviders) {
//                Log.d(TAG, "AllProviders  ->  provider => " + provider);
//                if ((provider != null) && (PROVIDER_ARRAY.contains(provider))) {
//                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: 权限申请
//                        return;
//                    }
//                    if (LocationManager.GPS_PROVIDER.equals(provider)) {
//                        Log.d(TAG, "AllProviders  ->  provider => add gpsLocationListener");
//                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, FAST_UPDATE_INTERVAL, 0, gpsLocationListener);
//                    } else if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
//                        Log.d(TAG, "AllProviders  ->  provider => add networkLocationListener");
//                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, FAST_UPDATE_INTERVAL, 0, networkLocationListener);
//                    } else if (LocationManager.PASSIVE_PROVIDER.equals(provider)) {
//                        Log.d(TAG, "AllProviders  ->  provider => add passiveLocationListener");
//                        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, FAST_UPDATE_INTERVAL, 0, passiveLocationListener);
//                    }
//                }
//            }
//        }
//
//        while (!isDestory) {
//            getBestLocationProvider();
//            Log.d(TAG, "locationProvider => " + locationProvider);
//            updateLocation();
//            Log.d(TAG, "是否要停下" + isDestory);
//            if (isDestory) return;//防止GPS指示灯长时间闪烁，造成耗电的问题
//            if ((locationProvider != null) && (PROVIDER_ARRAY.contains(locationProvider))) {//如果成功获取到了位置
//                try {
//                    if (!isWrongPosition(MyLocation.getInstance().latitude, MyLocation.getInstance().longitude)) {//当前获取到的经纬度
//                        isDestory = true;
//                    } else {
//                        Thread.sleep(FAST_UPDATE_INTERVAL);//如果获取的是不正确的经纬度
//                    }
//                } catch (InterruptedException ex) {
//                    Log.d(TAG, " onHandleIntent ", ex);
//                }
//            } else {//如果没有成功获取到位置
//                try {
//                    Thread.sleep(FAST_UPDATE_INTERVAL);
//                } catch (Exception ex) {
//                    Log.d(TAG, " onHandleIntent ", ex);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        isDestory = true;
//        if ((locationManager != null) && (gpsLocationListener != null)) {
//            locationManager.removeUpdates(gpsLocationListener);
//        }
//
//        if ((locationManager != null) && (networkLocationListener != null)) {
//            locationManager.removeUpdates(networkLocationListener);
//        }
//
//        if ((locationManager != null) && (passiveLocationListener != null)) {
//            locationManager.removeUpdates(passiveLocationListener);
//        }
//    }
//
//    private synchronized void getBestLocationProvider() {
//        if (locationManager == null) {
//            locationProvider = null;
//            return;
//        }
//        List<String> providers = locationManager.getAllProviders();
//        if (providers == null || providers.size() <= 0) {
//            locationProvider = null;
//            return;
//        }
//
//        String bestProvider = null;
//        Location bestLocation = null;
//        for (String provider : providers) {
//            if ((provider != null) && (PROVIDER_ARRAY.contains(provider))) {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: 权限申请
//                    return;
//                }
//                Location location = locationManager.getLastKnownLocation(provider);
//                if (location == null) {
//                    continue;
//                }
//
//                if (bestLocation == null) {
//                    bestLocation = location;
//                    bestProvider = provider;
//                    continue;
//                }
//                if (Float.valueOf(location.getAccuracy()).compareTo(bestLocation.getAccuracy()) >= 0) {
//                    bestLocation = location;
//                    bestProvider = provider;
//                }
//            }
//        }
//
//        locationProvider = bestProvider;
//    }
//
//
//    private void updateLocation() {
//        Log.d(TAG, " ----> updateLocation <---- locationProvider => " + locationProvider);
//        if ((locationProvider != null) && (!locationProvider.equals("")) && (PROVIDER_ARRAY.contains(locationProvider))) {
//            try {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: 权限申请
//                    return;
//                }
//                Location currentLocation = locationManager.getLastKnownLocation(locationProvider);
//                Log.d(TAG, "通过旧版service取到GPS，经度" + currentLocation.getLongitude() + " 纬度" + currentLocation.getLatitude() + "  来源" + locationProvider);
//                Log.d(TAG, "locationProvider -> " + locationProvider + "  currentLocation -> " + currentLocation);
//                if (currentLocation != null) {
//                    final double newLatitude = currentLocation.getLatitude();
//                    final double newLongitude = currentLocation.getLongitude();
//                    final float accuracy = currentLocation.getAccuracy();
//                    Log.d(TAG, "locationProvider -> " + newLatitude + " : " + newLongitude + "精确度" + accuracy);
//                    if (!isWrongPosition(newLatitude, newLongitude)) {
//                        recordLocation(newLatitude, newLongitude, accuracy);
//                    }
//                    if (SmartLocationManager.manager != null) {
//                        SmartLocationManager.manager.currentStatus = STATUS_NOT_TRACK;
//                    }
//                    //精确的GPS记录完毕可以停掉这个子线程了
//                    if (!isWrongPosition(newLatitude, newLongitude)) {
//                        isDestory = true;
//                    }
//                }
//            } catch (Exception ex) {
//                Log.d(TAG, " updateLocation ", ex);
//            }
//        }
//    }
//}
