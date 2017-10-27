package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.ConfigUtil;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.ZipUtil;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import static com.wangrj.java_lib.java_program.CreateJavaLibJar.Config.javaLibJarPath;
import static com.wangrj.java_lib.java_program.CreateJavaLibJar.Config.javaLibSourceDir;


/**
 * by wangrongjun on 2017/6/15.
 */

public class CreateJavaLibJar {

    public static void main(String[] args) {

        ConfigUtil.read(Config.class, "config.txt", true);
        if (!new File(javaLibJarPath).exists() || !new File(javaLibSourceDir).exists()) {
            System.out.println("please input java_lib.jar path and java_lib source dir !!!");
            return;
        }

        ZipUtil.uncompress(javaLibJarPath, "java_lib" + File.separator);

        FileUtil.findChildrenUnderDir(new File(javaLibSourceDir), new FileFilter() {
            @Override
            public boolean accept(File file) {
                String javaFileFromPath = file.getAbsolutePath();
                String javaFileToPath = "java_lib" + File.separator +
                        file.getAbsolutePath().replace(javaLibSourceDir, "");
                System.out.println("from: " + javaFileFromPath);
                System.out.println("to:   " + javaFileToPath);
                System.out.println();
                try {
                    FileUtil.copy(javaFileFromPath, javaFileToPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        ZipUtil.compress("java_lib", "java_lib_new.jar");

    }

    public static class Config {
        static String javaLibJarPath = "C:\\IDE\\android-studio-project\\MyLib\\java_lib\\eq\\libs\\java_lib.jar";
        static String javaLibSourceDir = "C:\\IDE\\android-studio-project\\MyLib\\java_lib\\src\\main\\java\\";
    }

}
