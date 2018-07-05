package com.wangrj.java_lib.java_util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * by wangrongjun on 2018/2/3.
 */
public class ReflectUtilTest {

    private User user;

    @Before
    public void initUser() {
        user = new User();
        user.setId(2);
        user.setName("name");
        user.setVip(true);
        user.setMan(true);
        user.setIsObsoletedDate(new Date());
    }

    @Test
    public void testCheckExtends() {
        Assert.assertEquals(true, ReflectUtil.checkExtends(A.class, B.class));
        Assert.assertEquals(true, ReflectUtil.checkExtends(A.class, C.class));
        Assert.assertEquals(true, ReflectUtil.checkExtends(B.class, C.class));

        Assert.assertEquals(true, ReflectUtil.checkExtends(A.class, A.class));
        Assert.assertEquals(true, ReflectUtil.checkExtends(B.class, B.class));
        Assert.assertEquals(true, ReflectUtil.checkExtends(C.class, C.class));

        Assert.assertEquals(false, ReflectUtil.checkExtends(B.class, A.class));
        Assert.assertEquals(false, ReflectUtil.checkExtends(C.class, A.class));
        Assert.assertEquals(false, ReflectUtil.checkExtends(C.class, B.class));

        Assert.assertEquals(true, ReflectUtil.checkExtends(Object.class, B.class));
        Assert.assertEquals(false, ReflectUtil.checkExtends(B.class, Object.class));
        Assert.assertEquals(false, ReflectUtil.checkExtends(User.class, A.class));
    }

    @Test
    public void testGetter() throws InvocationTargetException, IllegalAccessException {
        int id = (int) ReflectUtil.getter(User.class, "id").invoke(user);
        assertEquals(user.id, id);
        String name = (String) ReflectUtil.getter(User.class, "name").invoke(user);
        assertEquals(user.name, name);
        boolean isVip = (boolean) ReflectUtil.getter(User.class, "isVip").invoke(user);
        assertEquals(user.isVip, isVip);
        boolean man = (boolean) ReflectUtil.getter(User.class, "man").invoke(user);
        assertEquals(user.man, man);
        Date isObsoletedDate = (Date) ReflectUtil.getter(User.class, "isObsoletedDate").invoke(user);
        assertEquals(user.isObsoletedDate, isObsoletedDate);
    }

    @Test
    public void testIsBoolean() throws NoSuchFieldException {
        Field name = User.class.getDeclaredField("name");
        Field isVip = User.class.getDeclaredField("isVip");
        Field man = User.class.getDeclaredField("man");
        Field isObsoletedDate = User.class.getDeclaredField("isObsoletedDate");
        assertFalse(ReflectUtil.isBoolean(name));
        assertTrue(ReflectUtil.isBoolean(isVip));
        assertTrue(ReflectUtil.isBoolean(man));
        assertFalse(ReflectUtil.isBoolean(isObsoletedDate));
    }

    static class A {
    }

    static class B extends A {
    }

    static class C extends B {
    }

    private static class User {
        private int id;
        private String name;
        private boolean isVip;
        private boolean man;
        private Date isObsoletedDate;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isVip() {
            return isVip;
        }

        public void setVip(boolean vip) {
            isVip = vip;
        }

        public boolean isMan() {
            return man;
        }

        public void setMan(boolean man) {
            this.man = man;
        }

        public Date getIsObsoletedDate() {
            return isObsoletedDate;
        }

        public void setIsObsoletedDate(Date isObsoletedDate) {
            this.isObsoletedDate = isObsoletedDate;
        }
    }

}
