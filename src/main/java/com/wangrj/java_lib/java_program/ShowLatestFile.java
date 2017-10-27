package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.math.sort.SortHelper;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.math.sort.SortHelper;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * by wangrongjun on 2017/7/4.
 */

public class ShowLatestFile {

    public static void main(String[] args) {

        System.out.println("------------ �����޸����ڶ��ļ����� -----------");
        System.out.println("������Ŀ¼��");
        Scanner scanner = new Scanner(System.in);
        File dir = new File(scanner.next());
        scanner.close();

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("���� 1.·�����пո�  2.Ŀ¼������  3.����Ŀ¼");
            return;
        }

        List<File> fileList = FileUtil.findChildrenUnderDir(dir, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return true;
            }
        });

        SortHelper.sortMerge(fileList, new Comparator<File>() {
            @Override
            public int compare(File entity1, File entity2) {
                return entity1.lastModified() < entity2.lastModified() ? 1 : -1;
            }
        });

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateTimeInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        for (File file : fileList) {
            String time = sdf.format(new Date(file.lastModified()));
            String type = file.isDirectory() ? "Ŀ¼" : "�ļ�";
            System.out.println(type + "  " + time + "  " + file.getAbsolutePath());
        }

    }

}
