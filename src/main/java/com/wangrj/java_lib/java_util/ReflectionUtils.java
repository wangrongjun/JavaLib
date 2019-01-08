package com.wangrj.java_lib.java_util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * by wangrongjun on 2018/12/24.
 */
public class ReflectionUtils {

    /**
     * 通过反射从getter获取属性值。如果没有getter，直接从field获取。
     */
    public static Object getValueByGetterOrField(Field field, Object obj) {
        Object value;
        Class<?> declaringClass = field.getDeclaringClass();
        String getterMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
        Method getterMethod = null;
        try {
            getterMethod = declaringClass.getDeclaredMethod(getterMethodName);
        } catch (NoSuchMethodException ignored) {// 没有getter
        }
        try {
            if (getterMethod != null) {
                getterMethod.setAccessible(true);
                value = getterMethod.invoke(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return value;
    }

}
