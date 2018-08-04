package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Param("是否忽略大小写（y/n）")
    @MatchOne({"y", "n", "Y", "N"})
    private String ignoreCase;

    public static void main(String[] args) throws IOException {
        SearchFile searchFile = new SearchFile();
        searchFile.initParam(args);
        searchFile.runProgram();
    }

    @Override
    protected void runProgram() throws IOException {
        println("获取文件列表...");
        List<File> children = FileUtil.findChildrenUnderDir(new File(folderPath), null);
        println("搜索中...");

        List<String> searchRecordList = new ArrayList<>();
        for (int i = 0; i < children.size(); i++) {
            File file = children.get(i);
            printlnDelay(100, true, (i + 1) + " / " + children.size());
            if (file.isDirectory()) {
                continue;
            }
            StringBuilder searchRecord = new StringBuilder();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (ignoreCase.equalsIgnoreCase("y")) {// 大小写不敏感
                    if (line.toLowerCase().contains(searchWord.toLowerCase())) {
                        searchRecord.append("    ").append(lineNumber).append(". ").append(line).append("\r\n");
                    }
                } else {// 大小写敏感
                    if (line.contains(searchWord)) {
                        searchRecord.append("    ").append(lineNumber).append(". ").append(line).append("\r\n");
                    }
                }
            }
            br.close();
            fr.close();
            if (searchRecord.length() > 0) {// 如果这个文件找到searchWord
                searchRecord.insert(0, file.getAbsolutePath() + "\r\n\r\n");
                searchRecordList.add(searchRecord.toString());
            }
        }

        println();
        println("搜索完成，共有 " + searchRecordList.size() + " 条搜索记录。");
        println();

        for (int i = 0; i < searchRecordList.size(); i++) {
            String searchRecord = searchRecordList.get(i);
            println((i + 1) + ". " + searchRecord);
        }
    }

}
