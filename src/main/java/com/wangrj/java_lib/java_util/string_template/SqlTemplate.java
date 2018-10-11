package com.wangrj.java_lib.java_util.string_template;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理 sql 中模版指令（--#if attribute 和 --#endif）包裹的内容。
 * <p>
 * 处理方式：根据 attribute 是否为空，来决定输出的结果是否包含模版指令包裹的内容。
 */
public class SqlTemplate {

    public static String process(Object dataModel, String template) {
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
            return map.get(attrName);
        } else {
            throw new RuntimeException("Not Support");
        }
    }

}
