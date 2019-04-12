package com.gs.supply.component.file;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.gs.supply.component.encryption.MD5Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author husky
 * create on 2019/4/12-11:33
 */
public class FileUtils {

    /**
     * Convert byte[] to hex string.将byte转换成int，
     * 然后利用Integer.toHexString(int)来转换成16进制字符串。
     *
     * @param src byte[] data
     * @return hex string
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取sd卡上的指定文件夹下的指定的文件，如果不存在就创建
     *
     * @param fileName 文件名
     * @param folder   文件夹名
     * @return 获取sd卡上的文件
     * @throws IOException
     */
    public static File getFile(String fileName, String folder)
            throws IOException {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File pathFile = new File(Environment.getExternalStorageDirectory()
                    + folder);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            File file = new File(pathFile, fileName);
            return file;
        }
        return null;
    }

    /**
     * @param fileName 文件名
     * @param folder   文件夹名
     * @return 是否存在
     * @throws IOException
     */
    public static Boolean checkFile(String fileName, String folder)
            throws IOException {

        File targetFile = getFile(fileName, folder);
        if (!targetFile.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context 上下文
     * @param uri     uri
     * @return 文件绝对路径
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore
                    .Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * @param dirPath 检查文件是否存在
     * @return 文件路径
     */
    public static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
     * 复制文件
     *
     * @param sourceFile 源文件
     * @param targetFile 目标文件
     */
    public static void copyFile(File sourceFile, File targetFile) {
        FileInputStream input = null;
        BufferedInputStream inbuff = null;
        FileOutputStream out = null;
        BufferedOutputStream outbuff = null;
        try {
            input = new FileInputStream(sourceFile);
            inbuff = new BufferedInputStream(input);
            out = new FileOutputStream(targetFile);
            outbuff = new BufferedOutputStream(out);
            byte[] b = new byte[1024 * 5];
            int len = 0;
            while ((len = inbuff.read(b)) != -1) {
                outbuff.write(b, 0, len);
            }
            outbuff.flush();
        } catch (Exception ex) {
        } finally {
            try {
                if (inbuff != null) {
                    inbuff.close();
                }
                if (outbuff != null) {
                    outbuff.close();
                }
                if (out != null) {
                    out.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception ex) {

            }
        }
    }

    /**
     * 保存图片到本机
     *
     * @param context            context
     * @param fileName           文件名
     * @param folder             文件夹名
     * @param file               file
     * @param saveResultCallback 保存结果callback
     */
    public static void saveImage(final Context context, final String fileName, final String folder, final File file,
                                 final SaveResultCallback saveResultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(Environment.getExternalStorageDirectory(), folder);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                String saveFileName = "";
                if (fileName.contains(".png") || fileName.contains(".gif")) {
                    String fileFormat = fileName.substring(fileName.lastIndexOf("."));
                    saveFileName = MD5Util.getMD5String(fileName) + fileFormat;
                } else {
                    saveFileName = MD5Util.getMD5String(fileName) + ".png";
                }
                //取前20位作为SaveName
                saveFileName = saveFileName.substring(20);
                File savefile = new File(appDir, saveFileName);
                try {
                    InputStream is = new FileInputStream(file);
                    FileOutputStream fos = new FileOutputStream(savefile);
                    //1M缓冲区
                    byte[] buffer = new byte[1024 * 1024];
                    int count = 0;
                    while ((count = is.read(buffer)) > 0) {
                        fos.write(buffer, 0, count);
                    }
                    fos.close();
                    is.close();
                    saveResultCallback.onSavedSuccess();
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(savefile);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }

    /**
     * 保存Bitmap到本机
     *
     * @param context            context
     * @param fileName           bitmap文件名
     * @param folder             bitmap文件夹名
     * @param bmp                bitmap
     * @param saveResultCallback 保存结果callback
     */
    public static void saveBitmap(final Context context, final String fileName, final String folder, final Bitmap bmp,
                                  final SaveResultCallback
                                          saveResultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(Environment.getExternalStorageDirectory(), folder);
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                // 设置以当前时间格式为图片名称
                String saveFileName = MD5Util.getMD5String(fileName) + ".png";
                //取前20位作为SaveName
                saveFileName = saveFileName.substring(20);
                File file = new File(appDir, saveFileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess();
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }
                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }

    public interface SaveResultCallback {
        void onSavedSuccess();

        void onSavedFailed();
    }


    /**
     * 判断下载目录是否存在
     *
     * @param saveDir saveDir
     * @return
     * @throws IOException
     */
    public static String isExistDir(String saveDir, Context context) throws IOException {
        File downloadFile;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //sd卡存在
            downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        } else {
            downloadFile = new File(context.getFilesDir().getAbsolutePath(), saveDir);
        }
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        } else {
            deleteFile(downloadFile);
        }
        return downloadFile.getAbsolutePath();
    }

    /**
     * 从下载链接中获取文件的名称
     *
     * @param url url
     * @return 文件的名称
     */
    @NonNull
    public static String getFileRealName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }


    /**
     * 删除文件夹下的文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }

        } else if (file.exists()) {

        }
    }

    /**
     * 创建文件
     *
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return 文件
     */
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建文件夹
     *
     * @param filePath 文件路径地址
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.d("error:", e + "");
        }
    }
}
