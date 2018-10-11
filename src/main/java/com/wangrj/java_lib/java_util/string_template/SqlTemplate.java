package com.wangrj.java_lib.java_util.string_template;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SqlTemplate {

    /**
     * 处理 sql 中模版指令（--#if attribute 和 --#endif）包裹的内容。
     * <p>
     * 处理方式：根据 attribute 是否为空，来决定输出的结果是否包含模版指令包裹的内容。
     *
     * @throws IllegalArgumentException if attribute in template is not defined in dataModel
     */
    public static String process(String template, Object... dataModels) {
        if (dataModels == null || dataModels.length == 0) {
            throw new NullPointerException("dataModel is null");
        }
        if (template == null) {
            throw new NullPointerException("template is null");
        }

        StringBuffer result = new StringBuffer();

        String ifRegex = "[ ]*--#if (.+)\n([\\d\\D]+?)[ ]*--#endif[ ]*\n?";
        Matcher matcher = Pattern.compile(ifRegex).matcher(template);
        while (matcher.find()) {
            String attrName = matcher.group(1);
            String body = matcher.group(2);
            Object attrValue = getAttrValue(dataModels, attrName);
            if (attrValue != null) {
                matcher.appendReplacement(result, body);
            } else {
                matcher.appendReplacement(result, "");
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static Object getAttrValue(Object[] dataModels, String attrName) {
        boolean found = false;
        Object value = null;

        for (Object dataModel : dataModels) {
            if (dataModel instanceof Map) {
                Map map = (Map) dataModel;
                if (map.containsKey(attrName)) {
                    value = map.get(attrName);
                    found = true;
                    break;
                }
            }

            Map<Field, Method> fieldMethodMap = getFieldList(dataModel.getClass());
            Field field = null;
            Method getterMethod = null;
            for (Map.Entry<Field, Method> entry : fieldMethodMap.entrySet()) {
                if (entry.getKey().getName().equals(attrName)) {
                    field = entry.getKey();
                    getterMethod = entry.getValue();
                    break;
                }
            }

            if (field != null) {
                found = true;
                if (getterMethod != null) {// 如果 getter 存在，就执行 getter 方法取值
                    try {
                        value = getterMethod.invoke(dataModel);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);// 执行 getter 方法出错，向上抛异常
                    }
                } else {// 否则直接从 field 取值
                    field.setAccessible(true);
                    try {
                        value = field.get(dataModel);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException("attribute '" + attrName + "' in template is not defined in dataModel");
        } else {
            return value;
        }
    }

    private static Map<String, Map<Field, Method>> fieldCacheMap = new HashMap<>();

    /**
     * 获取指定类的 declaredFields 以及对应的 getterMethods
     * <p>
     * 注意：返回的结果包括指定类的父类（以及父类的父类，到Object为止，不包括Object）的 fields 以及对应的 getter
     */
    public static Map<Field, Method> getFieldList(Class cls) {
        Map<Field, Method> fieldMethodMap = fieldCacheMap.get(cls.getName());
        if (fieldMethodMap == null) {
            fieldMethodMap = new HashMap<>();
            Class<?> tempClass = cls;
            while (!tempClass.getName().equals("java.lang.Object")) {
                Field[] fields = tempClass.getDeclaredFields();
                for (Field field : fields) {
                    String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    Method getMethod = null;
                    try {
                        // 寻找 field 对应的 getter 方法，抛异常代表找不到 getter 方法
                        getMethod = tempClass.getDeclaredMethod(getMethodName);
                    } catch (NoSuchMethodException ignored) {//
                    }
                    fieldMethodMap.put(field, getMethod);
                }
                tempClass = tempClass.getSuperclass();
            }
            fieldCacheMap.put(cls.getName(), fieldMethodMap);
        }
        return fieldMethodMap;
    }

}
