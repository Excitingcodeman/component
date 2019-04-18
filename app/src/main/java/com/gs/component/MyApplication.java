package com.gs.component;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.gs.supply.component.Component;

/**
 * @author husky
 * create on 2019/4/12-15:34
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Component.init(this);
    }
}
