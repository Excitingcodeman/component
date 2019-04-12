package com.gs.supply.component.listener.edit;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author husky
 * create on 2019/4/12-11:08
 */
public class EditFocusChangeListener implements View.OnFocusChangeListener {

    private ImageView ivDelete;

    public EditFocusChangeListener(ImageView imageView) {
        this.ivDelete = imageView;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v instanceof EditText) {
                if (null != v) {
                    if (!TextUtils.isEmpty(((EditText) v).getText())) {
                        if (null != ivDelete) {
                            ivDelete.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } else {
            if (null != ivDelete) {
                ivDelete.setVisibility(View.GONE);
            }

        }
    }
}
