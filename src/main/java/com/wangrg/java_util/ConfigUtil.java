package com.wangrg.java_util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;

/**
 * 只支持导入导出具有静态成员变量为int，double，String，boolean类型的配置类
 */
public class ConfigUtil {
    /**
     * 一般配置文件的后缀名
     */
    public static final String postName = "properties";

    /**
     * 把configClass中已存在的配置项写出到配置文件
     */
    public static void write(Class configClass, String outPath) {

        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(outPath));
            BufferedWriter br = new BufferedWriter(osw);

            Field[] fields = configClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object o = field.get(configClass);
                if (o != null) {
                    String value = o.toString();
                    String line = fieldName + "=" + value;
                    br.write(line);
                    br.newLine();
                }
            }

            br.flush();
            br.close();
            osw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从配置文件读取配置项并设置到configClass类的静态成员变量中
     * <p/>
     * 注意：配置文件中加了#的那一行的配置项会忽略
     *
     * @param writeDefault 若配置文件不存在，是否把configClass中已存在的配置项写出到配置文件
     */
    public static void read(Class configClass, String configFilePath, boolean writeDefault) {

        if (!new File(configFilePath).exists()) {
            if (writeDefault) {
                write(configClass, configFilePath);
            }
            return;
        }

        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(configFilePath));
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                int div = line.indexOf("=");
                if (div != -1) {
                    String name = line.substring(0, div);
                    String value = line.substring(div + 1);
                    Field field = configClass.getDeclaredField(name);
                    if (field != null) {
                        field.setAccessible(true);

                        if (field.getType().getSimpleName().contains("int")) {
                            field.set(configClass, Integer.parseInt(value));

                        } else if (field.getType().getSimpleName().contains("double")) {
                            field.set(configClass, Double.parseDouble(value));

                        } else if (field.getType().getSimpleName().contains("String")) {
                            field.set(configClass, value);

                        } else if (field.getType().getSimpleName().contains("boolean")) {
                            field.set(configClass, Boolean.valueOf(value));
                        }
                    }
                }
            }

            br.close();
            isr.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
