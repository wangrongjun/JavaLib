package com.wangrj.java_lib.db3.main;

import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.Assert.assertEquals;

/**
 * by wangrongjun on 2017/11/16.
 */
public interface NameConverter {

    /**
     * 把POJO类的属性名转换为数据库字段名
     */
    String toColumnName(Field field);

    /**
     * 把POJO类名转换为数据库表名
     */
    String toTableName(Class pojoClass);

    /**
     * 默认转换器
     */
    class DefaultConverter implements NameConverter {
        @Override
        public String toColumnName(Field field) {
            return field.getName();
        }

        @Override
        public String toTableName(Class pojoClass) {
            return pojoClass.getSimpleName();
        }
    }

    /**
     * 驼峰命名 --> 下划线命名
     * <p>
     * 注意：表名的首字母即使是大写也不会加下划线
     */
    class HumpToUnderlineConverter implements NameConverter {
        public static String convert(String name, boolean firstLetterNoUnderline) {
            String result = "";
            char[] chars = name.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char c = chars[i];
                if (firstLetterNoUnderline && i == 0) {
                    result += ("" + c).toLowerCase();
                } else if (c >= 65 && c <= 90) {
                    result += ("_" + c).toLowerCase();
                } else {
                    result += c;
                }
            }
            return result;
        }

        @Override
        public String toColumnName(Field field) {
            return convert(field.getName(), false);
        }

        @Override
        public String toTableName(Class pojoClass) {
            return convert(pojoClass.getSimpleName(), true);
        }

        @Test
        public void testNameConverter() throws NoSuchFieldException {
            class UserInfo {
                private String userId;
                private String PhoneNumber;
            }

            assertEquals("user_id", toColumnName(UserInfo.class.getDeclaredField("userId")));
            assertEquals("_phone_number", toColumnName(UserInfo.class.getDeclaredField("PhoneNumber")));

            assertEquals("user_info", toTableName(UserInfo.class));
        }
    }

}
