package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * by wangrongjun on 2018/7/8.
 * <p>
 * 合并两个目录，同时做备份
 */
public class MergeFolder {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("need two args: mergeFromDir and mergeToDir");
            return;
        }
        String mergeFrom = args[0];
        String mergeTo = args[1];
        if (mergeFrom.contains("/") || mergeFrom.contains("\\") || mergeTo.contains("/") || mergeTo.contains("\\")) {
            System.out.println("args can't contain '/' or '\\'");
            return;
        }

        File mergeFromDir = new File(mergeFrom);
        File mergeToDir = new File(mergeTo);
        if (!mergeFromDir.exists() || !mergeFromDir.isDirectory() || !mergeToDir.exists() || !mergeToDir.isDirectory()) {
            System.out.println("mergeFromDir and mergeToDir must be folder and exists");
            return;
        }

        List<File> mergeFromFiles = FileUtil.findChildrenUnderDir(mergeFromDir, pathname -> !pathname.isDirectory());
        for (File mergeFromFile : mergeFromFiles) {
            File mergeToFile = new File(mergeFromFile.getPath().replaceFirst(mergeFrom + "[/|\\\\]", mergeTo + File.separator));
            if (mergeToFile.exists()) {// 如果需要被覆盖的文件存在
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String backupFilePath = mergeFromFile.getPath().replaceFirst(mergeFrom + "[/|\\\\]", mergeTo + "_old_bak_" + time + File.separator);
                new File(TextUtil.getTextExceptLastSlash(backupFilePath)).mkdirs();// 备份前先创建父目录
                FileUtil.copy(mergeToFile, new File(backupFilePath));// 备份
                FileUtil.copy(mergeFromFile, mergeToFile);// 替换
            } else {// 如果需要被覆盖的文件不存在
                new File(TextUtil.getTextExceptLastSlash(mergeToFile.getPath())).mkdirs();// 复制前先创建父目录
                FileUtil.copy(mergeFromFile, mergeToFile);// 复制
            }
        }

    }

}
