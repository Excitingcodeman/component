package com.gs.component;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * create on 2018-08-29   14:01
 *
 * @author husky
 * 下载的对话框
 */
public class DownLoadDialog extends Dialog {

    private Context context;
    private ProgressBar progressBar;
    private TextView progressBarTv, tipTextView;


    public DownLoadDialog(@NonNull Context context) {
        this(context, R.style.TransparentDialog);
    }

    public DownLoadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }


    private void init() {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(attributes);
        setContentView(R.layout.dialog_download_layout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        progressBarTv = findViewById(R.id.progressBar_tv);
        tipTextView = findViewById(R.id.tipTextView);


    }


    public void setProgressData(int progressNumber) {
        progressBar.setProgress(progressNumber);
        progressBarTv.setText(context.getString(R.string.have_complete) + progressNumber + context.getString(R.string.percent_sign));
    }

    public void setDownLoadCompleteText() {
        tipTextView.setText(context.getString(R.string.download_apk_success));
    }

}
