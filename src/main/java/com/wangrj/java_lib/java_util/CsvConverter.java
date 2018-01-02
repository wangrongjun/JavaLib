package com.wangrj.java_lib.java_util;

import org.junit.Test;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Csv文本与对象列表之间的相互转化
 */
public class CsvConverter {

    private String columnSeparator = ";";
    private String rowSeparator = "\r\n";
    private boolean ignoreCsvTextSyntaxError = true;

    public <T> String toCsvText(List<T> entityList) {
        // 判断列表是否为空
        if (entityList == null || entityList.size() == 0) {
            return null;
        }
        if (entityList.size() == 0) {
            return "";
        }

        // 初始化
        StringBuilder builder = new StringBuilder();
        Field[] fields = entityList.get(0).getClass().getDeclaredFields();

        // 遍历生成Csv文本
        for (int i = 0; i < entityList.size(); i++) {
            T entity = entityList.get(i);
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                field.setAccessible(true);
                Object value = ReflectUtil.get(field, entity);
                builder.append(value == null ? "" : value.toString());
                if (j < fields.length - 1) {// 如果不是当前行的最后一个属性，就在属性值后面加列分隔符
                    builder.append(columnSeparator);
                }
            }
            if (i < entityList.size() - 1) {// 如果不是最后一行，就在后面加行分隔符
                builder.append(rowSeparator);
            }
        }

        // 返回Csv文本
        return builder.toString();
    }

    // ====================================== Test Separator ======================================

    static class UserBean {
        enum Gender {
            MAN, WOMAN
        }

        private long id;
        private String name;
        private Gender gender;
        private int age;
        private boolean vip;
        private Date birthday;

        public UserBean() {
        }

        public UserBean(long id, String name, Gender gender, int age, boolean vip, Date birthday) {
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.age = age;
            this.vip = vip;
            this.birthday = birthday;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Gender getGender() {
            return gender;
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public boolean isVip() {
            return vip;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

    @Test
    public void testToCsvText() {
        List<UserBean> userBeanList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            UserBean user = new UserBean(
                    53435654635434L + i,
                    "wang_" + i, i % 2 == 0 ? UserBean.Gender.MAN : UserBean.Gender.WOMAN,
                    22 + i,
                    i % 2 == 0,
                    DateUtil.toDate("2107-10-" + i)
            );
            userBeanList.add(user);
        }
        System.out.println(toCsvText(userBeanList));
    }

    public static void main(String[] args) throws ParseException {
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse("Mon Oct 10 00:00:00 CST 2107");
        System.out.println(date);
    }

}
