package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.math.sort.SortHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/7/4.
 */

public class ShowLatestFile {

    public static void main(String[] args) {

        System.out.println("------------ 按照修改日期对文件排序 -----------");
        System.out.println("请输入目录：");
        Scanner scanner = new Scanner(System.in);
        File dir = new File(scanner.next());
        scanner.close();

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("错误！ 1.路径含有空格  2.目录不存在  3.不是目录");
            return;
        }

        List<File> fileList;
        if (args != null && args.length == 1) {
            String regex = args[0];
            System.out.println("根据正则表达式查找文件：" + regex);
            Pattern pattern = Pattern.compile(regex);
            fileList = FileUtil.findChildrenUnderDir(dir, file -> pattern.matcher(file.getName()).matches());
        } else {
            fileList = FileUtil.findChildrenUnderDir(dir, null);
        }

        SortHelper.sortMerge(fileList, (file1, file2) -> file1.lastModified() < file2.lastModified() ? 1 : -1);

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        for (File file : fileList) {
            String time = sdf.format(new Date(file.lastModified()));
            String type = file.isDirectory() ? "目录" : "文件";
            System.out.println(type + "  " + time + "  " + file.getAbsolutePath());
        }

    }

}
