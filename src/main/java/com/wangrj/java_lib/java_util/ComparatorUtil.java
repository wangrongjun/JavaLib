package com.wangrj.java_lib.java_util;

import java.util.Comparator;

/**
 * by wangrongjun on 2017/9/3.
 */
public class ComparatorUtil {

    public final static Comparator<Integer> IntegerComparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 < o2 ? -1 : (o1 > o2 ? 1 : 0);
        }
    };

    public final static Comparator<String> StringComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            int i = o1.compareTo(o2);
            return i < 0 ? -1 : (i > 0 ? 1 : 0);
        }
    };

}
