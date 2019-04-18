package com.gs.supply.component.loaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.gs.supply.component.Component;

/**
 * @author husky
 * create on 2019/4/17-09:32
 */
public class LocationHelper {

    private Context mContext;
    LocationManager locationManager;
    private MyLocationListener listeners[] = {
            new MyLocationListener(),
            new MyLocationListener()
    };


    public LocationHelper(Context context) {
        mContext = context;
        locationManager = (LocationManager) Component.mApplicationContext.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startRequestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //没有权限
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1f, listeners[0]);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1F, listeners[1]);
    }

    public void stopRequestLocationUpdates() {
        locationManager.removeUpdates(listeners[0]);
        locationManager.removeUpdates(listeners[1]);
    }

    public Location getCurrentLocation() {
        for (int i = 0; i < listeners.length; i++) {
            Location l = listeners[i].currentLocation();
            if (l != null) return l;
        }
        return null;
    }


    private class MyLocationListener implements LocationListener {
        Location mLastLocation;
        boolean mValid = false;

        @Override
        public void onLocationChanged(Location newLocation) {
            if (newLocation.getLatitude() == 0.0
                    && newLocation.getLongitude() == 0.0) {
                return;
            }
            mLastLocation.set(newLocation);
            mValid = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                case LocationProvider.TEMPORARILY_UNAVAILABLE: {
                    mValid = false;
                    break;
                }
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            mValid = false;
        }

        public Location currentLocation() {
            return mValid ? mLastLocation : null;
        }
    }
}
