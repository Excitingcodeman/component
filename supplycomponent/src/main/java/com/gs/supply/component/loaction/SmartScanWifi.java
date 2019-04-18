package com.gs.supply.component.loaction;

import android.net.wifi.ScanResult;

import static com.gs.supply.component.loaction.LocationTools.getChannelByFrequency;

/**
 * @author husky
 * create on 2019/4/17-16:25
 */
public class SmartScanWifi implements Comparable<SmartScanWifi> {
    public final int dBm;
    public final String ssid;
    public final String mac;
    public short channel;
    public SmartScanWifi(ScanResult scanresult) {
        dBm = scanresult.level;
        ssid = scanresult.SSID;
        mac = scanresult.BSSID;//BSSID就是传说中的mac
        channel = getChannelByFrequency(scanresult.frequency);
    }
    public SmartScanWifi(String s, int i, String s1, String imac) {
        dBm = i;
        ssid = s1;
        mac = imac;
    }

    /**
     * 根据信号强度进行排序
     * @param wifiinfo
     * @return
     */
    public int compareTo(SmartScanWifi wifiinfo) {
        int i = wifiinfo.dBm;
        int j = dBm;
        return i - j;
    }

    /**
     * 为了防止添加wifi的列表重复，复写equals方法
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        boolean flag = false;
        if (obj == this) {
            flag = true;
            return flag;
        } else {
            if (obj instanceof SmartScanWifi) {
                SmartScanWifi wifiinfo = (SmartScanWifi) obj;
                int i = wifiinfo.dBm;
                int j = dBm;
                if (i == j) {
                    String s = wifiinfo.mac;
                    String s1 = this.mac;
                    if (s.equals(s1)) {
                        flag = true;
                        return flag;
                    }
                }
                flag = false;
            } else {
                flag = false;
            }
        }
        return flag;
    }
    public int hashCode() {
        int i = dBm;
        int j = mac.hashCode();
        return i ^ j;
    }
}
