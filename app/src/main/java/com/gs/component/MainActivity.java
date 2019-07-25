package com.gs.component;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gs.supply.component.device.DeviceInfo;
import com.gs.supply.component.encryption.MD5Util;
import com.gs.supply.component.listener.click.OnSingleClickListener;
import com.gs.supply.component.update.DownLoadTask;
import com.gs.supply.component.update.InstallApkTools;
import com.gs.supply.component.update.TaskProgressListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static final String url = "http://appdl.hicloud.com/dl/appdl/application/apk/64/647e95152dc447c288176cfd727982cc/com.sohu.tv.1809222117.apk";

    private DownLoadDialog downLoadDialog;

    private Context mContext;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        content = findViewById(R.id.content);
    }

    @Override
    protected void onStart() {
        super.onStart();

        findViewById(R.id.down_Tv).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
//                down();
                getCertificateMD5();
                content.setText(new DeviceInfo(mContext).getDeviceInfo());
            }
        });

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
//        ) {
//            SmartLocationManager.onCreateGPS(this.getApplication());
//        } else {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
//                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
//        }

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


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1000) {
//            boolean result = true;
//            for (int item : grantResults) {
//                if (item != PackageManager.PERMISSION_GRANTED) {
//                    result = false;
//                    break;
//                }
//            }
//            if (result) {
//                SmartLocationManager.onCreateGPS(this.getApplication());
//            }
//        }
//
//    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        SmartLocationManager.stopGPS();
//    }


    private void getCertificateMD5() {
        try {
            String packageName = getApplicationContext().getPackageName();
            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNING_CERTIFICATES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            byte[] cert = sign.toByteArray();
            String md5 = MD5Util.getMD5String(cert);

            Log.e("TAG", "md5 = " + md5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
