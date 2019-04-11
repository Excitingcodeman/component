package com.gs.supply.component.format;

import android.content.res.Resources;
import android.os.Build;
import android.support.v4.os.ConfigurationCompat;
import android.support.v4.os.LocaleListCompat;

import java.util.Locale;

/**
 * @author husky
 * create on 2019/4/11-14:52
 */
public class LocaleHelper {


    public static Locale getLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleListCompat listCompat = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration());
            if (null != listCompat && listCompat.size() > 0) {
                return listCompat.get(0);
            }
        }
        return Locale.getDefault();

    }
}
