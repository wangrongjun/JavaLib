package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 搜索指定目录下包含指定关键字的所有文本文件
 * <p>
 * by wangrongjun on 2018/8/3.
 */
public class SearchFile extends JavaProgram {

    @Param("查询目录")
    @FileIsDirectory
    private String folderPath;
    @Param("搜索关键字")
    @NotBlank
    private String searchWord;

    public static void main(String[] args) {
        SearchFile searchFile = new SearchFile();
        searchFile.initParam();

        System.out.println("1.folderPath: " + searchFile.folderPath);
        System.out.println("2.searchWord: " + searchFile.searchWord);
    }

    public static void main1(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入查询的目录：");
        File folder = new File(scanner.next());
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("错误：目录不存在或者不是目录");
            return;
        }
        System.out.println("请输入搜索关键字：");
        String searchWord = scanner.next();
        scanner.close();

        System.out.println("获取文件列表...");
        List<File> children = FileUtil.findChildrenUnderDir(folder, null);
        System.out.println("搜索中...");

        List<String> searchRecordList = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            File file = children.get(i);
            showText((i + 1) + " / " + children.size());
            if (file.isDirectory()) {
                continue;
            }
            String searchRecord = "";
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.contains(searchWord)) {
                    searchRecord += "    " + lineNumber + ". " + line + "\r\n";
                }
            }
            br.close();
            fr.close();
            if (searchRecord.length() > 0) {// 如果这个文件找到searchWord
                searchRecord = file.getAbsolutePath() + "\r\n\r\n" + searchRecord;
                searchRecordList.add(searchRecord);
            }
        }

        System.out.println();
        if (searchRecordList.size() > 0) {
            System.out.println("搜索完成，以下是搜索记录：\r\n");
        } else {
            System.out.println("搜索完成，无搜索记录。");
        }

        for (int i = 0; i < searchRecordList.size(); i++) {
            String searchRecord = searchRecordList.get(i);
            System.out.println((i + 1) + ". " + searchRecord);
        }
    }

    private static void showText(String text) {
        System.out.print(text);
        for (int i = 0; i < text.length(); i++) {
            System.out.print("\b");
        }
    }

}
