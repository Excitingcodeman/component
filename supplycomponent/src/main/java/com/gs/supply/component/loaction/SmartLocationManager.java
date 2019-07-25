//package com.gs.supply.component.loaction;
//
//import android.annotation.SuppressLint;
//import android.app.Application;
//import android.content.Intent;
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationServices;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
//import static com.gs.supply.component.loaction.LocationTools.createFastLocationRequest;
//import static com.gs.supply.component.loaction.LocationTools.createLowPowerLocationRequest;
//import static com.gs.supply.component.loaction.LocationTools.getCellInfo;
//import static com.gs.supply.component.loaction.LocationTools.getWifiInfo;
//import static com.gs.supply.component.loaction.LocationTools.recordLocation;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.FAST_UPDATE_INTERVAL;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.GOOGLE_API_KEY;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.MAX_deviation;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.STATUS_LOW_POWER;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.STATUS_NOT_CONNECT;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.STATUS_NOT_TRACK;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.STATUS_TRYING_FIRST;
//import static com.gs.supply.component.loaction.SmartLocationManagerConfig.isGeoApp;
//
///**
// * @author husky
// * create on 2019/4/17-13:41
// */
//public class SmartLocationManager implements
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {
//    public static final String TAG = SmartLocationManager.class.getSimpleName();
//    /**
//     * 防止内存泄漏，不使用activity的引用
//     */
//    private Application context;
//
//    private GoogleApiClient mGoogleApiClient;
//    /**
//     * 单例模式
//     */
//    public static SmartLocationManager manager;
//    @SmartLocationManagerConfig.STATUS
//    public String currentStatus = STATUS_NOT_CONNECT;
//
//    private Timer locationTimer;
//    /**
//     * 发送给谷歌API的WiFi和基站数据
//     */
//    public String dataJson;
//
//    private SmartLocationManager() {
//
//    }
//
//    public static SmartLocationManager getInstance() {
//        return manager;
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (currentStatus != STATUS_NOT_CONNECT) return;//有些手机会多次连接成功
//        currentStatus = STATUS_TRYING_FIRST;
//        if (!getCurrentLocation()) {
//            //得到当前gps并记录
//            //如果没有成功拿到当前经纬度，那么就通过实时位置监听去不断的拿，直到拿到为止
//            //如果没有拿到经纬度，就一直监听
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createFastLocationRequest(), this);//创建位置监听
//            new TowerAndWiFiTask().executeDependSDK();
//        } else if (isGeoApp) {
//            //@对位置准确度要求高，希望一直跟踪位置的APP
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createFastLocationRequest(), this);//创建位置监听
//            new TowerAndWiFiTask().executeDependSDK();
//
//        } else {
//            //获取了最后一次硬件记录的经纬度，不进行追踪
//            //在获取最近一次硬件经纬度成功之后要不要开启追踪模式，应该视项目的具体需求而定，如果对定位的准确度要求不是特别高的APP,那么拿到最近一次的定位就不用追踪了，减少耗电
//            //对位置要求不高，要求省电的APP
//            currentStatus = STATUS_NOT_TRACK;
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onLocationChanged(Location location) {
//        if (currentStatus == STATUS_LOW_POWER) {
//            Log.d(TAG, "现在是低电力的定位策略");
//        }
//        Log.d(TAG, "位置改变了" + location);
//        if (location == null) {
//            return;
//        }
//        if (location.getAccuracy() < MAX_deviation) {
//            recordLocation(location.getLatitude(), location.getLongitude(), location.getAccuracy());
//        } else {
//            //精度如果太小就放弃
//            Log.d(TAG, "精确度太低，准备放弃最新的位置");
//        }
//        if (location.getAccuracy() > 50
//                || currentStatus != STATUS_TRYING_FIRST
//                || isGeoApp) {
//            //如果现在是高电量模式，那么就停止当前监听，采用低电量监听
//            //地理位置敏感APP并不会进入低电量模式
//            //成功获取到之后就降低频率来省电
//            return;
//        }
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        Log.d(TAG, "准备开启省电策略");
//        currentStatus = STATUS_LOW_POWER;
//        //创建位置监听
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createLowPowerLocationRequest(), this);
//
//
//    }
//
//    public static void onCreateGPS(@NonNull final Application context) {
//        if (manager != null && manager.mGoogleApiClient != null) {
//            return;
//        }
//        manager = new SmartLocationManager();
//        manager.context = context;
//        manager.mGoogleApiClient = new GoogleApiClient.Builder(context)
//                .addConnectionCallbacks(manager)
//                .addOnConnectionFailedListener(manager)
//                .addApi(LocationServices.API)
//                .build();
//        manager.mGoogleApiClient.connect();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (manager == null
//                        || manager.context == null
//                        || manager.currentStatus != STATUS_NOT_CONNECT) {
//                    //如果连接GPS硬件到9s后还没成功
//                    return;
//                }
//                context.startService(new Intent(context, SmartLocationService.class));
//                if (manager.locationTimer == null) {
//                    manager.locationTimer = new Timer();
//                }
//                try {
//                    //10s获取一次
//                    manager.locationTimer.scheduleAtFixedRate(new LocationTask(), 0, FAST_UPDATE_INTERVAL);
//                } catch (Exception e) {
//
//                }
//                new SmartAsynTask<Void, Void, String>() {
//
//                    @Override
//                    protected String doInBackground(Void... voids) {
//                        GeoLocationAPI geoLocationAPI = null;
//                        try {
//                            geoLocationAPI = getCellInfo(manager.context);//得到基站信息,通过基站进行定位
//                        } catch (Exception e) {
//                            Log.d(TAG, "获取附近基站信息出现异常", e);
//                        }
//                        if (geoLocationAPI == null) {
//                            Log.d(TAG, "获取基站信息失败");
//                            return "{}";
//                        }
//                        getWifiInfo(manager.context, geoLocationAPI);
//                        String json = geoLocationAPI.toJson();
//                        return json;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//                        if (manager != null
//                                && manager.context != null) {
//                            manager.sendJsonByPost(s, "https://www.googleapis.com/geolocation/v1/geolocate?key=" + GOOGLE_API_KEY);
//
//                        }
//                    }
//                }.executeDependSDK();
//
//            }
//        }, 9000);
//    }
//
//    public static class LocationTask extends TimerTask {
//
//        @Override
//        public void run() {
//            if (manager == null || !SmartLocationService.isDestory) {
//                return;
//            }
//            manager.context.startService(new Intent(manager.context, SmartLocationService.class));
//        }
//    }
//
//    /**
//     * 进入某些页面，重新刷GPS
//     *
//     * @param context
//     */
//    public static void restartGPS(Application context) {
//        stopGPS();//先停止当前的GPS
//        onCreateGPS(context);//重启GPS
//    }
//
//    /**
//     * 停止gps服务，用来省电
//     */
//    public static void stopGPS() {
//        if (manager == null) return;
//        pauseGPS();
//        manager.mGoogleApiClient = null;
//        manager = null;
//    }
//
//    /**
//     * 当app被放到后台时，暂停GPS
//     */
//    public static void pauseGPS() {
//        if (manager == null
//                || manager.mGoogleApiClient == null
//                || manager.currentStatus == STATUS_NOT_CONNECT
//                || manager.currentStatus == STATUS_NOT_TRACK) {
//            return;
//        }
//        try {
//            LocationServices.FusedLocationApi.removeLocationUpdates(manager.mGoogleApiClient, manager);
//            manager.currentStatus = STATUS_NOT_CONNECT;
//            if (manager.mGoogleApiClient.isConnected() || manager.mGoogleApiClient.isConnecting())
//                manager.mGoogleApiClient.disconnect();
//            manager.mGoogleApiClient = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    class TowerAndWiFiTask extends SmartAsynTask<Void, Void, String> {
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            GeoLocationAPI geoLocationAPI = null;
//            try {
//                geoLocationAPI = getCellInfo(context);//得到基站信息,通过基站进行定位
//            } catch (Exception e) {
//                Log.d(TAG, "获取附近基站信息出现异常", e);
//            }
//            if (geoLocationAPI == null) {
//                Log.d(TAG, "获取基站信息失败");
//                return "{}";
//            }
//            getWifiInfo(context, geoLocationAPI);
//            //这里使用gson.toJson()会被混淆，推荐使用手动拼json
//            String json = geoLocationAPI.toJson();
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d(TAG, "准备发给google的json是" + s);
//            manager.sendJsonByPost(s, "https://www.googleapis.com/geolocation/v1/geolocate?key=" + GOOGLE_API_KEY);
//
//        }
//    }
//
//
//    /**
//     * 拿到最近一次的硬件经纬度记录,只用精确度足够高的时候才会采用这种定位
//     *
//     * @return
//     */
//    @SuppressLint("MissingPermission")
//    public boolean getCurrentLocation() {
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        Log.d(TAG, "得到last Location的gps是==" + mLastLocation);
//        if (mLastLocation == null) {
//            //在少数情况下这里有可能是null
//            return false;
//        }
//        double latitude = mLastLocation.getLatitude();//纬度
//        double longitude = mLastLocation.getLongitude();//经度
//        double altitude = mLastLocation.getAltitude();//海拔
//        float last_accuracy = mLastLocation.getAccuracy();//精度
//        Log.d(TAG, "last Location的精度是" + last_accuracy);
//        String provider = mLastLocation.getProvider();//传感器
//        float bearing = mLastLocation.getBearing();
//        float speed = mLastLocation.getSpeed();//速度
//        Log.d(TAG, "获取last location成功，纬度==" + latitude + "  经度" + longitude + "  海拔" + altitude + "   传感器" + provider + "   速度" + speed + "精确度" + last_accuracy);
//        if (last_accuracy < MAX_deviation) {
//            recordLocation(latitude, longitude, last_accuracy);
//        } else {
//            Log.d(TAG, "精确度太低，放弃last Location");
//        }
//        return last_accuracy < MAX_deviation;
//    }
//
//
//    /**
//     * 使用httpclient发送一个post的json请求
//     *
//     * @param url
//     * @return
//     */
//    public void sendJsonByPost(String json, String url) {
//        this.dataJson = json;
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
//        final Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (null != response && response.isSuccessful()) {
//                /*
//                谷歌返回的json如下
//                {
//                   "location": {
//                    "lat": 1.3553794,
//                    "lng": 103.86774439999999
//                   },
//                   "accuracy": 16432.0
//                  }
//                 */
//                    String result = response.body().string();
//                    Log.d(TAG, "成功" + result);
//                    JSONObject returnJson = null;
//                    try {
//                        returnJson = new JSONObject(result);
//                        JSONObject location = returnJson.getJSONObject("location");
//                        if (location == null) {
//                            Log.d(TAG, "条件不足，无法确定位置");
//                            return;
//                        }
//                        double latitude = location.optDouble("lat");
//                        double longitute = location.optDouble("lng");
//                        double google_accuracy = returnJson.optDouble("accuracy");
//                        Log.d(TAG, "谷歌返回的经纬度是" + latitude + "  :  " + longitute + "  精度是" + google_accuracy);
//                        //当没有从GPS获取经纬度成功，或者GPS的获取经纬度精确度不高，则使用基站和wifi的结果
//                        recordLocation(latitude, longitute, (float) google_accuracy);
//                    } catch (JSONException e) {
//                        Log.d(TAG, "条件不足，无法确定位置2", e);
//                    }
//                } else {
//                    if (null != response) {
//                        Log.d(TAG, response.toString());
//                    }
//                }
//            }
//        });
//
//    }
//
//
//}
