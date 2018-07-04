package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CopyFilesInFolder {

    private static String filesTextFileName = "files.txt";
    private static String outputDir = "output" + File.separator;

    public static void main(String[] args) throws IOException {
        if (!new File(filesTextFileName).exists() || new File(filesTextFileName).isDirectory()) {
            System.out.println("Fail to load text file: files.txt");
            return;
        }

        FileUtil.deleteDir(new File(outputDir));
        if (!new File(outputDir).mkdirs()) {
            System.out.println("Fail to create output folder");
            return;
        }

        System.out.println("** Error description **");
        System.out.println("Error 1 : Not exists");
        System.out.println("Error 2 : Failed create dir");
        System.out.println("Error 3 : Failed create parent dir");
        System.out.println("\r\n ======== Status ========\r\n");

        int succeedCount = 0;
        int failCount = 0;
        List<String> lines = Files.readAllLines(Paths.get(filesTextFileName));
        lines = lines.stream().sorted().collect(Collectors.toList());
        for (String line : lines) {
            if (TextUtil.isBlank(line)) {
                continue;
            }
            File file = new File(line);
            if (!file.exists()) {
                failCount++;
                System.out.println("Error 1: " + line);
                continue;
            }
            if (file.isDirectory()) {
                if (!new File(outputDir + line).mkdirs()) {
                    failCount++;
                    System.out.println("Error 2: " + line);
                }
            } else {
                File toDir = new File(outputDir + TextUtil.getTextExceptLastSlash(line));
                toDir.mkdirs();
                File toFile = new File(outputDir + line);
                FileUtil.copy(file, toFile);
                succeedCount++;
                System.out.println("Succeed: " + line);
            }
        }

        System.out.println("\r\n ======== Result ========\r\n");
        System.out.println("succeed: " + succeedCount + "   fail: " + failCount + "\r\n");
    }

}
