package com.wangrj.java_lib.constant;

/**
 * by wangrongjun on 2017/11/6.
 */
public class JavaLibConstant {

    public static String srcDir() {
        return "C:\\IDE\\ideaIU-project\\JavaLib\\src\\main\\java\\";
    }

    public static String classDir(Class cls) {
        return "C:\\IDE\\ideaIU-project\\JavaLib\\src\\main\\java\\" +
                cls.getPackage().getName().replace(".", "\\") +
                "\\";
    }

}
