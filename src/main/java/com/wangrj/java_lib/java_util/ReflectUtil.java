package com.wangrj.java_lib.java_util;

import com.wangrj.java_lib.test.JavaLibTestClass;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
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

    public static void setNumber(Field field, Object obj, Long value) {
        try {
            field.setAccessible(true);
            String typeName = field.getType().getName();
            switch (typeName) {
                case "int":
                case "java.lang.Integer":
                    int v = Integer.parseInt(String.valueOf(value));
                    field.set(obj, v);
                    break;
                case "long":
                case "java.lang.Long":
                    field.set(obj, value);
                    break;
                default:
                    throw new RuntimeException("field '" + field.getName() + "' type is '" + typeName + "'");
            }
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

    /**
     * 获取参数类的第一个泛型的类对象
     * 如参数为Dao<User>.class，则返回User.class
     */
    public static Class getGenericClass(Class cls) {
        ParameterizedType type = (ParameterizedType) cls.getGenericSuperclass();
        Type[] types = type.getActualTypeArguments();
        if (types != null && types.length > 0) {
            return (Class) types[0];
        }
        return null;
    }

    /**
     * 给obj赋值
     */
    public static void setObjectValue(Object obj, boolean ignoreMismatch, GetValue getValue) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String type = field.getType().getName();
            Object value = getValue.get(field);
            if (value == null) {
                continue;
            }
            try {
                switch (type) {
                    case "int":
                    case "java.lang.Integer":
                        field.setInt(obj, Integer.parseInt(String.valueOf(value)));
                        break;
                    case "long":
                    case "java.lang.Long":
                        field.setLong(obj, Long.parseLong(String.valueOf(value)));
                        break;
                    case "float":
                    case "java.lang.Float":
                        field.setFloat(obj, Float.parseFloat(String.valueOf(value)));
                        break;
                    case "double":
                    case "java.lang.Double":
                        field.setDouble(obj, Double.parseDouble(String.valueOf(value)));
                        break;
                    case "boolean":
                    case "java.lang.Boolean":
                        field.setBoolean(obj, Boolean.parseBoolean(String.valueOf(value)));
                        break;
                    case "java.util.Date":
                        if (checkExtends(Date.class, value.getClass())) {// 如果value是Date的子类
                            field.set(obj, value);
                        } else {
                            String dateString = (String) value;
                            Date date = DateUtil.toDate(dateString);
                            field.set(obj, date);
                        }
                        break;
                    default:
                        field.set(obj, value);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
                if (!ignoreMismatch) {
                    throw new RuntimeException("set mismatch value \"" + value + "\" to field " +
                            field.getType().getSimpleName() + " " + field.getName());
                }
            }
        }
    }

    public static <T> T setObjectValue(Class<T> cls, boolean ignoreMismatch, GetValue getValue) {
        T obj;
        try {
            obj = cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(cls.getName() + " must have no-parameter create method");
        }
        setObjectValue(obj, ignoreMismatch, getValue);
        return obj;
    }

    public interface GetValue {
        Object get(Field field);
    }

}
