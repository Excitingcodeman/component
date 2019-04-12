package com.gs.supply.component.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.gs.supply.component.R;
import com.gs.supply.component.update.InstallApkTools;

import java.io.File;

/**
 * @author husky
 * create on 2019/4/12-14:20
 */
public class AndroidORequestActivity extends Activity {
    /**
     * Android 8.0 打开未知权限的安装
     */
    public static final int REQUEST_O = 2000;
    /**
     * apk文件路径
     */
    private String filePath;

    private File apkFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filePath = getIntent().getStringExtra("filePath");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        filePath = getIntent().getStringExtra("filePath");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(filePath)) {
            finish();
        }
        apkFile = new File(filePath);
        if (null == apkFile) {
            finish();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean requestPackageInstalls = getPackageManager().canRequestPackageInstalls();
            if (!requestPackageInstalls) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, REQUEST_O);
            } else {
                //安装apk
                InstallApkTools.installApk(apkFile, this);
                finish();
            }
        } else {
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_O:
                //有注册权限且用户允许安装
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    InstallApkTools.installApk(apkFile, this);
                } else {
                    //将用户引导至安装未知应用界面。
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, REQUEST_O);
                }
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_O) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (getPackageManager().canRequestPackageInstalls()) {
                    //安装apk
                    InstallApkTools.installApk(apkFile, this);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.request_install_packages_tips), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
