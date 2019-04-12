package com.gs.supply.component.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.view.View;

import com.gs.supply.component.Component;

/**
 * @author husky
 * create on 2019/4/11-15:04
 * 调用之前必须初始化
 */
public class ResourcesUtils {
   


    /**
     * 获取strings.xml资源文件字符串
     *
     * @param id 资源文件id
     * @return 资源文件对应字符串
     */
    @NonNull
    public static String getString(@StringRes int id) {
        return Component.mApplicationContext.getApplicationContext().getString(id);
    }

    @NonNull
    public static String getString(@StringRes int id, Object... args) {
        return Component.mApplicationContext.getApplicationContext().getString(id, args);
    }

    /**
     * 获取strings.xml资源文件字符串数组
     *
     * @param id 资源文件id
     * @return 资源文件对应字符串数组
     */
    @NonNull
    public static String[] getStringArray(@ArrayRes int id) {
        return Component.mApplicationContext.getApplicationContext().getResources().getStringArray(id);
    }


    /**
     * 获取drawable资源文件图片
     *
     * @param id 资源文件id
     * @return 资源文件对应图片
     */
    public static Drawable getDrawable(@DrawableRes int id) {
        return Component.mApplicationContext.getApplicationContext().getResources().getDrawable(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Drawable getDrawable(@DrawableRes int id, @Nullable Resources.Theme theme) {
        return Component.mApplicationContext.getApplicationContext().getResources().getDrawable(id, theme);
    }


    /**
     * 获取colors.xml资源文件颜色
     *
     * @param id 资源文件id
     * @return 资源文件对应颜色值
     */
    public static int getColor(@ColorRes int id) {
        return Component.mApplicationContext.getApplicationContext().getResources().getColor(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static int getColor(@ColorRes int id, @Nullable Resources.Theme theme) {
        return Component.mApplicationContext.getApplicationContext().getResources().getColor(id, theme);
    }


    /**
     * 获取颜色的状态选择器
     *
     * @param id 资源文件id
     * @return 资源文件对应颜色状态
     */
    @NonNull
    public static ColorStateList getColorStateList(@ColorRes int id) {
        return Component.mApplicationContext.getApplicationContext().getResources().getColorStateList(id);
    }


    /**
     * 获取dimens资源文件中具体像素值
     * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘  返回int
     *
     * @param id 资源文件id
     * @return 资源文件对应像素值
     */
    public static int getDimensionPixelOffset(@NonNull Context context, @DimenRes int id) {
        return context.getApplicationContext().getResources().getDimensionPixelOffset(id);
    }

    /**
     * 获取dimens资源文件中具体像素值
     * 获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘   返回float
     *
     * @param id 资源文件id
     * @return 资源文件对应像素值
     */
    public static float getDimension(@DimenRes int id) {
        return Component.mApplicationContext.getApplicationContext().getResources().getDimension(id);
    }

    /**
     * 获取dimens资源文件中具体像素值
     * 则不管写的是dp还是sp还是px,都会乘以denstiy.
     *
     * @param id 资源文件id
     * @return 资源文件对应像素值
     */
    public static int getDimensionPixelSize(@DimenRes int id) {
        return Component.mApplicationContext.getApplicationContext().getResources().getDimensionPixelSize(id);
    }

    /**
     * 加载布局文件
     *
     * @param id 布局文件id
     * @return 布局view
     */
    public static View inflate(@NonNull Context context, @LayoutRes int id) {
        return View.inflate(context, id, null);
    }

}
