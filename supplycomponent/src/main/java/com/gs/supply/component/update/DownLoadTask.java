package com.gs.supply.component.update;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gs.supply.component.Component;
import com.gs.supply.component.file.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author husky
 * create on 2019/4/12-14:48
 * 下载的任务
 */
public class DownLoadTask {

    public static final BigDecimal hundred = new BigDecimal(100);
    /**
     * 下载开始
     */
    public static final int DOWN_START = 1000;
    /**
     * 下载中
     */
    public static final int DOWNING = 1001;

    /**
     * 下载完成
     */
    public static final int DOWN_COMPLETE = 1002;
    /**
     * 下载失败
     */
    public static final int DOWN_ERROR = 1003;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWN_START:
                    taskProgressListener.onStart();
                    break;
                case DOWNING:
                    taskProgressListener.onNext(msg.arg1);
                    break;
                case DOWN_ERROR:
                    taskProgressListener.onError();
                    break;
                case DOWN_COMPLETE:
                    taskProgressListener.onComplete(downLoadFile);
                    break;
            }
        }
    };

    /**
     * 下载的地址
     */
    private String downLoadUrl;
    /**
     * 保存下载文件的路径地址
     */
    private String downLoadFilePath;
    /**
     * 保存文件的文件
     */
    private File downLoadFile;
    /**
     * 下载的监听器
     */
    private TaskProgressListener taskProgressListener;

    public DownLoadTask(@NonNull String downLoadUrl, @NonNull TaskProgressListener listener) {
        this.downLoadUrl = downLoadUrl;
        this.taskProgressListener = listener;
    }

    public DownLoadTask(@NonNull String downLoadUrl, @NonNull String downLoadFilePath, @NonNull TaskProgressListener listener) {
        this.downLoadUrl = downLoadUrl;
        this.downLoadFilePath = downLoadFilePath;
        this.taskProgressListener = listener;
    }

    public DownLoadTask(@NonNull String downLoadUrl, File downLoadFile, @NonNull TaskProgressListener listener) {
        this.downLoadUrl = downLoadUrl;
        this.downLoadFile = downLoadFile;
        this.taskProgressListener = listener;
    }


    public void downLoad() {
        if (TextUtils.isEmpty(downLoadUrl)) {
            handler.sendEmptyMessage(DOWN_ERROR);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (downLoadFile == null) {
                    if (TextUtils.isEmpty(downLoadFilePath)) {
                        downLoadFile= FileUtils.makeFilePath(Component.mApplicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath(),
                                downLoadUrl.substring(downLoadUrl.lastIndexOf("/") + 1));
                    } else {
                        downLoadFile = new File(downLoadFilePath);
                    }
                }
                if (downLoadFile != null && downLoadFile.exists()) {
                    downLoadFile.delete();
                }
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    URL url = new URL(downLoadUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(false);
                    urlConnection.setConnectTimeout(20 * 1000);
                    urlConnection.setReadTimeout(20 * 1000);
                    urlConnection.setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("Charset", "UTF-8");
                    urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    urlConnection.connect();
                    //文件总大小
                    long bytetotal = urlConnection.getContentLength();
                    //下载的大小
                    long bytesum = 0;
                    int byteread = 0;
                    in = urlConnection.getInputStream();
                    out = new FileOutputStream(downLoadFile);
                    byte[] buffer = new byte[1024];
                    int oldProgress = 0;
                    handler.sendEmptyMessage(DOWN_START);
                    while ((byteread = in.read(buffer)) != -1) {
                        bytesum += byteread;
                        int newProgress = new BigDecimal(bytesum).divide(new BigDecimal(bytetotal), 2, RoundingMode.HALF_UP).multiply(hundred).intValue();
                        if (newProgress > oldProgress) {
                            oldProgress = newProgress;
                        }
                        Message message = Message.obtain();
                        message.arg1 = oldProgress;
                        message.what = DOWNING;
                        handler.sendMessage(message);
                        out.write(buffer, 0, byteread);

                    }
                    out.flush();
                    out.close();
                    in.close();
                    handler.sendEmptyMessage(DOWN_COMPLETE);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(DOWN_ERROR);
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }
}
