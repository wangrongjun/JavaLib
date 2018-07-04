package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.java_util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CopyFilesInFolderTest {

    @Test
    public void testCopyFilesInFolder() throws IOException {
        FileUtil.deleteDir(new File("test"));

        assertTrue(new File("test/dao").mkdirs());
        assertTrue(new File("test/dao/impl").mkdirs());
        assertTrue(new File("test/entity").mkdirs());
        assertTrue(new File("test/util").mkdirs());

        assertTrue(new File("test/Application.java").createNewFile());
        assertTrue(new File("test/dao/UserDao.java").createNewFile());
        assertTrue(new File("test/dao/impl/UserDaoImpl.java").createNewFile());
        assertTrue(new File("test/entity/User.java").createNewFile());

        List<String> filePathList = new ArrayList<>();
        filePathList.add("test/Application.java");
        filePathList.add("test/dao/UserDao.java");
        filePathList.add("test/dao/impl/UserDaoImpl.java");
        filePathList.add("test/entity/User.java");
        filePathList.add("test/entity/Emp.java");// 不存在
        filePathList.add("test/util");
        String filesContent = filePathList.stream().collect(Collectors.joining("\r\n"));
        FileUtil.write(filesContent, "files.txt");

        CopyFilesInFolder.main(null);

        assertTrue(new File("output/test/Application.java").exists());
        assertTrue(new File("output/test/dao/UserDao.java").exists());
        assertTrue(new File("output/test/dao/impl/UserDaoImpl.java").exists());
        assertTrue(new File("output/test/entity/User.java").exists());
        assertTrue(new File("output/test/util").exists());
        assertTrue(new File("output/test/util").isDirectory());
        assertFalse(new File("output/test/entity/Emp.java").exists());
    }

}
