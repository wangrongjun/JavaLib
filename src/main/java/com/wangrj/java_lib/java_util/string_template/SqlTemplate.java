package com.wangrj.java_lib.java_util.string_template;

import com.sun.istack.internal.NotNull;

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
     * @param dataModels 可以设置多个dataModel，如果在第一个dataModel中找不到attribute，会在第二个找，依此类推。
     * @throws IllegalArgumentException if attribute in template is not defined in any of dataModels
     */
    public static String process(String template, Object... dataModels) {
        if (dataModels == null || dataModels.length == 0) {
            throw new NullPointerException("dataModel is null");
        }
        if (template == null) {
            throw new NullPointerException("template is null");
        }

        StringBuffer result = new StringBuffer();

        String ifRegex = "[ ]*--#if (.+)\n([\\d\\D]+?)\n?[ ]*--#endif[ ]*\n?";
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

        if (Pattern.compile("--#if .+").matcher(result.toString()).find()) {// 如果处理后的结果，还有模版指令，就报错
            throw new IllegalStateException("Syntax Error");
        }

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

            Map<Field, Object> fieldAndValueMap = getFieldAndValueMap(dataModel);
            for (Map.Entry<Field, Object> entry : fieldAndValueMap.entrySet()) {
                Field field = entry.getKey();
                if (field.getName().equals(attrName)) {
                    found = true;
                    value = entry.getValue();
                    break;
                }
            }
        }

        if (!found) {
            throw new IllegalArgumentException("attribute '" + attrName + "' in template is not defined in any of dataModels");
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
    public static Map<Field, Method> getFieldAndGetterMethodMap(@NotNull Class cls) {
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
                    } catch (NoSuchMethodException ignored) {
                    }
                    fieldMethodMap.put(field, getMethod);
                }
                tempClass = tempClass.getSuperclass();
            }
            fieldCacheMap.put(cls.getName(), fieldMethodMap);
        }
        return fieldMethodMap;
    }

    /**
     * 获取实体对象的 declaredFields 以及对应的 value
     * <p>
     * 注意：返回的结果包括指定类的父类（以及父类的父类，到Object为止，不包括Object）的 fields 以及对应的 value。
     * 其中 value 默认使用 getter 获取。如果没有 getter，直接从属性获取。
     */
    public static Map<Field, Object> getFieldAndValueMap(@NotNull Object entity) {
        Map<Field, Object> fieldAndValueMap = new HashMap<>();

        Map<Field, Method> fieldMethodMap = getFieldAndGetterMethodMap(entity.getClass());
        for (Map.Entry<Field, Method> entry : fieldMethodMap.entrySet()) {
            Field field = entry.getKey();
            Method getterMethod = entry.getValue();
            Object value;
            if (getterMethod != null) {// 如果 getter 存在，就执行 getter 方法取值
                try {
                    value = getterMethod.invoke(entity);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new IllegalStateException(e);// 执行 getter 方法出错，向上抛异常
                }
            } else {// 否则直接从 field 取值
                field.setAccessible(true);
                try {
                    value = field.get(entity);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
            fieldAndValueMap.put(field, value);
        }
        return fieldAndValueMap;
    }

}
