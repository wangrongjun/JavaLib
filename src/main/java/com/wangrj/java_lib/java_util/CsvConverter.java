package com.wangrj.java_lib.java_util;

import org.junit.Test;

import java.lang.reflect.Field;
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

    interface ValueConverter {
        Object convert(Field field, Object value);
    }

    public <T> String toCsvText(List<T> entityList) {
        return toCsvText(entityList, (field, value) -> {
            if (value instanceof Date) {// 如果value是Date的子类
                Date date = (Date) value;
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            }
            return value.toString();
        });
    }

    /**
     * 把实体列表转换为Csv文本
     * <p>
     * Csv规则（参考百度百科：https://baike.baidu.com/item/CSV/10739）
     * 1. 包含逗号, 双引号, 或是换行符的字段必须放在引号内
     * 2. 字段内部的引号必须在其前面增加一个引号来实现文字引号的转码
     * 3. 分隔符逗号前后的空格 可能不会 被修剪掉. 这是RFC 4180的要求
     * 4. 元素中的换行符将被保留下来
     *
     * @param entityList
     * @param converter
     * @param <T>
     * @return
     */
    public <T> String toCsvText(List<T> entityList, ValueConverter converter) {
        // 判断列表是否为空
        if (entityList == null || entityList.size() == 0) {
            return null;
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
                // 进行数据转换
                if (converter != null) {
                    value = converter.convert(field, value);
                }
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

    public <T> List<T> fromCsvText(String csvText) {
        return null;
    }

    public CsvConverter setColumnSeparator(String columnSeparator) {
        this.columnSeparator = columnSeparator;
        return this;
    }

    public CsvConverter setRowSeparator(String rowSeparator) {
        this.rowSeparator = rowSeparator;
        return this;
    }

    // ====================================== 以下是测试代码 ======================================

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

}
