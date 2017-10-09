package com.wangrg.java_lib.java_util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FileUtil {

    /**
     * 递归删除非空文件夹
     */
    public static void deleteDir(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        if (!dir.isDirectory()) {
            dir.delete();
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }

        dir.delete();
    }

    public static boolean delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static void copy(String fromPath, String toPath) throws IOException {
        copy(new File(fromPath), new File(toPath));
    }

    public static void copy(File fromFile, File toFile) throws IOException {
        FileInputStream fis = new FileInputStream(fromFile);
        FileOutputStream fos = new FileOutputStream(toFile);

        FileChannel fromChannel = fis.getChannel();
        FileChannel toChannel = fos.getChannel();

        fromChannel.transferTo(0, fromChannel.size(), toChannel);

        fromChannel.close();
        toChannel.close();
        fis.close();
        fos.close();

    }

    /**
     * 递归复制指定目录及其以下的所有文件到另一个目录之下。如E:/a/这个文件夹复制到F:/test/下
     * 则 copyDir(new File("E:/a/"), new File("E:/test/"));
     */
    public static void copyDir(File fromDir, File toDir) throws IOException {
        if (fromDir == null || !fromDir.exists()) {
            return;
        }
        File nextToDir = new File(toDir, fromDir.getName());
        if (!fromDir.isDirectory()) {//若不是目录
            copy(fromDir, nextToDir);
            return;
        }

        nextToDir.mkdirs();

        File[] files = fromDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    copyDir(file, nextToDir);
                } else {
                    copy(file, new File(nextToDir, file.getName()));
                }
            }
        }

    }


    public static Object readObject(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            ois.close();
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    public static void writeObject(Object object, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
    }

    /**
     * @return 文件不存在，返回null。文件存在但无内容，返回""。
     */
    public static String read(File textFile) {
        if (!textFile.exists()) {
            return null;
        }
        try {
            return StreamUtil.readInputStream(new FileInputStream(textFile), null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 文件不存在，返回null。文件存在但无内容，返回""。
     */
    public static String read(String filePath) {
        return read(filePath, "utf-8");
    }

    public static String read(String filePath, String charset) {
        File textFile = new File(filePath);
        if (!textFile.exists()) {
            return null;
        }
        try {
            return StreamUtil.readInputStream(new FileInputStream(textFile), charset);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<String> readLines(String filePath) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        isr.close();
        return lines;
    }

    public static void write(String content, String path) throws IOException {
        write(content, path, "utf-8");
    }

    public static void write(String content, String path, String charset) throws IOException {

        if (TextUtil.isEmpty(path)) {
            throw new IOException("filePath is null");
        }

        if (content == null) {
            content = "";
        }

        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }

        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(path), charset);
        osw.write(content);
        osw.flush();
        osw.close();
//        FileWriter fw = new FileWriter(path);
//        fw.write(content);
//        fw.flush();
//        fw.closeAndOpenMainView();
    }

    public static boolean mkdirs(String dir) {
        return new File(dir).mkdirs();
    }

    public static boolean mkdirs(String parentDir, String dirName) {
        return new File(parentDir, dirName).mkdirs();
    }

    /**
     * 递归获取指定目录下所有符合要求的文件和文件夹
     */
    public static void findChildrenUnderDir(File file, List<File> fileList, FileFilter filter) {
        if (!file.exists()) {
            return;
        }

        if (fileList == null) {
            fileList = new ArrayList<>();
        }

        if (filter.accept(file)) {
            fileList.add(file);
        }

        File[] fileUnderCurrentDirList = file.listFiles();
        if (fileUnderCurrentDirList != null) {
            for (File fileUnderCurrentDir : fileUnderCurrentDirList) {
                findChildrenUnderDir(fileUnderCurrentDir, fileList, filter);
            }
        }

    }

    /**
     * 迭代获取指定目录下所有符合要求的文件和文件夹
     */
    public static List<File> findChildrenUnderDir(File rootFile, FileFilter filter) {
        if (rootFile == null || !rootFile.exists()) {
            return null;
        }
        List<File> fileList = new ArrayList<>();
        Stack<File> stack = new Stack<>();
        stack.push(rootFile);
        while (!stack.empty()) {
            File currentFile = stack.pop();
            if (filter.accept(currentFile)) {
                fileList.add(currentFile);
            }
            File[] files = currentFile.listFiles();
            if (files != null) {
                for (int i = files.length - 1; i >= 0; i--) {
                    stack.push(files[i]);
                }
            }
        }
        return fileList;
    }

}
