package com.gs.supply.component.broadcast;

/**
 * @author husky
 * create on 2019/4/12-12:00
 */
public interface NetChangeObserver {
    /**
     * 网络连接回调
     *
     * @param type type为网络类型
     */
    void onNetConnected(@NetType String type);

    /**
     * 没有网络
     */
    void onNetDisConnect();
}
