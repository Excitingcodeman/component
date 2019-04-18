package com.gs.component;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gs.supply.component.listener.click.OnSingleClickListener;
import com.gs.supply.component.loaction.SmartLocationManager;
import com.gs.supply.component.update.DownLoadTask;
import com.gs.supply.component.update.InstallApkTools;
import com.gs.supply.component.update.TaskProgressListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String url = "http://appdl.hicloud.com/dl/appdl/application/apk/64/647e95152dc447c288176cfd727982cc/com.sohu.tv.1809222117.apk";

    private DownLoadDialog downLoadDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.down_Tv).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                down();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            SmartLocationManager.onCreateGPS(this.getApplication());
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }

    }

    void down() {
        new DownLoadTask(url, new TaskProgressListener() {
            @Override
            public void onStart() {
                if (null == downLoadDialog) {
                    downLoadDialog = new DownLoadDialog(MainActivity.this);
                }
                if (!downLoadDialog.isShowing()) {
                    downLoadDialog.show();
                }
            }

            @Override
            public void onNext(int progress) {
                if (null != downLoadDialog) {
                    downLoadDialog.setProgressData(progress);
                }
            }

            @Override
            public void onComplete(File downFile) {
                if (null != downLoadDialog) {
                    downLoadDialog.dismiss();
                }
                InstallApkTools.installApk(downFile, MainActivity.this);
            }

            @Override
            public void onError() {
                if (null != downLoadDialog) {
                    downLoadDialog.dismiss();
                }
            }
        }).downLoad();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            boolean result = true;
            for (int item : grantResults) {
                if (item != PackageManager.PERMISSION_GRANTED) {
                    result = false;
                    break;
                }
            }
            if (result) {
                SmartLocationManager.onCreateGPS(this.getApplication());
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SmartLocationManager.stopGPS();
    }
}
