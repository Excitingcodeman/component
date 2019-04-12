package com.gs.supply.component.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gs.supply.component.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author husky
 * create on 2019/4/12-10:36
 */
public class SharedPreferencesUtil {
    private static SharedPreferences sharedPreferences;
    public static final Gson gson = new Gson();
    private static class Holder {
        private static final SharedPreferencesUtil instance = new SharedPreferencesUtil();
    }

    private SharedPreferencesUtil() {
        if (sharedPreferences == null) {
            sharedPreferences = Component.mApplicationContext.getSharedPreferences(StorageConfig.sharedPreferencesName, Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferencesUtil getInstance() {
        return Holder.instance;
    }


    /**
     * 存入Boolean类型的值
     *
     * @param key   key值
     * @param value 需要存的值
     */
    public void putBoolean(@NonNull String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取 Boolean类型的值
     *
     * @param key      key值
     * @param defValue 默认的值
     * @return 默认值或者此节点读取到的结果
     */
    public boolean getBoolean(@NonNull String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * 写入String类型的值
     *
     * @param key   key值
     * @param value 需要存的值
     */
    public void putString(@NonNull String key, String value) {

        sharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * 获取String类型的值
     *
     * @param key      key值
     * @param defValue 默认的值
     * @return 默认值或者此节点读取到的结果
     */
    public String getString(@NonNull String key, String defValue) {

        return sharedPreferences.getString(key, defValue);
    }

    /**
     * 写入int类型的数据
     *
     * @param key   key值
     * @param value 需要存的值
     */
    public void putInt(@NonNull String key, int value) {

        sharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * 获取int类型的数据
     *
     * @param key      key值
     * @param defValue 默认的值
     * @return 默认值或者此节点读取到的结果
     */
    public int getInt(@NonNull String key, int defValue) {

        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * 写入long类型的数据
     *
     * @param key   key值
     * @param value 需要存的值
     */
    public void putLong(@NonNull String key, long value) {

        sharedPreferences.edit().putLong(key, value).apply();
    }

    /**
     * 获取long类型的数据
     *
     * @param key      key值
     * @param defValue 默认的值
     * @return 默认值或者此节点读取到的结果
     */
    public long getLong(@NonNull String key, long defValue) {

        return sharedPreferences.getLong(key, defValue);
    }

    /**
     * 写入float类型的数据
     *
     * @param key   key值
     * @param value 需要存的值
     */
    public void putFloat(@NonNull String key, float value) {

        sharedPreferences.edit().putFloat(key, value).apply();
    }

    /**
     * 获取float类型的数据
     *
     * @param key      key值
     * @param defValue 默认的值
     * @return 默认值或者此节点读取到的结果
     */
    public float getFloat(@NonNull String key, float defValue) {

        return sharedPreferences.getFloat(key, defValue);
    }

    /**
     * 移除指定的节点
     *
     * @param key 指定的节点
     */
    public void remove(@NonNull String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * 保存List对象到sp中
     *
     * @param key      指定的节点
     * @param dataList 数据集合
     * @param <T>      对象的类型
     */
    public <T> void setDataList(@NonNull String key, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0) {
            return;
        }
        String toJson = gson.toJson(dataList);
        putString(key, toJson);
    }

    /**
     * 获取List对象
     *
     * @param key    指定的节点
     * @param tClass 对象类型
     * @param <T>    对象类型
     * @return 返回指定的对象的集合或者空的集合
     */
    public <T> List<T> getDataList(@NonNull String key, Class<T> tClass) {
        List<T> dataList = new ArrayList<T>();
        String string = getString(key, null);
        if (null == string) {
            return dataList;
        }
        try {
            JsonArray array = new JsonParser().parse(string).getAsJsonArray();
            for (final JsonElement elem : array) {
                dataList.add(gson.fromJson(elem, tClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * 存入指定对象到sp
     *
     * @param key    指定的节点
     * @param tClass 对象类型
     * @param <T>    对象类型
     */
    public <T> void saveObject(@NonNull String key, T tClass) {
        if (null == tClass) {
            return;
        }
        String toJson = gson.toJson(tClass);
        putString(key, toJson);
    }

    /**
     * 获取指定对象
     *
     * @param key    指定的节点
     * @param tClass 对象类型
     * @param <T>    对象类型
     * @return 返回获取的对象或者null
     */
    public <T> T getObject(@NonNull String key, Class<T> tClass) {
        String string = getString(key, null);
        if (null != string) {
            try {
                return gson.fromJson(string, tClass);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
