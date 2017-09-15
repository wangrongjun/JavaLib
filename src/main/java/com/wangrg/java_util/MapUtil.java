package com.wangrg.java_util;

import java.util.Iterator;
import java.util.Map;

/**
 * Java中HashMap遍历的两种方式原文地址:
 * http://www.javaweb.cc/language/java/032291.shtml
 * HashMap的遍历有两种常用的方法，使用keyset及entryset遍历，但遍历速度是有差别的。entryset效率高。
 */
public class MapUtil {
    /**
     * @return 返回如user=wang \n pass=123的字符串
     */
    public static String iterator(Map map) {
        String result = "";
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            result += entry.getKey() + "=";
            result += entry.getValue() + "\n";
        }
        return result;
    }

}
