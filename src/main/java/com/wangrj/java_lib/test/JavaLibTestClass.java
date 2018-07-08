package com.wangrj.java_lib.test;

import java.io.File;

public class JavaLibTestClass {

    public static void main(String[] args) {
        String s = "abc/aaabc\\";
        String s1 = s.replaceFirst("abc[/|\\\\]", "def\\\\");
        System.out.println(s1);
    }

}
