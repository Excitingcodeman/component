package com.gs.supply.component.listener.edit;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * @author husky
 * create on 2019/4/12-11:11
 */
public class EditTextChangeListener implements TextWatcher {

    private ImageView ivDelete;
    private EditText editText;

    public EditTextChangeListener(@NonNull ImageView ivDelete) {
        this.ivDelete = ivDelete;
    }

    public EditTextChangeListener(ImageView ivDelete, EditText editText) {
        this.ivDelete = ivDelete;
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence str, int start, int before, int count) {
        String contents = str.toString();
        int length = 0;
        if (!TextUtils.isEmpty(contents)) {
            length = contents.length();
        }
        if (null != editText && editText.isEnabled()) {
            if (length > 0) {
                ivDelete.setVisibility(View.VISIBLE);
            } else {
                ivDelete.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
