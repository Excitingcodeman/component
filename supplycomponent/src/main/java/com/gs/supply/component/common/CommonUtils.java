package com.gs.supply.component.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

import com.gs.supply.component.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gs.supply.component.common.CommonConstant.COMMON_LABEL;

/**
 * @author husky
 * create on 2019/4/12-10:58
 */
public class CommonUtils {

    /**
     * 复制
     *
     * @param text
     */
    public static void copyToClipBoard(String text) {
        ClipboardManager cm = (ClipboardManager) Component.mApplicationContext.getSystemService(
                Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(COMMON_LABEL, text));
        Toast.makeText(Component.mApplicationContext, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 打开目录
     *
     * @param context
     * @param url     应用路径
     */
    public static void openInBrowser(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(Component.mApplicationContext, "打开失败了，没有可打开的应用", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开手机Setting界面
     *
     * @param context 上下文
     */
    public static void goAppSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
    }


}
