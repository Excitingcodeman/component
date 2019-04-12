package com.gs.supply.component.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Currency;


/**
 * 系统货币类型；统一输出格式，值可为Null
 *
 * @author fuli
 * @version 1.0
 * @date 2017-06-02 16:41
 */
public final class MoneyUtil implements Cloneable, Serializable, Comparable<MoneyUtil> {
    private static final long serialVersionUID = 1L;
    // 输出格式精度
    private static final int SCALE = 2;
    // 除法计算的最小精度
    private static final int DIVIDE_MIN_SCALE = 16;
    private static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
    private static final String DEFAULT_CURRENCY_CODE = "CNY";
    public static DecimalFormat fnum = new DecimalFormat("##0.00");

    /**
     * 定义了系统货币计算精度<br/>
     * 货币精度：2，四舍五入截取
     *
     * @param value
     * @return
     */
    public static BigDecimal format(BigDecimal value) {
        return value != null ? value.setScale(SCALE, ROUNDING_MODE) : null;
    }

    /**
     * 格式化金额
     * @param value
     * @return
     */
    public static String formatMoney(String value) {
        if (value == null || value == "") {
            value = "0.00";
        }
        return fnum.format(new BigDecimal(value));
    }

        /**
         * 定义了Number类型转BigDecimal类型的
         *
         * @param number
         * @return
         */
    public static BigDecimal number2BigDecimal(Number number) {
        return number == null ? null : number instanceof BigDecimal ? (BigDecimal) number : new BigDecimal(number.toString());
    }

    /**
     * 货币类型暂无业务意义
     */
    private final Currency currency;
    private BigDecimal value;

    public MoneyUtil(Double number) {
        value = number2BigDecimal(number);
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    public MoneyUtil(Float number) {
        value = number2BigDecimal(number);
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    public MoneyUtil(Long number) {
        value = number2BigDecimal(number);
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    public MoneyUtil(Integer number) {
        value = number2BigDecimal(number);
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    public MoneyUtil(Short number) {
        value = number2BigDecimal(number);
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    public MoneyUtil(Byte number) {
        value = number2BigDecimal(number);
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    public MoneyUtil(String number) {
        if (number != null) {
            value = new BigDecimal(number);
        }
        this.currency = Currency.getInstance(DEFAULT_CURRENCY_CODE);
    }

    /*****
     * calculate
     *****/
    private MoneyUtil doAdd(Number target) {
        value = value.add(number2BigDecimal(target));
        return this;
    }

    public MoneyUtil add(MoneyUtil target) {
        return doAdd(target.value);
    }

    public MoneyUtil add(double target) {
        return doAdd(target);
    }

    public MoneyUtil add(float target) {
        return doAdd(target);
    }

    public MoneyUtil add(long target) {
        return doAdd(target);
    }

    public MoneyUtil add(int target) {
        return doAdd(target);
    }

    public MoneyUtil add(short target) {
        return doAdd(target);
    }

    public MoneyUtil add(byte target) {
        return doAdd(target);
    }

    public MoneyUtil add(String target) {
        return doAdd(new BigDecimal(target));
    }

    private MoneyUtil doSubtract(Number target) {
        value = value.subtract(number2BigDecimal(target));
        return this;
    }

    public MoneyUtil subtract(MoneyUtil target) {
        return doSubtract(target.value);
    }

    public MoneyUtil subtract(double target) {
        return doSubtract(target);
    }

    public MoneyUtil subtract(float target) {
        return doSubtract(target);
    }

    public MoneyUtil subtract(long target) {
        return doSubtract(target);
    }

    public MoneyUtil subtract(int target) {
        return doSubtract(target);
    }

    public MoneyUtil subtract(short target) {
        return doSubtract(target);
    }

    public MoneyUtil subtract(byte target) {
        return doSubtract(target);
    }

    public MoneyUtil subtract(String target) {
        return doSubtract(new BigDecimal(target));
    }

    private MoneyUtil doMultiply(Number target) {
        value = value.multiply(number2BigDecimal(target));
        return this;
    }

    public MoneyUtil multiply(MoneyUtil target) {
        return doMultiply(target.value);
    }

    public MoneyUtil multiply(double target) {
        return doMultiply(target);
    }

    public MoneyUtil multiply(float target) {
        return doMultiply(target);
    }

    public MoneyUtil multiply(long target) {
        return doMultiply(target);
    }

    public MoneyUtil multiply(int target) {
        return doMultiply(target);
    }

    public MoneyUtil multiply(short target) {
        return doMultiply(target);
    }

    public MoneyUtil multiply(byte target) {
        return doMultiply(target);
    }

    public MoneyUtil multiply(String target) {
        return doMultiply(new BigDecimal(target));
    }

    private MoneyUtil doDivide(Number target) {
        BigDecimal bdTarget = number2BigDecimal(target);
        int scale = bdTarget.scale() > value.scale() ? bdTarget.scale() : value.scale();
        value = value.divide(number2BigDecimal(target), scale > DIVIDE_MIN_SCALE ? scale : DIVIDE_MIN_SCALE, ROUNDING_MODE);
        return this;
    }

    public MoneyUtil divide(MoneyUtil target) {
        return doDivide(target.value);
    }

    public MoneyUtil divide(double target) {
        return doDivide(target);
    }

    public MoneyUtil divide(float target) {
        return doDivide(target);
    }

    public MoneyUtil divide(long target) {
        return doDivide(target);
    }

    public MoneyUtil divide(int target) {
        return doDivide(target);
    }

    public MoneyUtil divide(short target) {
        return doDivide(target);
    }

    public MoneyUtil divide(byte target) {
        return doDivide(target);
    }

    public MoneyUtil divide(String target) {
        return doDivide(new BigDecimal(target));
    }


    public boolean isValueNull() {
        return value == null;
    }

    @Override
    public int compareTo(MoneyUtil target) {
        return value.compareTo(target.value);
    }

    /**
     * 若value为Null则将其设置为ZERO
     *
     * @return
     */
    public MoneyUtil zeroIfNull() {
        value = value == null ? BigDecimal.ZERO : value;
        return this;
    }

    public Currency currency() {
        return currency;
    }

    /**
     * 格式化后的值
     *
     * @return
     */
    public BigDecimal value() {
        return format(value);
    }

    /**
     * 全精度值
     *
     * @return
     */
    public BigDecimal originalValue() {
        return value;
    }

    @Override
    public MoneyUtil clone() {
        try {
            return (MoneyUtil) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 以分为单位表示
     *
     * @return
     */
    public BigDecimal cent() {
        return value == null ? null : value().multiply(BigDecimal.valueOf(100)).setScale(0);
    }

    public String dump() {
        StringBuffer sb = new StringBuffer();
        sb.append("MoneyUtil [hashCode()=").append(hashCode()).append(", ");
        sb.append("value=").append(format(value)).append(", ");
        sb.append("currency=").append(currency).append("]");
        return sb.toString();
    }

    @Override
    public String toString() {
        return value == null ? "Null" : value().toString();
    }

}
