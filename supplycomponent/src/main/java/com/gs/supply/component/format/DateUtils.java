package com.gs.supply.component.format;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * @author husky
 * create on 2019/4/11-17:15
 */
public class DateUtils {


    /**
     * 返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(@NonNull Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        return c.getTimeInMillis();
    }

    /**
     * 日期相加
     *
     * @param date 日期
     * @param day  天数
     * @return 返回相加后的日期
     */
    public static Date addDate(@NonNull Date date, long day) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTimeInMillis(getMillis(date) + day * 24 * 3600 * 1000);
        return c.getTime();
    }

    /**
     * 格式化日期格式返回字符串
     *
     * @param date       日期
     * @param dateFormat 格式化的形式
     * @return 格式化日期格式返回字符串
     */
    public static String formatDate(Date date, DateFormat dateFormat) {
        return dateFormat.format(date);
    }

    /**
     * 把指定格式日期的字符串转变成日期
     *
     * @param dateStr    需要转换的字符串
     * @param dateFormat 日期格式
     * @return 日期
     */
    public static Date changeStrig2Date(@NonNull String dateStr, @NonNull DateFormat dateFormat) {
        try {
            return new Date(dateFormat.parse(dateStr).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 把long类型的数据时间戳转变成指定格式的日期
     *
     * @param millisecond
     * @param dateFormat
     * @return 指定格式的日期的字符串
     */
    public static String long2DateString(long millisecond, @NonNull DateFormat dateFormat) {
        return formatDate(new Date(millisecond), dateFormat);
    }

}
