package com.wangrg.java_util;

import com.wangrg.test.JavaLibTestClass;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * by Administrator on 2016/3/4.
 */
public class ReflectUtil {

    public static Field findByAnno(Class entityClass, Class annoClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(annoClass) != null) {
                return field;
            }
        }
        return null;
    }

    public static Object get(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void set(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> T newInstance(Class<T> entityClass) {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> getDeclaredFields(Class cls) {
        List<String> strFields = new ArrayList<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String modifiers = Modifier.toString(field.getModifiers());
            String type = TextUtil.getTextAfterLastPoint(field.getType().toString());
            String name = field.getName();
            String s = modifiers + " " + type + " " + name + ";";
            strFields.add(s);
            System.out.println(s);
        }
        return strFields;
    }

    private static List<String> getDeclaredMethods(Class cls) {
        List<String> strFields = new ArrayList<>();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            String modifiers = Modifier.toString(method.getModifiers());
            String declare = TextUtil.getTextAfterLastPoint(method.getReturnType().toString());
            declare = declare.replace(";", "");
            String name = method.getName();
            String parameters = "";
            Class[] c = method.getParameterTypes();
            for (Class c1 : c) {
                parameters += TextUtil.getTextAfterLastPoint(c1.toString()) + ", ";
            }
            if (c.length > 0) {
                parameters = parameters.substring(0, parameters.length() - 2);
            }
            String s = modifiers + " " + declare + " " + name + "(" + parameters + ")" + ";";
            strFields.add(s);
            System.out.println(s);
        }
        return strFields;
    }

    @Test
    public void testCheckExtends() {
        Assert.assertEquals(true, checkExtends(LogUtil.class, B.class));
        Assert.assertEquals(true, checkExtends(LogUtil.class, C.class));
        Assert.assertEquals(true, checkExtends(B.class, C.class));

        Assert.assertEquals(true, checkExtends(LogUtil.class, LogUtil.class));
        Assert.assertEquals(true, checkExtends(B.class, B.class));
        Assert.assertEquals(true, checkExtends(C.class, C.class));

        Assert.assertEquals(false, checkExtends(B.class, LogUtil.class));
        Assert.assertEquals(false, checkExtends(C.class, LogUtil.class));
        Assert.assertEquals(false, checkExtends(C.class, B.class));

        Assert.assertEquals(true, checkExtends(Object.class, B.class));
        Assert.assertEquals(false, checkExtends(B.class, Object.class));
        Assert.assertEquals(false, checkExtends(JavaLibTestClass.class, LogUtil.class));
        Assert.assertEquals(false, checkExtends(B.class, JavaLibTestClass.class));
    }

    static class A {
    }

    static class B extends A {
    }

    static class C extends B {
    }

    /**
     * 检查class1是否为class2的父类。若是，返回true。
     * 注意：如果class1==class2，返回true。
     */
    public static boolean checkExtends(Class class1, Class class2) {
        if (class1.getName().equals("java.lang.Object")) {
            return true;
        }
        while (!class2.getName().equals("java.lang.Object")) {
            if (class2.getName().equals(class1.getName())) {
                return true;
            }
            class2 = class2.getSuperclass();
        }
        return false;
    }

}
