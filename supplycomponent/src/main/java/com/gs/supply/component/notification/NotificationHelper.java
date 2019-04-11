package com.gs.supply.component.notification;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.gs.supply.component.R;


/**
 * @author husky
 * create on 2019/4/11-14:15
 * @Documented 通知相关的处理类
 */

public class NotificationHelper {

    /**
     * 检测是否开启了通知，如果没有开启  引导用户开启通知
     *
     * @param mActivity activity
     */
    public static void openNotification(@NonNull final Activity mActivity) {
        NotificationManagerCompat notification = NotificationManagerCompat.from(mActivity);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            new AlertDialog.Builder(mActivity)
                    .setTitle(mActivity.getString(R.string.open_notice))
                    .setMessage(mActivity.getString(R.string.notifications_tips))
                    .setNegativeButton(mActivity.getString(R.string.cancel), null)
                    .setPositiveButton(mActivity.getString(R.string.setting), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setNotification(mActivity);
                        }
                    })
                    .create()
                    .show();
        }
    }

    /**
     * 跳转到设置页面开启 通知
     *
     * @param mActivity activity
     */

    public static void setNotification(@NonNull Activity mActivity) {
        //引导开启通知
        Intent intent = new Intent();
        try {
            String packageName = mActivity.getApplicationContext().getPackageName();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int uid = mActivity.getApplicationInfo().uid;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, uid);
                } else {
                    //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                    intent.setAction(Settings.ACTION_SETTINGS);
                    intent.putExtra("app_package", packageName);
                    intent.putExtra("app_uid", uid);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Settings.ACTION_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + packageName));
            } else {
                intent.setAction(Settings.ACTION_SETTINGS);
            }
        } catch (Exception e) {
            intent.setAction(Settings.ACTION_SETTINGS);
        }
        mActivity.startActivity(intent);
    }
}
