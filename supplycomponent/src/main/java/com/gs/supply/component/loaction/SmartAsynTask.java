package com.gs.supply.component.loaction;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author husky
 * create on 2019/4/17-16:00
 */
public abstract class SmartAsynTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private static ExecutorService photosThreadPool;//用于加载大图和评论的线程池

    public void executeDependSDK(Params... params) {
        if (photosThreadPool == null) {
            photosThreadPool = Executors.newSingleThreadExecutor();
        }
        if (Build.VERSION.SDK_INT < 11) {
            super.execute(params);
        } else {
            super.executeOnExecutor(photosThreadPool, params);
        }
    }

}
