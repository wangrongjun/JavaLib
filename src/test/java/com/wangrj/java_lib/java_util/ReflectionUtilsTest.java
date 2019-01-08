package com.wangrj.java_lib.java_util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * by wangrongjun on 2018/12/24.
 */
public class ReflectionUtilsTest {

    public static class User {
        private String a = "aaa";
        private String name = "name_1";
        private String pass = "pass_1";

        public String getA() {
            return "getter:" + a;
        }

        public String getName() {
            return "getter:" + name;
        }
    }

    @Test
    public void getValueByGetterOrField() throws NoSuchFieldException {
        assertEquals("getter:aaa", ReflectionUtils.getValueByGetterOrField(User.class.getDeclaredField("a"), new User()));
        assertEquals("getter:name_1", ReflectionUtils.getValueByGetterOrField(User.class.getDeclaredField("name"), new User()));
        assertEquals("pass_1", ReflectionUtils.getValueByGetterOrField(User.class.getDeclaredField("pass"), new User()));
    }

}