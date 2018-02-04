package com.wangrj.java_lib.test;

import java.io.File;
import java.io.FileNotFoundException;

public class JavaLibTestClass {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\天翼云盘同步盘\\Bamboo公司的文件\\Bamboo公司的文件\\开发环境\\SQL Server");
        System.out.println(file.getAbsolutePath());
        System.out.println(file.exists());
        System.out.println(file.isDirectory());
    }

}
