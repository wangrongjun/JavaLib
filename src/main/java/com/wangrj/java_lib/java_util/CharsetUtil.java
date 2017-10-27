package com.wangrj.java_lib.java_util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by 王荣俊 on 2016/3/28.
 */
public class CharsetUtil {

    public static String[] charsets = new String[]{"gbk", "utf-8", "ISO8859_1"};

    public static String encode(String text) {
        if (TextUtil.isEmpty(text)) {
            return "";
        }
        try {
            return URLEncoder.encode(text, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decode(String text) {
        if (TextUtil.isEmpty(text)) {
            return "";
        }
        try {
            return URLDecoder.decode(text, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void printAllSet(String text) throws UnsupportedEncodingException {
        System.out.println("text:\n" + text + "\n");
        for (String fromCharSet : charsets) {
            for (String toCharSet : charsets) {
                String s = "new String(text.getBytes(" + fromCharSet + "), " + toCharSet + "):\n";
                s += new String(text.getBytes(fromCharSet), toCharSet) + "\n";
                System.out.println(s);
            }
        }

    }

    public static String changeCharset(String text, String fromSet, String toSet) {
        try {
            return new String(text.getBytes(fromSet), toSet);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String println(String text) {
        try {
            text = new String(text.getBytes("gbk"), "utf-8");
            System.out.println(text);
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断编码格式是否为utf-8
     *
     * @param text 长度为0或为空则抛异常
     */
    public static boolean isUTF_8(String text) {
        byte[] b = text.getBytes();
        if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
            return true;
        } else {
            return false;
        }
    }

}
