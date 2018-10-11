package com.wangrj.java_lib.java_util.string_template;

import java.lang.reflect.Field;
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
    public static String process(Object dataModel, String template) {
        if (dataModel == null) {
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
            Object attrValue = getAttrValue(dataModel, attrName);
            if (attrValue != null) {
                matcher.appendReplacement(result, body);
            } else {
                matcher.appendReplacement(result, "");
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    public static Object getAttrValue(Object dataModel, String attrName) {
        if (dataModel instanceof Map) {
            Map map = (Map) dataModel;
            if (!map.containsKey(attrName)) {
                throw new IllegalArgumentException("attribute '" + attrName + "' in template is not defined in dataModel");
            } else {
                return map.get(attrName);
            }
        }

        List<Field> fieldList = getFieldList(dataModel.getClass());
        Field field = fieldList.stream().filter(f -> f.getName().equals(attrName)).findFirst().orElse(null);
        if (field == null) {
            throw new IllegalArgumentException("attribute '" + attrName + "' in template is not defined in dataModel");
        }
        field.setAccessible(true);
        try {
            return field.get(dataModel);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
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
