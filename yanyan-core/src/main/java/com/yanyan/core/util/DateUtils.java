package com.yanyan.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 11:36
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static class DateFormat {
        public String regex;//表达式
        public String miss;//丢失的部分
        public String pattern;//完整的模式

        public DateFormat(String regex, String miss, String pattern) {
            this.regex = regex;
            this.miss = miss;
            this.pattern = pattern;
        }
    }

    private static List<DateFormat> dateFormats = new ArrayList<DateFormat>();

    static {
        dateFormats.add(new DateFormat("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}", "", "yyyyMMddHHmmss"));//yyyyMMddHHmmss
        dateFormats.add(new DateFormat("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}[0-9]{2}", "00", "yyyyMMddHHmmss"));//yyyyMMddHHmm
        dateFormats.add(new DateFormat("[0-9]{4}[0-9]{2}[0-9]{2}[0-9]{2}", "yyyyMMddHHmmss", "0000"));//yyyyMMddHH
        dateFormats.add(new DateFormat("[0-9]{4}[0-9]{2}[0-9]{2}", "yyyyMMddHHmmss", "000000"));//yyyyMMdd
        dateFormats.add(new DateFormat("[0-9]{4}[0-9]{2}", "yyyyMMddHHmmss", "01000000"));//yyyyMM
        dateFormats.add(new DateFormat("[0-9]{4}", "yyyyMMddHHmmss", "0101000000"));//yyyy

        dateFormats.add(new DateFormat("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", "", "yyyy/MM/dd HH:mm:ss"));//yyyy/MM/dd HH:mm:ss
        dateFormats.add(new DateFormat("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}:[0-9]{2}", ":00", "yyyy/MM/dd HH:mm:ss"));//yyyy/MM/dd HH:mm
        dateFormats.add(new DateFormat("[0-9]{4}/[0-9]{2}/[0-9]{2} [0-9]{2}", ":00:00", "yyyy/MM/dd HH:mm:ss"));//yyyy/MM/dd HH
        dateFormats.add(new DateFormat("[0-9]{4}/[0-9]{2}/[0-9]{2}", " 00:00:00", "yyyy/MM/dd HH:mm:ss"));//yyyy/MM/dd
        dateFormats.add(new DateFormat("[0-9]{4}/[0-9]{2}", "/01 00:00:00", "yyyy/MM/dd HH:mm:ss"));//yyyy/MM

        dateFormats.add(new DateFormat("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", "", "yyyy-MM-dd HH:mm:ss"));//yyyy-MM-dd HH:mm:ss
        dateFormats.add(new DateFormat("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}", ":00", "yyyy-MM-dd HH:mm:ss"));//yyyy-MM-dd HH:mm
        dateFormats.add(new DateFormat("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}", ":00:00", "yyyy-MM-dd HH:mm:ss"));//yyyy-MM-dd HH
        dateFormats.add(new DateFormat("[0-9]{4}-[0-9]{2}-[0-9]{2}", " 00:00:00", "yyyy-MM-dd HH:mm:ss"));//yyyy-MM-dd
        dateFormats.add(new DateFormat("[0-9]{4}-[0-9]{2}", "-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));//yyyy-MM
    }

    /**
     * 解析日期
     *
     * @param date
     * @return
     */
    public static Date parseDate(String date) {
        return parseDate(date, Locale.getDefault());
    }

    /**
     * 解析日期
     *
     * @param date
     * @param locale
     * @return
     */
    public static Date parseDate(String date, Locale locale) {
        try {
            String str = StringUtils.trim(date);
            if (StringUtils.isNotEmpty(str)) {
                for (DateFormat dateFormat : dateFormats) {
                    if (Pattern.matches(dateFormat.regex, str)) {
                        return DateUtils.parseDateStrictly(str + dateFormat.miss, locale, (dateFormat.pattern));
                    }
                }
                if (StringUtils.isNumeric(date)) {
                    return new Date(NumberUtils.toLong(date));
                }
            }
            return null;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 解析日期
     *
     * @param date
     * @return
     */
    /*public static Date parseDate(String date) {
        String str = StringUtils.trimToEmpty(date);
        try {
            //不同的格式需要区分开，否则会混乱
            if (date.indexOf("-") > 0) {
                return DateUtils.parseDateStrictly(str, Locale.SIMPLIFIED_CHINESE, new String[]{
                        "yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd HH:mm",
                        "yyyy-MM-dd HH",
                        "yyyy-MM-dd",
                        "yyyy-MM",
                });
            } else if (date.indexOf("/") > 0) {
                return DateUtils.parseDateStrictly(str, Locale.SIMPLIFIED_CHINESE, new String[]{
                        "yyyy/MM",
                        "yyyy/MM/dd",
                        "yyyy/MM/dd HH",
                        "yyyy/MM/dd HH:mm",
                        "yyyy/MM/dd HH:mm:ss",
                });
            } else {
                if (date.length() == 2) {//默认本世纪
                    str = StringUtils.substring(DateFormatUtils.format(Calendar.getInstance(), "yyyy"), 0, 2) + str;
                }

                return DateUtils.parseDateStrictly(str, Locale.SIMPLIFIED_CHINESE, new String[]{
                        "yyyyMM",
                        "yyyyMMdd",
                        "yyyyMMddHH",
                        "yyyyMMddHHmm",
                        "yyyyMMddHHmmss",
                        "yyyy",
                        "yy",
                });
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }*/

    /**
     * 根据字符串创建一个日期
     *
     * @param str
     * @return
     */
    public static Date createDate(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        } else {
            return parseDate(StringUtils.trim(str));
        }
    }


    public static Date addMilliseconds(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addMilliseconds(date, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, amount);
    }

    public static Date addHours(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
    }

    public static Date addDays(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
    }

    public static Date addMonths(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
    }

    public static Date addYears(Date date, int amount) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
    }

    public static Date truncate(Date date, int field) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.truncate(date, field);
    }

    public static Date ceiling(Date date, int field) {
        if (date == null) return date;
        return org.apache.commons.lang3.time.DateUtils.ceiling(date, field);
    }

    /**
     * 2017-2-8 17:34:25.234
     * Calendar.DAY_OF_MONTH
     * =>2017-2-8 00:00:00.000
     * Calendar.MONTH
     * =>2017-2-1 00:00:00.000
     *
     * @param date
     * @param field
     * @return
     */
    public static Date getFirstMillisecond(Date date, int field) {
        return truncate(date, field);
    }

    /**
     * 2017-2-8 17:34:25.234
     * Calendar.DAY_OF_MONTH
     * =>2017-2-8 23:59:59.999
     * Calendar.MONTH
     * =>2017-2-28 23:59:59.999
     *
     * @param date
     * @param field
     * @return
     */
    public static Date getLastMillisecond(Date date, int field) {
        return addMilliseconds(ceiling(date, field), -1);
    }
}
