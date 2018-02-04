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
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/7/4.
 */

public class ShowLatestFile {

    private static final String FILTER_FILE_NAME = "filters.properties";
    private static final String FILTER_REGEX_KEY_NAME = "filterRegex";

    public static void main(String[] args) throws IOException {

        System.out.println("------------ 按照修改日期对文件排序 -----------");

        // 读取/创建filter配置文件
        String regex = null;
        File filterFile = new File(FILTER_FILE_NAME);
        if (!filterFile.exists()) {
            FileWriter fileWriter = new FileWriter(filterFile);
            fileWriter.write("#filterRegex=^.+\\\\.exe$\r\n");
            fileWriter.write("#filterRegex=^.+\\\\([\\\\d]+\\\\)\\\\.[^.]+$\r\n");
            fileWriter.flush();
            fileWriter.close();
            System.out.println("已创建配置文件：" + filterFile.getAbsolutePath());
        } else {
            Properties properties = new Properties();
            InputStreamReader isr = new InputStreamReader(new FileInputStream(filterFile), "utf-8");
            properties.load(isr);
            isr.close();
            System.out.println("已读取配置文件：" + filterFile.getAbsolutePath());
            regex = properties.getProperty(FILTER_REGEX_KEY_NAME);
            if (TextUtil.isNotBlank(regex)) {
                System.out.println("根据正则表达式查找文件：" + regex);
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
        if (TextUtil.isNotBlank(regex)) {
            String finalRegex = regex;
            fileList = FileUtil.findChildrenUnderDir(dir, file -> file.getName().matches(finalRegex));
        } else {
            fileList = FileUtil.findChildrenUnderDir(dir, null);
        }

        SortHelper.sortMerge(fileList, (file1, file2) -> file1.lastModified() < file2.lastModified() ? 1 : -1);

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        for (File file : fileList) {
            String time = sdf.format(new Date(file.lastModified()));
            String type = file.isDirectory() ? "目录" : "文件";
            System.out.println(type + "  " + time + "  " + file.getAbsolutePath().replace(dir.getAbsolutePath(), ""));
        }

    }

}
