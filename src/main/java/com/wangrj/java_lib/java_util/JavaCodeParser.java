package com.wangrj.java_lib.java_util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/11/18.
 */
public class JavaCodeParser {

    private static class Item {
        String sign;// 注释之间的分隔标记，比如 class User / String username
        String name;// 类名或属性名，比如 User / username

        Item(String sign, String name) {
            this.sign = sign;
            this.name = name;
        }
    }

    /**
     * 可以获取属性（类）前面定义的文档注释。如果属性（类）前面有多个文档注释，取最后一个。
     * 可以获取属性（类）前面定义的单行注释或者后面定义的单行注释（后面定义的话，属性名和注释必须在同一行）。
     *
     * @return 键为属性（类）名，值为注释内容
     */
    public static Map<String, String> getDocument(Class cls, String javaCode) {
        Map<String, String> map = new HashMap<>();

        if (javaCode == null) {
            throw new RuntimeException("javaCode is null");
        }

        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item("class " + cls.getSimpleName(), cls.getSimpleName()));
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            String sign = field.getType().getSimpleName() + " " + field.getName() + ";";
            itemList.add(new Item(sign, field.getName()));
        }

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            // 匹配 "// ..." 或 "/** ... */"
            String commentRegex = "[\\d\\D]+(/\\*\\*([\\d\\D]*)\\*/|//(.+))[\\d\\D]+";
            if (i == 0) {// 获取类名注释的regex
                commentRegex = commentRegex + item.sign;
            } else {// 获取属性名注释的regex
                commentRegex = itemList.get(i - 1).sign + commentRegex + item.sign;
            }

            Pattern pattern = Pattern.compile(commentRegex);
            Matcher matcher = pattern.matcher(javaCode);
            if (matcher.find()) {
                String comment;
                if (matcher.group(3) != null) {// 匹配了单行注释
                    comment = matcher.group(3).trim();
                } else {// 匹配了多行注释
                    comment = matcher.group(2).
                            replace("\r", "").
                            replace("\n", "").
                            replace("* <p>", "").
                            replace(" * ", "").
                            trim();
                }
                map.put(item.name, comment);

            } else {// 没匹配到，有可能本身就没有注释，也有可能是单行注释并写在属性名后面（与属性同一行）
                commentRegex = "(.*)//(.+)";
                commentRegex = item.sign + commentRegex;
                pattern = Pattern.compile(commentRegex);
                matcher = pattern.matcher(javaCode);
                if (matcher.find()) {
                    String comment = matcher.group(2).trim();
                    map.put(item.name, comment);
                }
            }
        }

        return map;
    }

}
