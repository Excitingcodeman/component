package com.gs.supply.component.storage;

/**
 * @author husky
 * create on 2019/4/12-09:48
 */
public class CacheItem<V> {
    public V value;
    /**
     * 过期时间
     */
    public long deleteTime;

    public CacheItem(V value, long deleteTime) {
        this.value = value;
        this.deleteTime = deleteTime;
    }
}
