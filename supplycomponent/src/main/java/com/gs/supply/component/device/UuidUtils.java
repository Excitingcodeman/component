package com.gs.supply.component.device;

import java.util.UUID;

/**
 * @author husky
 * create on 2019/4/12-10:35
 */
public class UuidUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
