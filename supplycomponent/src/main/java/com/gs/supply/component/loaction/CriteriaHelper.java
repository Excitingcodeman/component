package com.gs.supply.component.loaction;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import com.gs.supply.component.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author husky
 * create on 2019/4/17-09:45
 */
public class CriteriaHelper {
    public static final Criteria criteria;

    static {
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置定位精准度
        criteria.setAltitudeRequired(false);//是否要求海拔
        criteria.setBearingRequired(true);//是否要求方向
        criteria.setCostAllowed(true);//是否要求收费
        criteria.setSpeedRequired(true);//是否要求速度
        criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);//设置电池耗电要求
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);//设置方向精确度
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);//设置速度精确度
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);//设置水平方向精确度
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);//设置垂直方向精确度
    }


    String getAddress(Location location) {

        Geocoder geocoder = new Geocoder(Component.mApplicationContext);
        boolean present = Geocoder.isPresent();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);

            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                if (null != address) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        stringBuilder.append(address.getAddressLine(i)).append("\n");
                    }
                    stringBuilder.append(address.getCountryName()).append("_");//国家
                    stringBuilder.append(address.getFeatureName()).append("_");//周边地址
                    stringBuilder.append(address.getLocality()).append("_");//市
                    stringBuilder.append(address.getPostalCode()).append("_");
                    stringBuilder.append(address.getCountryCode()).append("_");//国家编码
                    stringBuilder.append(address.getAdminArea()).append("_");//省份
                    stringBuilder.append(address.getSubAdminArea()).append("_");
                    stringBuilder.append(address.getThoroughfare()).append("_");//道路
                    stringBuilder.append(address.getSubLocality()).append("_");//香洲区
                    stringBuilder.append(address.getLatitude()).append("_");//经度
                    stringBuilder.append(address.getLongitude());//维度
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
