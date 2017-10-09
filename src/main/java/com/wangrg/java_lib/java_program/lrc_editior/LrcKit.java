package com.wangrg.java_lib.java_program.lrc_editior;

import java.io.File;

public class LrcKit {

    public static void saveLabel(File lrcFile, String charsetName)
            throws Exception {

        LrcKit.formatLRC(lrcFile, charsetName);
        String text = FileOperator.readFile(lrcFile, charsetName);
        String[] sentenses = text.split("\r\n");
        StringBuilder builder = new StringBuilder();

        for (String sentense : sentenses) {
            String s = sentense;

            if (s.length() > 2) {
                boolean flag1 = s.substring(1, 2).matches("\\d");
                boolean flag2 = sentense.length() > 10;

                if (flag1 && flag2) {// 是时间标签，而不是信息标签或其他
                    s = sentense.substring(0, 10);
                }
            }

            builder.append(s + "\r\n");
        }

        FileOperator.saveFile(lrcFile, charsetName, builder.toString());

    }

    /**
     * 删除每一句歌词特殊括号及括号外的文本，常见特殊括号：〖〗【】<>『』 注意传进来的left,right有可能是特殊符号如()需要转义
     *
     * @param left  左特殊括号
     * @param right 右特殊括号
     */
    public static void deleteInside(File lrcFile, String charsetName,
                                    String left, String right) throws Exception {

        LrcKit.formatLRC(lrcFile, charsetName);
        String text = FileOperator.readFile(lrcFile, charsetName);

        if (!text.contains(left)) {
            throw new Exception("无该特殊括号:" + left + right);
        }

        LrcKit.formatLRC(lrcFile, charsetName);
        String[] sentenses = text.split("\r\n");

        StringBuilder builder = new StringBuilder();

        for (String s : sentenses) {
            if (s.length() == 0) {
                continue;
            }

            String s1 = s.replaceAll("\\].*", "]");
            String s2;
            if (left.equals("(")) {
                s2 = s.replaceAll(".*\\" + left, "").replaceAll(
                        "\\" + right + ".*", "");
            } else if (left.equals("\\")) {
                s2 = s.replaceAll(".*\\" + left, "");
            } else {
                s2 = s.replaceAll(".*" + left, "").replaceAll(right + ".*", "");

            }

            builder.append((s.contains(left) ? s1 + s2 : s) + "\r\n");
        }

        FileOperator.saveFile(lrcFile, charsetName, builder.toString());

    }

    /**
     * 左右两个文本行对行合并
     */
    public static void mixture(File lFile, File rFile, File newFile,
                               String charsetName) throws Exception {

        String[] s1 = FileOperator.readFile(lFile, charsetName).split("\r\n");
        String[] s2 = FileOperator.readFile(rFile, charsetName).split("\r\n");

        StringBuilder builder = new StringBuilder();

        int i;

        for (i = 0; i < s1.length && i < s2.length; i++) {
            builder.append(s1[i] + s2[i] + "\r\n");
        }

        while (i < s1.length) {
            builder.append(s1[i++] + "\r\n");
        }

        while (i < s2.length) {
            builder.append(s2[i++] + "\r\n");
        }

        FileOperator.saveFile(newFile, charsetName, builder.toString());

    }

    /**
     * 左：Lrc文本 右：酷狗复制的日文歌词（奇行）翻译（偶行）文本 功能：行对行合并
     *
     * @param deleteLeftLrc true:删除Lrc文本的歌词部分 false:保留
     */
    public static void mixtureKuGou(File lrcFile, File textFile,
                                    String charsetName, boolean deleteLeftLrc) throws Exception {

        LrcKit.formatLRC(lrcFile, charsetName);
        if (deleteLeftLrc) {
            LrcKit.saveLabel(lrcFile, charsetName);
        }
        LrcKit.transCrossingLine(textFile, charsetName, false);
        String s = FileOperator.readFile(lrcFile, charsetName);
        String text = FileOperator.readFile(textFile, charsetName);

        String[] lrcSentenses = s.split("\r\n");

        String append = "";
        for (int i = 0; i < lrcSentenses.length; i++) {
            // 如[ar:kana]中，第二个字符a不是数字，true
            if (lrcSentenses[i].length() > 2
                    && lrcSentenses[i].substring(1, 2).matches("\\D")) {
                append = append + "\r\n";
            }
        }

        if (!deleteLeftLrc) {
            text = text.replace("\r\n", ">\r\n <");
            text = " <" + text;
        }

        text = append + text;

        FileOperator.saveFile(textFile, charsetName, text);
        LrcKit.mixture(lrcFile, textFile, lrcFile, charsetName);
    }

    /**
     * @param single true:保存奇行 false:保存偶行
     */
    public static void transCrossingLine(File file, String charsetName,
                                         boolean single) throws Exception {

        String s = FileOperator.readFile(file, charsetName);
        String[] sentences = s.split("\r\n");

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < sentences.length; i++) {
            if ((single && i % 2 == 0) || (!single && i % 2 == 1)) {
                builder.append(sentences[i] + "\r\n");
            }
        }

        FileOperator.saveFile(file, charsetName, builder.toString());

    }

    /**
     * Lrc文本格式化
     */
    public static void formatLRC(File lrcFile, String charsetName)
            throws Exception {
        String text = FileOperator.readFile(lrcFile, charsetName);
        text = text.replace("\r", "");
        text = text.replace("\n", "");
        text = text.replace("[", "\r\n[");
        if (text.length() > 2) {
            text = text.substring(2);
        }
        FileOperator.saveFile(lrcFile, charsetName, text);
    }

}
