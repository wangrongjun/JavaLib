package com.wangrj.java_lib.test;

import java.io.File;

public class JavaLibTestClass {

    public static void main(String[] args) {
        System.out.println(new File("a").getAbsolutePath());
        if (args.length == 0) {
            System.out.println("no args");
        }
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            System.out.println("arg" + (i + 1) + ": " + arg);
        }
    }

}
