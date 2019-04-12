package com.gs.supply.component.broadcast;

import android.support.annotation.StringDef;

import static com.gs.supply.component.broadcast.NetTypeConfig.CMNET_TYPE;
import static com.gs.supply.component.broadcast.NetTypeConfig.CMWAP_TYPE;
import static com.gs.supply.component.broadcast.NetTypeConfig.NONE_TYPE;
import static com.gs.supply.component.broadcast.NetTypeConfig.WIFI_TYPE;

/**
 * @author husky
 * create on 2019/4/12-12:02
 */
@StringDef({WIFI_TYPE,CMNET_TYPE,CMWAP_TYPE,NONE_TYPE})
public @interface NetType {

}
