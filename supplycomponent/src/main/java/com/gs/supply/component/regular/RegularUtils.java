package com.gs.supply.component.regular;

import android.support.annotation.NonNull;

/**
 * @author husky
 * create on 2019/4/12-09:37
 */
public class RegularUtils {
    /**
     * 手机号码正则
     */
    private static String PHONE_REX = "^1[0-9]\\d{9}";
    /**
     * 密码正则
     */
    private static String PASS_REX = "^(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[\\-\\/:;()$&@\"\\.,\\?\\!'\\[\\]#%\\^\\*\\+=_\\\\\\|~<>€£¥•：；（）¥@“”。，、？！【】｛｝—《》\\·]+$)[\\da-zA-Z\\-\\/:;()$&@\"\\.,\\?\\!'\\[\\]#%\\^\\*\\+=_\\\\\\|~<>€£¥•：；（）¥@“”。，、？！【】｛｝—《》\\·]{6,20}$";
    /**
     * 身份证正则
     */
    private static String ID_REX = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";
    /**
     * 银行卡正则
     */
    private static String CARD_REX = "^\\d{16,19}$";
    /**
     * 汉字
     */
    private static String ZN_CHARACTERS_REX = "([\\u4e00-\\u9fa5]{2,10})";

    /**
     * 大小写和数字必须全包含的
     */
    private static String PASSWORD_REX= "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,20}$";
    /**
     * 邮箱
     */
    private static String EMAIL_REX = "^[A-Za-z0-9_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * @param source 需要校验的字符串
     * @param ruler  校验的规则
     * @return true  符合规则  false不符合规则
     */
    public static boolean checkRex(@NonNull String source, @NonNull String ruler) {
        return source.matches(ruler);
    }
}
