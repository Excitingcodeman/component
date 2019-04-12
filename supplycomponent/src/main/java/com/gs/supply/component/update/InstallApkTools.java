package com.gs.supply.component.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.gs.supply.component.activity.AndroidORequestActivity;

import java.io.File;

/**
 * @author husky
 * create on 2019/4/12-14:27
 */
public class InstallApkTools {
    /**
     * 安装apk  FileProvider名称为  应用包名+".FileProvider";
     *
     * @param apkFile
     * @param context
     */
    public static void installApk(@NonNull File apkFile, @NonNull Context context) {
        if (null == apkFile) {
            return;
        }
        int sdkVersion = context.getApplicationInfo().targetSdkVersion;

        if (sdkVersion >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            checkAndroidO(context, apkFile);
        } else {
            doInstall(apkFile, context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void checkAndroidO(@NonNull Context context, @NonNull File apkFile) {
        boolean requestPackageInstalls = context.getPackageManager().canRequestPackageInstalls();
        if (!requestPackageInstalls) {
            //注意这个是8.0新API
            Intent intent = new Intent(context, AndroidORequestActivity.class);
            intent.putExtra("filePath", apkFile.getPath());
            context.startActivity(intent);
        } else {
            doInstall(apkFile, context);
        }
    }

    private static void doInstall(@NonNull File apkFile, @NonNull Context context) {
        if (null == apkFile) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, context.getPackageName() + ".FileProvider", apkFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * @param apkFile
     * @param context
     * @param fileProvider 自定义的FileProvider
     */
    public static void installApk(@NonNull File apkFile,@NonNull Context context, @NonNull String fileProvider) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean requestPackageInstalls = context.getPackageManager().canRequestPackageInstalls();
            if (!requestPackageInstalls) {
                //注意这个是8.0新API
                Intent intent = new Intent(context, AndroidORequestActivity.class);
                intent.putExtra("filePath", apkFile.getPath());
                context.startActivity(intent);

            } else {
                doInstall(apkFile, context, fileProvider);
            }
        } else {
            doInstall(apkFile, context, fileProvider);
        }


    }

    private static void doInstall(@NonNull File apkFile,@NonNull Context context,@NonNull String fileProvider) {
        if (null == apkFile) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (TextUtils.isEmpty(fileProvider)){
                return;
            }
            data = FileProvider.getUriForFile(context, fileProvider, apkFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
