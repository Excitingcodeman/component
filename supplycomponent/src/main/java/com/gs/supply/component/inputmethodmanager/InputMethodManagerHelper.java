package com.gs.supply.component.inputmethodmanager;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @author husky
 * create on 2019/4/12-11:20
 */
public class InputMethodManagerHelper {
    /**
     * 隐藏键盘
     *
     * @param view     当前的视图
     * @param activity 依附的activity
     */

    public static void hideInputMethod(@NonNull View view, @NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 弹出键盘
     *
     * @param view     需要弹出键盘的视图
     * @param activity 依附的activity
     */
    public static void showKeyBoard(@NonNull View view, @NonNull Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != imm) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }
}
