package com.gs.supply.component.storage;

/**
 * @author husky
 * create on 2019/4/12-10:17
 */
public class LruCacheUtilsFactory {

    private static class StringHolder {
        private static final LruCacheUtils<String> instanceString = new LruCacheUtils<>();
    }

    public static LruCacheUtils getinstanceString() {
        return StringHolder.instanceString;
    }

}
