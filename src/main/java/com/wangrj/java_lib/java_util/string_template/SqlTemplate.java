package com.wangrj.java_lib.java_util.string_template;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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

        String ifRegex = "[ ]*--#if (.+)\n([\\d\\D]+?)[ ]*--#endif[ ]*\n";
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

            List<Field> fieldList = getFieldList(dataModel.getClass());
            Field field = fieldList.stream().filter(f -> f.getName().equals(attrName)).findFirst().orElse(null);
            if (field != null) {
                found = true;
                field.setAccessible(true);
                try {
                    String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    try {
                        // 寻找 field 对应的 getter 方法，找到就调用 getter 方法以获取 field 的值
                        Method getMethod = dataModel.getClass().getDeclaredMethod(getMethodName);
                        value = getMethod.invoke(dataModel);
                    } catch (NoSuchMethodException e) {// 抛异常代表找不到 getter 方法，就直接赋值
                        value = field.get(dataModel);
                    } catch (InvocationTargetException e) {// 执行 getter 方法出错，向上抛异常
                        throw new IllegalStateException(e);
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
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

    private static Map<String, List<Field>> fieldCacheMap = new HashMap<>();

    /**
     * 获取指定类的 DeclaredFields，同时返回指定类的父类（以及父类的父类，到Object为止，不包括Object）的 DeclaredFields
     */
    public static List<Field> getFieldList(Class cls) {
        List<Field> fieldList = fieldCacheMap.get(cls.getName());
        if (fieldList == null) {
            fieldList = new ArrayList<>();
            Class tempClass = cls;
            while (!tempClass.getName().equals("java.lang.Object")) {
                fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                tempClass = tempClass.getSuperclass();
            }
            fieldCacheMap.put(cls.getName(), fieldList);
        }
        return fieldList;
    }

}
