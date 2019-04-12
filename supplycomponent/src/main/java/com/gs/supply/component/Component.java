package com.gs.supply.component;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * @author husky
 * create on 2019/4/12-09:01
 * <p>
 * 初始化获取全局上下文
 */
public class Component {

    public static Application mApplicationContext;

    public static void init(@NonNull Application application) {
        mApplicationContext = application;
    }


}
