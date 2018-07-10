package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * 拷贝指定目录下的目录结构
 * <p>
 * by wangrongjun on 2018/7/10.
 */
public class GetFolder {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("need param folder name");
            return;
        }
        File dir = new File(args[0]);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("not exists or not folder: " + dir.getAbsolutePath());
            return;
        }

        List<File> dirChildren = FileUtil.findChildrenUnderDir(dir, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File dirChild : dirChildren) {
            String newPath = dirChild.getPath().replaceFirst(dir.getName(), dir.getName() + "-new");
            new File(newPath).mkdirs();
        }
        System.out.println("目录结构拷贝成功");
    }

}
