package com.wangrg.java_lib.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  by 王荣俊 on 2016/5/12.
 */
public class IteratorHashMap {

    public static void iterator() {
        HashMap map = new HashMap();
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
        }
    }
}
