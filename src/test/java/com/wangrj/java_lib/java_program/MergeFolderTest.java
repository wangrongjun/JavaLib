package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * by wangrongjun on 2018/7/8.
 */
public class MergeFolderTest {

    private void deleteTemp() {
        FileUtil.deleteDir(new File("merge_from"));
        FileUtil.deleteDir(new File("merge_to"));
        File[] files = new File(".").listFiles(pathname -> pathname.getName().matches("merge_to_.+"));
        if (files != null && files.length != 0) {
            assert files.length == 1;
            FileUtil.deleteDir(files[0]);
        }
    }

    @Test
    public void testMergeFolder() throws IOException {
        deleteTemp();

        // 根目录替换
        assertTrue(new File("merge_from").mkdirs());
        assertTrue(new File("merge_to").mkdirs());
        FileUtil.write("v2", "merge_from/ClassA.txt");
        FileUtil.write("v1", "merge_to/ClassA.txt");
        FileUtil.write("v2", "merge_from/ClassB.txt");
        FileUtil.write("v1", "merge_to/ClassB.txt");

        // 子目录替换
        assertTrue(new File("merge_from/com").mkdirs());
        assertTrue(new File("merge_to/com").mkdirs());
        FileUtil.write("v2", "merge_from/com/ClassC.txt");
        FileUtil.write("v1", "merge_to/com/ClassC.txt");
        FileUtil.write("v2", "merge_from/com/ClassD.txt");
        FileUtil.write("v1", "merge_to/com/ClassD.txt");

        // 新增
        assertTrue(new File("merge_from/org").mkdirs());
        FileUtil.write("v2", "merge_from/org/ClassE.txt");

        MergeFolder.main(new String[]{"merge_from", "merge_to"});

        // 验证合并结果
        assertEquals("v2", FileUtil.read("merge_to/ClassA.txt"));
        assertEquals("v2", FileUtil.read("merge_to/ClassB.txt"));
        assertEquals("v2", FileUtil.read("merge_to/com/ClassC.txt"));
        assertEquals("v2", FileUtil.read("merge_to/com/ClassD.txt"));
        assertEquals("v2", FileUtil.read("merge_to/org/ClassE.txt"));

        // 验证备份
        File[] files = new File(".").listFiles(pathname -> pathname.getName().matches("merge_to_.+"));
        assertTrue(files != null && files.length == 1);
        String mergeToBak = files[0].getPath();
        assertEquals("v1", FileUtil.read(mergeToBak + "/ClassA.txt"));
        assertEquals("v1", FileUtil.read(mergeToBak + "/ClassB.txt"));
        assertEquals("v1", FileUtil.read(mergeToBak + "/com/ClassC.txt"));
        assertEquals("v1", FileUtil.read(mergeToBak + "/com/ClassD.txt"));
        assertFalse(new File(mergeToBak + "/org/ClassE.txt").exists());

//        deleteTemp();
    }

}