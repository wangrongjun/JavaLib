package com.wangrg.java_util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * http://www.open-open.com/lib/view/open1381641653833.html#articleHeader4
 * 使用java.util.zip包压缩和解压缩文件
 */
public class ZipUtil {

    /**
     * 压缩文件或文件夹
     * 示例：String compressDir="E:/test"; ZipUtil.compress(compressDir, compressDir + ".zip");
     * 注意，受java.util.zip框架影响，若有空的文件夹要压缩，
     * 压缩文件中的该文件夹下将自动生成空的abc.txt文件。
     *
     * @param compressPath 既可以是文件，也可以是文件夹。即使是文件夹也不必在末尾加/
     */
    public static void compress(String compressPath, String zipFilePath) {
        try {
            new File(zipFilePath).delete();
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File file = new File(compressPath);

            if (file.isDirectory()) {
                String baseDir = file.getAbsolutePath();
                compressDir(baseDir, new File(baseDir), zos);
            } else {
                compressFile(file, file.getName(), zos);
            }

            zos.close();
            fos.close();

            System.out.println("zip finish! file: " + zipFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFile(File file, String pathInZip, ZipOutputStream zos) throws IOException {
        zos.putNextEntry(new ZipEntry(pathInZip));
        FileInputStream fis = new FileInputStream(file);
        int len;
        byte[] buf = new byte[1024];
        while ((len = fis.read(buf)) != -1) {
            zos.write(buf, 0, len);
        }
        fis.close();
//        System.out.println("zip successfully: " + file.getAbsoluteFile());
    }

    private static void compressDir(String baseDir, File dir, ZipOutputStream zos) throws IOException {
        File[] files = dir.listFiles();
        if (files.length == 0) {
            String dirInZip = dir.getAbsolutePath().replace(baseDir, "") + File.separator + "abc.txt";
            zos.putNextEntry(new ZipEntry(dirInZip));
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                compressDir(baseDir, file, zos);
            } else {
                String pathInZip = dir.getAbsolutePath().replace(baseDir, "") +
                        File.separator + file.getName();
                compressFile(file, pathInZip, zos);
            }

        }
    }

    /**
     * zip文件解压缩
     *
     * @param unzipDir 压缩文件下的所有文件及文件夹解压到unzipDir目录下，unzipDir会自动创建。
     */
    public static void uncompress(String zipFilePath, String unzipDir) {
        try {
            new File(unzipDir).mkdirs();
            unzipDir = new File(unzipDir).getAbsolutePath();

            ZipFile zipFile = new ZipFile(zipFilePath);
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                String path = entry.getName();
                String fileName = TextUtil.getTextAfterLastSlash(path);
                String dir = unzipDir + File.separator + path.replace(fileName, "");
                System.out.println(dir);
                new File(dir).mkdirs();

                if (entry.isDirectory()) {
                    continue;
                }

                InputStream is = zipFile.getInputStream(entry);
                FileOutputStream fos = new FileOutputStream(dir + fileName);

                int len;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.close();
                is.close();
//                System.out.println("unzip successfully: " + dir + fileName);
            }

            zis.close();
            fis.close();

            System.out.println("unzip finish! dir: " + unzipDir + File.separator);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
