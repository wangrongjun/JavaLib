package com.wangrj.java_lib.java_util;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class DateUtil {

    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_TIME = "HH:mm:ss";
    public static final String PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = (SimpleDateFormat) DateFormat.getInstance();
        sdf.applyPattern(pattern);
        return sdf.format(date);
    }

    public static String formatLocalDateTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(PATTERN_DATE_TIME));
    }

    public static String formatLocalDateTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取系统当前日期，格式：yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return formatDate(new Date(), PATTERN_DATE);
    }

    /**
     * 获取系统当前时间，格式：HH:mm:ss
     */
    public static String getCurrentTime() {
        return formatDate(new Date(), PATTERN_TIME);
    }

    /**
     * 获取系统当前日期和时间，格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateAndTime() {
        return formatDate(new Date(), PATTERN_DATE_TIME);
    }

    /**
     * @param date1 输入如2016-05-20或2016-5-20
     * @return date1与date2相隔的天数，负数则date1 < date2。依次类推。
     */
    public static int compareDate(Date date1, Date date2) {
        long time = date1.getTime() - date2.getTime();
        return (int) (time / 1000 / 60 / 60 / 24);
    }

    /**
     * 推迟或提早日期。如2016-07-08，天数为4，则输出2016-07-12
     *
     * @param day 可以为负数
     */
    public static Date changeDate(Date date, int day) {
        // 注意！1000L中最后的L一定要加，否则long+int的结果会出错！！！
        return new Date(date.getTime() + day * 24 * 60 * 60 * 1000L);
    }

    /**
     * 获得网络日期 格式：2016-01-31
     */
    public static String getInternetDate() {
        String baiduWeatherUrl = "http://api.map.baidu.com/telematics/v3/weather?" +
                "output=json&ak=6tYzTvGZSOpYB5Oc2YGGOKt8&location=";
        try {
            URL url = new URL(baiduWeatherUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);

            InputStreamReader isr = new InputStreamReader(
                    conn.getInputStream(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
            br.close();
            isr.close();

            String html = builder.toString();
            int i = html.indexOf("date\":\"");
            String date = html.substring(i + 7).substring(0, 10);
            return date;
        } catch (Exception e) {
            return "";
        }
    }

    public static String toDateTimeText(Date date) {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 支持转换的格式如下：
     * 1.yyyy-MM-dd
     * 2.yyyy-M-d
     * 3.yyyy-MM-dd HH:mm:ss
     * 4.yyyy-M-d H:m:s
     */
    public static Date toDate(String dateText) throws DateTextSyntaxErrorException {
        String datePattern = null;
        if (dateText.matches("[\\d]{4}-[\\d]{1,2}-[\\d]{1,2}")) {
            datePattern = "yyyy-MM-dd";
        }
        if (dateText.matches("[\\d]{4}-[\\d]{1,2}-[\\d]{1,2} [\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}")) {
            datePattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (datePattern == null) {
            throw new DateTextSyntaxErrorException(dateText);
        }
        try {
            return new SimpleDateFormat(datePattern).parse(dateText);
        } catch (ParseException e) {
            throw new DateTextSyntaxErrorException(e);
        }
    }

    public static class DateTextSyntaxErrorException extends RuntimeException {
        DateTextSyntaxErrorException(Throwable cause) {
            super(cause);
        }

        DateTextSyntaxErrorException(String date) {
            super(date);

        }
    }

    @Test
    public void test() {
        assertEquals(-4, compareDate(toDate("2017-08-01"), toDate("2017-08-05")));
        assertEquals(4, compareDate(toDate("2017-08-05"), toDate("2017-08-01")));
        assertEquals(-2, compareDate(toDate("2017-08-31"), toDate("2017-09-02")));

        assertEquals("2017-08-27", formatDate(changeDate(toDate("2017-08-31"), -4), PATTERN_DATE));
        assertEquals("2017-09-04", formatDate(changeDate(toDate("2017-08-31"), 4), PATTERN_DATE));
    }

    @Test
    public void testToDateAndToDateTimeText() throws DateTextSyntaxErrorException {
        assertEquals("2015-12-03 00:00:00", toDateTimeText(toDate("2015-12-03")));//正常
        assertEquals("2015-12-03 00:00:00", toDateTimeText(toDate("2015-12-03")));//正常
        assertEquals("1995-12-03 00:00:00", toDateTimeText(toDate("1995-12-03")));//正常
        assertEquals("2017-02-03 12:54:30", toDateTimeText(toDate("2017-02-03 12:54:30")));//正常
        assertEquals("2017-02-03 00:04:03", toDateTimeText(toDate("2017-2-3 0:4:3")));//正常
        assertEquals("2017-02-03 23:04:03", toDateTimeText(toDate("2017-2-3 23:4:3")));//正常
        try {
            toDateTimeText(toDate("015-01-03 12:34:10"));//格式错误，应该抛异常
            fail("should throw DateTextSyntaxErrorException, actually doesn't throw");
        } catch (DateTextSyntaxErrorException e) {
            e.printStackTrace(System.out);
        }
        try {
            toDateTimeText(toDate("2015-01-03_12:34:10"));//格式错误，应该抛异常
            fail("should throw DateTextSyntaxErrorException, actually doesn't throw");
        } catch (DateTextSyntaxErrorException e) {
            e.printStackTrace(System.out);
        }
        try {
            toDateTimeText(toDate("2015-01-a3 12:34:10"));//非数字，应该抛异常
            fail("should throw DateTextSyntaxErrorException, actually doesn't throw");
        } catch (DateTextSyntaxErrorException e) {
            e.printStackTrace(System.out);
        }
    }

}
