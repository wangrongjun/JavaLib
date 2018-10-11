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

    private static Map<String, List<Field>> fieldCacheMap = new HashMap<>();

    private static Object getAttrValue(Object dataModel, String attrName) {
        if (dataModel instanceof Map) {
            Map map = (Map) dataModel;
            if (!map.containsKey(attrName)) {
                throw new IllegalArgumentException("attribute '" + attrName + "' in template is not defined in dataModel");
            } else {
                return map.get(attrName);
            }
        }

        List<Field> fieldList = fieldCacheMap.get(dataModel.getClass().getName());
        if (fieldList == null) {
            fieldList = new ArrayList<>();
            Class tempClass = dataModel.getClass();
            // 循环获取父类（除了Object），并保存父类的属性
            while (!tempClass.getName().equals("java.lang.Object")) {
                fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
                tempClass = tempClass.getSuperclass();
            }
            fieldCacheMap.put(dataModel.getClass().getName(), fieldList);
        }

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

}
