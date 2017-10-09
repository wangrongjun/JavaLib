package com.wangrg.java_lib.web;

/**
 * by 王荣俊 on 2016/8/11.
 */
public class Cookie {

    private int id;
    private String cookieKey;
    private String cookieValue;
    private String createTime;
    private int aliveTime;

    public Cookie() {
    }

    public Cookie(String cookieKey, String cookieValue, String createTime, int aliveTime) {
        this.cookieKey = cookieKey;
        this.cookieValue = cookieValue;
        this.createTime = createTime;
        this.aliveTime = aliveTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCookieKey() {
        return cookieKey;
    }

    public void setCookieKey(String cookieKey) {
        this.cookieKey = cookieKey;
    }

    public String getCookieValue() {
        return cookieValue;
    }

    public void setCookieValue(String cookieValue) {
        this.cookieValue = cookieValue;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public void setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
    }

    static class Time {
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        public Time(int year, int month, int day, int hour, int minute, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }

}
