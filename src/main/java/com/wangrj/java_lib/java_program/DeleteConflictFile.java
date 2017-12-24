package com.wangrj.java_lib.java_program;

import com.wangrj.java_lib.data_structure.Pair;
import com.wangrj.java_lib.java_util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * by wangrongjun on 2017/12/24.
 * 搜索并处理天翼云的重复文件
 */
public class DeleteConflictFile {

    private static final String CONFLICT_FILE_NAME_REGEX =
            "(.+)的冲突文件 \\d{4}-\\d{2}-\\d{2} \\d{2}-\\d{2}-\\d{2}-\\d{3}(\\..*)?";

    private static final String helloWorld = "" +
            " ----- 搜索并处理天翼云的重复文件 ------\n" +
            "对于文件名以“的冲突文件 2017-12-17 22-10-24-637”类似格式结尾（不包括后缀名）的处理方式：\n" +
            "1. 如果同一目录下存在冲突文件对应的原文件。且如果原文件大小与冲突文件大小一致，移动冲突文件到指定目录，否则提示出错。\n" +
            "2. 如果同一目录下不存在冲突文件对应的原文件，重命名为原文件名。";

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(helloWorld);
        System.out.println("请输入目录地址：");
        String searchDir = new Scanner(System.in).next();
        if (!new File(searchDir).exists()) {
            System.out.println("目录不存在");
            return;
        }
        File conflictFileMoveFolder = new File(searchDir + File.separator + "ConflictFiles");
        List<File> conflictFileList = searchConflictFile(searchDir);

        if (conflictFileList.size() > 0) {
            if (!conflictFileMoveFolder.mkdir() && !conflictFileMoveFolder.exists()) {
                System.out.println("创建文件夹ConflictFiles失败");
                return;
            }
        }

        for (File conflictFile : conflictFileList) {
            Pair<Boolean, String> result = handleConflictFile(conflictFile, conflictFileMoveFolder);
            if (!result.first) {
                System.out.println("失败！冲突文件：" + conflictFile);
                System.out.println("错误原因：" + result.second);
            } else {
                System.out.println("成功！冲突文件：" + conflictFile);
            }
        }

        System.out.println("\n\n冲突文件处理完毕！");
    }

    private static List<File> searchConflictFile(String dir) {
        return FileUtil.findChildrenUnderDir(new File(dir),
                pathname -> pathname.getName().matches(CONFLICT_FILE_NAME_REGEX));
    }

    /**
     * 搜索并处理天翼云的重复文件
     * 对于文件名以“的冲突文件 2017-12-17 22-10-24-637”结尾（不包括后缀名）的处理方式：
     * <p>
     * 1. 如果同一目录下存在冲突文件对应的原文件。且如果原文件大小与冲突文件大小一致，移动冲突文件到指定目录，否则提示出错
     * 2. 如果同一目录下不存在冲突文件对应的原文件，重命名为原文件名
     *
     * @param conflictFile           冲突文件
     * @param conflictFileMoveFolder 冲突文件移动到的目录
     * @return 第一个参数代表是否成功，第二个参数代表提示信息
     */
    private static Pair<Boolean, String> handleConflictFile(File conflictFile, File conflictFileMoveFolder) {
        Matcher matcher = Pattern.compile(CONFLICT_FILE_NAME_REGEX).matcher(conflictFile.getName());
        if (!matcher.find()) {
            return new Pair<>(false, "程序异常：匹配不到原文件名");
        }
        String originFileName = matcher.group(1);
        if (matcher.groupCount() >= 2) {// 如果有后缀名，要补上
            originFileName += matcher.group(2);
        }

        File originFile = new File(conflictFile.getParent(), originFileName);
        if (originFile.exists()) {
            if (originFile.length() == conflictFile.length()) {// 移动冲突文件到指定目录
                File moveConflictFile = new File(conflictFileMoveFolder, conflictFile.getName());
                if (!conflictFile.renameTo(moveConflictFile)) {
                    String msg = "移动冲突文件到指定目录失败。移动路径：" + moveConflictFile;
                    return new Pair<>(false, msg);
                }
            } else {// 出错
                return new Pair<>(false, "存在冲突文件对应的原文件，但大小不相同");
            }
        } else {// 重命名为原文件名
            if (!conflictFile.renameTo(originFile)) {
                String msg = "冲突文件对应的原文件不存在，但是冲突文件重命名为原文件名失败。原文件：" + originFile;
                return new Pair<>(false, msg);
            }
        }
        return new Pair<>(true, null);
    }

    @Test
    public void testIsConflictFile() {
        assertEquals(true, "openGL相关库文件的冲突文件 2017-12-17 22-16-29-692.rar".matches(CONFLICT_FILE_NAME_REGEX));
        assertEquals(false, "的冲突文件 2017-12-17 22-16-29-692.rar".matches(CONFLICT_FILE_NAME_REGEX));
        assertEquals(false, "openGL相关库文件的冲突文件 2017-12-17 22-16-29-6921.rar".matches(CONFLICT_FILE_NAME_REGEX));
        assertEquals(false, "openGL相关库文件的冲突文件  2017-12-17 22-16-29-692.rar".matches(CONFLICT_FILE_NAME_REGEX));
    }

}
