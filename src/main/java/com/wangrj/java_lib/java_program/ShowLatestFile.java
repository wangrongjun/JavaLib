package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.math.sort.SortHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * by wangrongjun on 2017/7/4.
 */

public class ShowLatestFile {

    private static final String PROPERTIES_FILE_NAME = "ShowLatestFile.properties";
    private static final String FILTER_REGEX_KEY_NAME = "filterRegex";
    private static final String OUTPUT_PATH_KEY_NAME = "outputPath";

    public static void main(String[] args) throws IOException {

        System.out.println("------------ 按照修改日期对文件排序 -----------");

        // 读取或创建配置文件
        String filterRegex = null;
        String outputPath = null;
        File propertiesFile = new File(PROPERTIES_FILE_NAME);
        if (!propertiesFile.exists()) {
            FileWriter fileWriter = new FileWriter(propertiesFile);
            fileWriter.write("#filterRegex=^.+\\\\.exe$\r\n");
            fileWriter.write("#filterRegex=^.+\\\\([\\\\d]+\\\\)\\\\.[^.]+$\r\n");
            fileWriter.write("#outputPath=output.txt");
            fileWriter.flush();
            fileWriter.close();
            System.out.println("已创建配置文件：" + propertiesFile.getAbsolutePath());
        } else {
            Properties properties = new Properties();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(propertiesFile), "utf-8");
            properties.load(isr);
            isr.close();
            System.out.println("已读取配置文件：" + propertiesFile.getAbsolutePath());
            filterRegex = properties.getProperty(FILTER_REGEX_KEY_NAME);
            if (TextUtil.isNotBlank(filterRegex)) {
                System.out.println("根据正则表达式查找文件：" + filterRegex);
            }
            outputPath = properties.getProperty(OUTPUT_PATH_KEY_NAME);
            if (TextUtil.isNotBlank(outputPath)) {
                System.out.println("结果将会输出到文件：" + outputPath);
            }
        }

        System.out.println("请输入目录：");
        Scanner scanner = new Scanner(System.in);
        File dir = new File(scanner.nextLine());
        scanner.close();

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("错误！ 1.路径含有空格  2.目录不存在  3.不是目录");
            return;
        }

        // 搜索文件
        List<File> fileList;
        if (TextUtil.isNotBlank(filterRegex)) {
            String finalFilterRegex = filterRegex;
            fileList = FileUtil.findChildrenUnderDir(dir, file -> file.getName().matches(finalFilterRegex));
        } else {
            fileList = FileUtil.findChildrenUnderDir(dir, null);
        }

        SortHelper.sortMerge(fileList, (file1, file2) -> file1.lastModified() < file2.lastModified() ? 1 : -1);

        StringBuilder output = new StringBuilder();
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        for (File file : fileList) {
            String time = sdf.format(new Date(file.lastModified()));
            String type = file.isDirectory() ? "目录" : "文件";
            output.append(type).append("  ").append(time).append("  ").
                    append(file.getAbsolutePath().replace(dir.getAbsolutePath(), "")).
                    append("\r\n");
        }

        if (TextUtil.isNotBlank(outputPath)) {
            FileUtil.write(output.toString(), outputPath);
            System.out.println("结果已经输出到文件：" + outputPath);
        } else {
            System.out.println(output.toString());
        }

    }

}
