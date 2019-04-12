package com.gs.supply.component.storage;

import android.text.TextUtils;
import android.util.LruCache;

/**
 * @author husky
 * create on 2019/4/12-09:49
 */
public class LruCacheUtils<T> {

    /**
     * 默认30分钟
     */
    public static long defaultDuring = 30 * 60 * 1000;
    /**
     * 12分钟
     */
    public static long TWELEVE = 12 * 60 * 1000;
    /**
     * 一天
     */
    public static long ONE_DAY = 24 * 60 * 60 * 1000;

    private final LruCache<String, CacheItem<T>> mLruCache;

    protected LruCacheUtils() {
        //获取最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置缓存的大小
        int cacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, CacheItem<T>>(cacheSize);
    }

    public void put(String key, T value, long expiredTime) {
        if (null == value) {
            return;
        }
        if (expiredTime <= 0) {
            expiredTime = defaultDuring;
        }
        long time = System.currentTimeMillis();
        mLruCache.put(key, new CacheItem(value, (time + expiredTime)));
    }


    public void put(String key, T value) {
        put(key, value, defaultDuring);
    }


    public T get(String key) {
        CacheItem<T> cacheItem = mLruCache.get(key);
        if (null != cacheItem) {
            if (cacheItem.deleteTime - System.currentTimeMillis() > 0) {
                return cacheItem.value;
            } else {
                mLruCache.remove(key);
                return null;
            }
        }
        return null;
    }


    public T getValue(String key) {
        return getValue(key, defaultDuring);
    }

    public T getValue(String key, long expiredTime) {
        CacheItem<T> cacheItem = mLruCache.get(key);
        if (null != cacheItem) {
            if (cacheItem.deleteTime - System.currentTimeMillis() > 0) {
                put(key, cacheItem.value, expiredTime);
                return cacheItem.value;
            } else {
                mLruCache.remove(key);
                return null;
            }
        }
        return null;
    }

    /**
     * 移除指定的缓存
     *
     * @param key 存取的关键字
     */
    public void remove(String key) {
        if (!TextUtils.isEmpty(key)) {
            mLruCache.remove(key);
        }
    }
}
