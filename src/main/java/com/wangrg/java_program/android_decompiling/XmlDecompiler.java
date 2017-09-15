package com.wangrg.java_program.android_decompiling;

import com.wangrg.java_util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import test.AXMLPrinter;

/**
 * by wangrongjun on 2017/5/22.
 */
public class XmlDecompiler {

    public static void main(String[] args) throws Exception {
        File dirFile = new File(args[0]);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            throw new IOException("dir not exists or dir not a real dir");
        }
        String decompiledDir = dirFile.getAbsolutePath() + "(Compiled)" + File.separator;
        System.out.println("start copy dir to dir(Compile)");
        FileUtil.copyDir(dirFile, new File(decompiledDir));
        System.out.println("start find all xml files under dir");
        List<File> xmlFileList = FileUtil.findChildrenUnderDir(
                new File(decompiledDir),
                new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".xml");
                    }
                });
        int size = xmlFileList.size();
        for (int i = 0; i < size; i++) {
            String path = xmlFileList.get(i).getAbsolutePath();
            String decompiledXml = decompileXml(path);
            FileUtil.write(decompiledXml, path);
            System.out.println((i + 1) + "/" + size + ": " + path + "      decompile succeed!!!");
        }
        System.out.println("all xml files compiled!!!");
    }

    private static String decompileXml(String xmlFilePath) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream newPrintStream = new PrintStream(baos);
        PrintStream oldPrintStream = System.out;
        System.setOut(newPrintStream);
        AXMLPrinter.main(new String[]{xmlFilePath});
        System.setOut(oldPrintStream);
        String result = baos.toString();
        newPrintStream.close();
        baos.close();
        return result;
    }

}
