package com.wangrj.java_lib.test;

import com.wangrj.java_lib.java_util.FileUtil;

public class JavaLibTestClass {

    public static void main(String[] args) {
        String s = FileUtil.read("E:/Test/a.txt");
        System.out.println(s);
        System.out.println("---------------------------");
        s = s.replace("\r", "\\r").replace("\n", "\\n").replace("\t", "\\t");
        System.out.println(s);
    }

}
