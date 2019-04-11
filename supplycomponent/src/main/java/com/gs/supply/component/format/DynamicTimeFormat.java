package com.gs.supply.component.format;

import android.support.annotation.NonNull;

import com.gs.supply.component.R;
import com.gs.supply.component.resources.ResourcesUtils;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author husky
 * create on 2019/4/11-14:48
 * 动态时间格式化
 */
public class DynamicTimeFormat extends SimpleDateFormat {


    static final String defaultFormat = "%s";
    static final String defaultYearFormat = "yyyy年";
    static final String defaultDateFormat = "M月d日";
    static final String defaultTimeFormat = "HH:mm";
    private static String weeks[] = ResourcesUtils.getStringArray(R.array.weeks);
    private static String moments[] = ResourcesUtils.getStringArray(R.array.moments);

    private String mFormat = defaultFormat;

    public DynamicTimeFormat() {
        this(defaultFormat, defaultYearFormat, defaultDateFormat, defaultTimeFormat);
    }

    public DynamicTimeFormat(String format) {
        this(format, defaultYearFormat, defaultDateFormat, defaultTimeFormat);
    }

    public DynamicTimeFormat(String yearFormat, String dateFormat, String timeFormat) {
        super(String.format(LocaleHelper.getLocal(), "%s %s %s", yearFormat, dateFormat, timeFormat), LocaleHelper.getLocal());
    }

    public DynamicTimeFormat(String format, String yearFormat, String dateFormat, String timeFormat) {
        this(yearFormat, dateFormat, timeFormat);
        this.mFormat = format;
    }

    @Override
    public StringBuffer format(@NonNull Date date, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
        toAppendTo = super.format(date, toAppendTo, pos);

        Calendar otherCalendar = calendar;
        Calendar todayCalendar = Calendar.getInstance();

        int hour = otherCalendar.get(Calendar.HOUR_OF_DAY);

        String[] times = toAppendTo.toString().split(" ");
        String moment = hour == 12 ? moments[0] : moments[hour / 6 + 1];
        String timeFormat = moment + " " + times[2];
        String dateFormat = times[1] + " " + timeFormat;
        String yearFormat = times[0] + dateFormat;
        toAppendTo.delete(0, toAppendTo.length());

        boolean yearTemp = todayCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR);
        if (yearTemp) {
            int todayMonth = todayCalendar.get(Calendar.MONTH);
            int otherMonth = otherCalendar.get(Calendar.MONTH);
            if (todayMonth == otherMonth) {
                //表示是同一个月
                int temp = todayCalendar.get(Calendar.DATE) - otherCalendar.get(Calendar.DATE);
                switch (temp) {
                    case 0:
                        toAppendTo.append(timeFormat);
                        break;
                    case 1:
                        toAppendTo.append(ResourcesUtils.getStringArray(R.array.days)[1]+" ");
                        toAppendTo.append(timeFormat);
                        break;
                    case 2:
                        toAppendTo.append(ResourcesUtils.getStringArray(R.array.days)[3]+" ");
                        toAppendTo.append(timeFormat);
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        int dayOfMonth = otherCalendar.get(Calendar.WEEK_OF_MONTH);
                        int todayOfMonth = todayCalendar.get(Calendar.WEEK_OF_MONTH);
                        if (dayOfMonth == todayOfMonth) {
                            //表示是同一周
                            int dayOfWeek = otherCalendar.get(Calendar.DAY_OF_WEEK);
                            if (dayOfWeek != 1) {
                                //判断当前是不是星期日     如想显示为：周日 12:09 可去掉此判断
                                toAppendTo.append(weeks[otherCalendar.get(Calendar.DAY_OF_WEEK) - 1]);
                                toAppendTo.append(' ');
                                toAppendTo.append(timeFormat);
                            } else {
                                toAppendTo.append(dateFormat);
                            }
                        } else {
                            toAppendTo.append(dateFormat);
                        }
                        break;
                    default:
                        toAppendTo.append(dateFormat);
                        break;
                }
            } else {
                toAppendTo.append(dateFormat);
            }
        } else {
            toAppendTo.append(yearFormat);
        }

        int length = toAppendTo.length();
        toAppendTo.append(String.format(LocaleHelper.getLocal(), mFormat, toAppendTo.toString()));
        toAppendTo.delete(0, length);
        return toAppendTo;
    }
}
