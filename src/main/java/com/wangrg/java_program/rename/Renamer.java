package com.wangrg.java_program.rename;

import com.wangrg.java_util.TextUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * by ���ٿ� on 2016/7/26.
 */
public class Renamer {

    public static final int MODE_REPLACE = 0;
    public static final int MODE_APPEND = 1;

    private String dir;
    private String renameText;
    private int mode;
    private boolean ignoreFileSuffix;//�Ƿ�����ļ�����׺
    private String prefix;//ǰ׺
    private String suffix;//��׺
    private String incrementBegin;

    private static final String INCREMENT_SIGN = "[auto_increment]";

    private List<RenameFile> renameFiles;

    /**
     * ע�⣺������ʽ��[auto_increment]���ַ��������Զ��滻Ϊ������������01,02,03...
     */
    public Renamer(String dir, String renameText, int mode, boolean ignoreFileSuffix,
                   String prefix, String suffix, String incrementBegin) {
        this.dir = dir;
        this.renameText = renameText;
        this.mode = mode;
        this.ignoreFileSuffix = ignoreFileSuffix;
        this.prefix = prefix;
        this.suffix = suffix;
        this.incrementBegin = incrementBegin;
    }

    public String prepare() throws Exception {
        renameFiles = new ArrayList<>();
        String[] lines = toLines(renameText);
        File[] files = new File(dir).listFiles();
        StringBuilder builder = new StringBuilder();

        if (files == null) {
            throw new Exception();
        }

        for (int i = 0; i < lines.length && i < files.length; i++) {
            String oldName = files[i].getName();
            String newName = getNewName(oldName, lines[i]);
            newName = newName.replace(INCREMENT_SIGN, getIncrementString(i));//�ѵ���ƥ���ת��Ϊ��������
            newName = TextUtil.correctFileName(newName, "");//ȥ���Ƿ�����
            RenameFile renameFile = new RenameFile(files[i], newName);
            renameFiles.add(renameFile);

            builder.append(oldName).append("\t").append(newName).append("\n");
        }

        return builder.toString();
    }

    /**
     * ����incrementBegin = "004" ����ôindex=0ʱ������004��index=1ʱ������005���Դ����ơ�
     */
    private String getIncrementString(int index) throws Exception {
        int begin = Integer.parseInt(incrementBegin);
        int n = incrementBegin.length();
        String incrementString = (begin + index) + "";
        while (incrementString.length() < n) {
            incrementString = "0" + incrementString;
        }
        return incrementString;
    }

    /**
     * @return �����ļ�������ʧ�ܵ��б�
     */
    public String rename() {
        StringBuilder builder = new StringBuilder();
        for (RenameFile renameFile : renameFiles) {
            File file = renameFile.getFile();
            String newName = renameFile.getNewName();
            String newFilePath = file.getAbsolutePath().replace(file.getName(), newName);
            boolean succeed = file.renameTo(new File(newFilePath));
            if (!succeed) {
                builder.append("�ļ�����ʧ�ܣ�").append(file.getName()).append("\n");
            }
        }
        return builder.toString();
    }

    private String getNewName(String oldName, String rename) {
        String fileSuffix = "." + TextUtil.getTextAfterLastPoint(oldName);
        String newName = "";
        if (mode == MODE_REPLACE) {
            if (ignoreFileSuffix && !TextUtil.isEmpty(fileSuffix)) {
                newName = prefix + rename + suffix + fileSuffix;
            } else {
                newName = prefix + rename + suffix;
            }

        } else if (mode == MODE_APPEND) {
            if (ignoreFileSuffix && !TextUtil.isEmpty(fileSuffix)) {
                newName = prefix + oldName.replace(fileSuffix, "") + suffix + fileSuffix;
            } else {
                newName = prefix + oldName + suffix;
            }
        }

        return newName;
    }

    private String[] toLines(String text) {
        return text.replace("\n\r", "\n").split("[\n]");
    }

    class RenameFile {
        private File file;
        private String newName;

        public RenameFile(File file, String newName) {
            this.file = file;
            this.newName = newName;
        }

        public File getFile() {
            return file;
        }

        public String getNewName() {
            return newName;
        }

    }

}
