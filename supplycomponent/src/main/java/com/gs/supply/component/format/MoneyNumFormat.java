package com.gs.supply.component.format;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.gs.supply.component.R;
import com.gs.supply.component.resources.ResourcesUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author husky
 * create on 2019/4/11-16:33
 * 金额相关的数据的格式化，比较
 */
public class MoneyNumFormat {
    /**
     * 10000
     */
    public static final BigDecimal TEN_THOUSAND = new BigDecimal("10000");

    /**
     * 100000转换成100，000.00
     */
    public static String numToString(long l) {
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String ff = format.format(l);
        return ff.substring(1);
    }

    /**
     * 100000转换成100，000.00
     */
    public static String numToString(double l) {
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String ff = format.format(l);
        return ff.substring(1);
    }

    /**
     * 100000转换成100，000.00
     */
    public static String numToString(BigDecimal l) {
        if (null == l) {
            l = BigDecimal.ZERO;
        }
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String ff = format.format(l);
        return ff.substring(1);
    }

    /**
     * 100000转换成100，000.00
     */
    public static String numToString(String l) {
        BigDecimal num;
        if (TextUtils.isEmpty(l)) {
            num = BigDecimal.ZERO;
        } else {
            num = new BigDecimal(l);
        }
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance();
        String ff = format.format(num);
        return ff.substring(1);
    }


    /**
     * 100000转换成￥100，000.00
     */
    public static String numToMoneyString(long l, Locale locale) {
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String ff = format.format(l);
        return ff.substring(1);
    }

    /**
     * 100000转换成￥100，000.00
     */
    public static String numToMoneyString(double l, Locale locale) {
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String ff = format.format(l);
        return ff.substring(1);
    }

    /**
     * 100000转换成￥100，000.00
     */
    public static String numToMoneyString(BigDecimal l, Locale locale) {
        if (null == l) {
            l = BigDecimal.ZERO;
        }
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String ff = format.format(l);
        return ff.substring(1);
    }

    /**
     * 100000转换成￥100，000.00
     */
    public static String numToMoneyString(String l, Locale locale) {
        BigDecimal num;
        if (TextUtils.isEmpty(l)) {
            num = BigDecimal.ZERO;
        } else {
            num = new BigDecimal(l);
        }
        // 创建格式化对象
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String ff = format.format(num);
        return ff.substring(1);
    }

    /**
     * 100,000.00转换成100000
     */
    public static long MoneyTolong(String money) {
        if (TextUtils.isEmpty(money)) {
            return 0l;
        }
        try {
            String regEx = "[0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(money);
            String trim = m.replaceAll("").trim();
            String money1 = trim.substring(0, trim.length() - 2);
            return Long.parseLong(money1);
        } catch (NumberFormatException e) {
            return 0l;
        }
    }

    /**
     * 保留2位小数
     *
     * @param availableCredit 数据
     * @return 修改成保留2位小数的形式
     */
    public static String DoubleToString(double availableCredit) {
        // 创建格式化对象
        if (0 == availableCredit) {
            return "0.00";
        }
        String a = new BigDecimal(availableCredit + "").stripTrailingZeros().toPlainString();
        int indexPoint = a.indexOf(".");
        if (indexPoint != -1) {
            if (indexPoint == a.length() - 3) {
                return a;
            } else if (indexPoint == a.length() - 2) {
                return a + "0";
            } else {
                return a.substring(0, indexPoint + 3);
            }
        } else {
            return a + ".00";
        }

    }

    /**
     * @param num 目标数据
     * @return 得到除以10000后的结果  结果四舍五入保留2位小数
     */
    public static String numTenThousand(BigDecimal num) {
        if (num == null) {
            num = BigDecimal.ZERO;
        }
        return num.divide(TEN_THOUSAND, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static int parseInt(String num) {
        try {
            return Integer.parseInt(num);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static double parseDouble(String num) {
        try {
            return Double.parseDouble(num);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static long parseLong(String num) {
        try {
            return Long.parseLong(num);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 把大于10000元的金额  单元改为万元处理
     *
     * @param money
     * @return
     */
    public static String changeWanYuan(@NonNull BigDecimal money) {
        if (money.compareTo(TEN_THOUSAND) >= 0) {
            return money.divide(TEN_THOUSAND).setScale(1, RoundingMode.DOWN).toString() + ResourcesUtils.getString(R.string.wan_money_unit);
        } else {
            return money.toString() + ResourcesUtils.getString(R.string.money_unit);
        }
    }

    /**
     * -1 是小于  0是等于   1是大于
     *
     * @param source  源数据
     * @param compare 被比较的数据
     * @return 数据的比较结果
     */
    public static int compareTo(BigDecimal source, BigDecimal compare) {
        if (null == source) {
            return -1;
        }
        if (null == compare) {
            return 1;
        }

        return source.compareTo(compare);
    }


}
