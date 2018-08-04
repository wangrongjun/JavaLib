package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.TextUtil;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * by wangrongjun on 2018/8/4.
 */
public abstract class JavaProgram {

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
    protected @interface MatchRegex {
        String value();
    }

    /**
     * 匹配其中之一
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface MatchOne {
        String[] value();
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

    protected abstract void runProgram() throws Exception;

    /**
     * 初始化 JavaProgram 子类中注解了 @Param 的成员变量
     *
     * @param args 如果 main 方法传入的参数列表不为空，按照顺序赋值给 JavaProgram 子类中注解了 @Param 的成员变量
     */
    protected void initParam(String[] args) {
        Field[] fields = this.getClass().getDeclaredFields();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            Param param = field.getAnnotation(Param.class);
            if (param == null) {
                continue;
            }

            String paramValue;
            if (args.length > i) {// 如果运行main方法时有启动参数，就直接按顺序赋值
                paramValue = args[i];
            } else {// 否则请求用户手动输入参数
                println(param.value() + "：");
                paramValue = scanner.next();
            }

            // 校验参数的合法性。如果参数校验失败，就提示错误原因并直接退出程序
            Annotation[] annotations = field.getDeclaredAnnotations();
            String errorMessage = validateParamValue(annotations, param.value(), paramValue);
            if (errorMessage != null) {
                printlnError("错误：" + errorMessage);
                System.exit(0);
            }

            // 赋值
            try {
                field.setAccessible(true);
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
                case "MatchOne":
                    MatchOne matchOne = (MatchOne) annotation;
                    String[] values = matchOne.value();
                    boolean match = false;
                    for (String value : values) {
                        if (value.equals(paramValue)) {
                            match = true;
                        }
                    }
                    if (!match) {
                        return paramValue + " 不是 " +
                                Arrays.stream(values).collect(Collectors.joining(",")) + " 的其中之一";
                    }
                    break;
                case "MatchRegex":
                    MatchRegex matchRegex = (MatchRegex) annotation;
                    if (!Pattern.matches(matchRegex.value(), paramValue)) {
                        return paramValue + " 与正则表达式 " + matchRegex.value() + " 不匹配";
                    }
                    break;
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

    private int previousLength = 0;

    /**
     * 可以在控制台中覆盖输出而不是追加输出
     */
    protected void printForRefresh(String s) {
        for (int i = 0; i < previousLength; i++) {
            System.out.print("\b");
        }
        System.out.print(s);
        previousLength = s.length();
    }

    private long previousTime = 0;
    private String previousText = null;

    /**
     * 隔一段指定的时间才输出一次。如果设置1000毫秒，在1000毫秒内调用了2次该方法，则第一次才真正输出。
     *
     * @param delayTime   指定的时间，单位毫秒。如果小于等于0，则一定输出。
     * @param allowRepeat 是否允许连续输出重复的文本
     */
    protected void printlnDelay(int delayTime, boolean allowRepeat, String text) {
        if (delayTime <= 0) {
            System.out.println(text);
        }
        if (!allowRepeat && text.equals(previousText)) {
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime - previousTime >= delayTime) {
            System.out.println(text);
        }
        previousTime = currentTime;
        previousText = text;
    }

}
