package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.TextUtil;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * by wangrongjun on 2018/8/4.
 */
public class JavaProgram {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface Param {
        String value();// paramName
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface NotBlank {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface FileExists {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface FileIsDirectory {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface FileIsNotDirectory {
    }

    protected void initParam() {
        Field[] fields = this.getClass().getDeclaredFields();
        Scanner scanner = new Scanner(System.in);
        for (Field field : fields) {
            field.setAccessible(true);
            Param param = field.getAnnotation(Param.class);
            if (param == null) {
                continue;
            }
            System.out.println("请输入" + param.value() + "：");
            String paramValue = scanner.next();
            Annotation[] annotations = field.getDeclaredAnnotations();
            String errorMessage = validateParamValue(annotations, param.value(), paramValue);
            if (errorMessage != null) {
                System.err.println("错误：" + errorMessage);
                System.exit(0);// 如果参数校验失败，就提示错误原因并直接退出程序
            }
            field.setAccessible(true);
            try {
                field.set(this, paramValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }

    private String validateParamValue(Annotation[] annotations, String paramName, String paramValue) {
        for (Annotation annotation : annotations) {
            switch (annotation.annotationType().getSimpleName()) {
                case "NotBlank":
                    if (TextUtil.isBlank(paramValue)) {
                        return paramName + " 不能为空";
                    }
                    break;
                case "FileExists":
                    File file = new File(paramValue);
                    if (!file.exists()) {
                        return "文件不存在：" + paramValue;
                    }
                    break;
                case "FileIsDirectory":
                    file = new File(paramValue);
                    if (!file.exists() || !file.isDirectory()) {
                        return "目录不存在或者不是目录";
                    }
                    break;
                case "FileIsNotDirectory":
                    file = new File(paramValue);
                    if (!file.exists() || file.isDirectory()) {
                        return "文件不存在或者不是文件";
                    }
                    break;
            }
        }
        return null;
    }

    protected void println() {
        System.out.println();
    }

    protected void println(String s) {
        System.out.println(s);
    }

    protected void printlnError(String s) {
        System.err.println(s);
    }

    private static void printForRefresh(String s) {
        System.out.print(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.print("\b");
        }
    }

}
