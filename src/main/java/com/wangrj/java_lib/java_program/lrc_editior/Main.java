package com.wangrj.java_lib.java_program.lrc_editior;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        printHint();
        int num;
        String fileName;
        String charsetName = "gbk";

        while (true) {
            try {
                num = Integer.parseInt(scanner.next());

                switch (num) {

                    case 1:
                        System.out.printf("请输入需要进行格式的文件名：");
                        fileName = scanner.next();
                        LrcKit.formatLRC(new File(fileName), charsetName);
                        printState("文本格式成功!");
                        break;

                    case 2:
                        System.out.printf("请输入需要进行删除歌词的文件名：");
                        fileName = new String(scanner.next().getBytes(),
                                charsetName);
                        System.out
                                .println("1.〖〗 2.【】 3.<> 4.『』 5.() 6.（） 7.「」 8./ 9.\\");
                        System.out.printf("请输入特殊括号的编号：");
                        int i = scanner.nextInt();
                        String[] signs = new String[]{"〖", "〗", "【", "】", "<",
                                ">", "『", "』", "(", ")", "（", "）", "「", "」", "/",
                                "@!#", "\\", "@!#"};// 斜杠作转义,
                        // @!#只是为了设置罕见字符串使正则表达式配对失败
                        LrcKit.deleteInside(new File(fileName), charsetName,
                                signs[i * 2 - 2], signs[i * 2 - 1]);
                        printState("文本歌词删除成功");
                        break;

                    case 3:
                        System.out.printf("请输入左文件名：");
                        String leftName = scanner.next();
                        System.out.printf("请输入右文件名：");
                        String rightName = scanner.next();
                        LrcKit.mixture(new File(leftName), new File(rightName),
                                new File("c.lrc"), charsetName);
                        printState("文本合并成功");
                        break;

                    case 4:
                        System.out.printf("当前编码格式：" + charsetName + "，请输入新的编码格式：");
                        charsetName = scanner.next();
                        printState("编码格式修改成功！ 当前编码格式：" + charsetName);
                        break;

                    case 5:
                        System.out.printf("请输入需要进行删除奇数行的文件名：");
                        fileName = scanner.next();
                        LrcKit.transCrossingLine(new File(fileName), charsetName,
                                false);
                        printState("奇数行删除成功");
                        break;

                    case 6:
                        System.out.printf("请输入需要进行删除偶数行的文件名：");
                        fileName = scanner.next();
                        LrcKit.transCrossingLine(new File(fileName), charsetName,
                                true);
                        printState("偶数行删除成功");
                        break;

                    case 7:
                        System.out.printf("请输入需要留下标签的Lrc文件名：");
                        fileName = scanner.next();
                        LrcKit.saveLabel(new File(fileName), charsetName);
                        printState("留下标签成功");
                        break;

                    case 8:
                        System.out.printf("请输入左文件名：");
                        leftName = scanner.next();
                        System.out.printf("请输入右文件名：");
                        rightName = scanner.next();
                        System.out.printf("是否删除Lrc文件（左文件）的歌词?（y/n）：");
                        String flag = scanner.next();
                        LrcKit.mixtureKuGou(new File(leftName),
                                new File(rightName), charsetName,
                                flag.equals("n") ? false : true);
                        printState("文本合并成功");
                        break;

                    case 9:
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.printf("请选择操作代号:");
                        break;
                }

            } catch (Exception e) {
                printState("错误: " + e.getMessage());
            }
        }

    }

    private static void printHint() {
        System.out.println("\t欢迎使用 Lrc Editior v1.0\n");
        System.out.println("1.格式Lrc文件");
        System.out.println("2.删除特殊括号以及其外的歌词，特殊括号如〖〗【】<>『』()（）「」/\\");
        System.out.println("3.Lrc纯标签文本与纯歌词文本行对行合并");
        // System.out.println("4.删除特殊括号以及其内的歌词");
        System.out.println("4.修改编码格式（gbk和utf-8，默认gbk），出现乱码时请修改");
        System.out.println("5.删除奇数行");
        System.out.println("6.删除偶数行");
        System.out.println("7.Lrc文件留下标签");
        System.out.println("8.Lrc文本与酷狗复制的日文歌词（奇行）翻译（偶行）文本对行合并");
        System.out.println("9.退出");
        System.out.printf("\n请选择操作代号:");
    }

    private static void printState(String state) {
        System.out.println("\n提示：" + state);
        System.out.printf("\n请选择操作代号:");
    }
}
