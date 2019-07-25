package com.gs.supply.component.device;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import com.gs.supply.component.Component;
import com.gs.supply.component.encryption.MD5Util;


/**
 * @author husky
 * create on 2019-07-25-10:34
 */
public class SignUtil {

    public static String getCertificateMD5() {
        String md5 = "";
        try {
            String packageName = Component.mApplicationContext.getApplicationContext().getPackageName();
            PackageInfo packageInfo = Component.mApplicationContext.getApplicationContext().getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            byte[] cert = sign.toByteArray();
            md5 = MD5Util.getMD5String(cert);
            Log.e("TAG", "md5 = " + md5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return md5;
    }

}
