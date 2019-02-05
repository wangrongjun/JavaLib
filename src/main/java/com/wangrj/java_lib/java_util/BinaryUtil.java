package com.wangrj.java_lib.java_util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 王荣俊 on 2016/5/26.
 */
public class BinaryUtil {

    public static String toBinaryString(byte b) {
        int n = b & 0xff;  //一定要进行按位与获取低8位，因为byte类型数据有八位，如果正常转换为int，
        // byte从低到高第8位会成为int从低到高第32位(符号位)，而int的第八位为0。
        String s = "";
        int m = 128;
        while (m > 0) {
            if (n >= m) {
                n -= m;
                s += "1";
            } else {
                s += "0";
            }
            m /= 2;
        }
        return s;
    }

    public static String toBinaryString(byte[] bytes, String separator) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(toBinaryString(b) + (separator == null ? "" : separator));
        }
        return builder.toString();
    }

    public static String toBinaryString(byte[] bytes, String separator, int off, int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = off; i < len; i++) {
            builder.append(toBinaryString(bytes[i]) + (separator == null ? "" : separator));
        }
        return builder.toString();
    }

    /**
     * 把字符串11110000等转换成一个字节
     *
     * @param binaryString 长度为8
     */
    public static byte toByte(String binaryString) {
        if (binaryString.length() != 8) {
            return 0;
        }

        byte b = 0;
        int m = 128;

        for (int i = 0; i < 8; i++) {
            String c = binaryString.charAt(i) + "";
            if (c.equals("1")) {
                b += m;
            }
            m /= 2;
        }
        return b;
    }

    /**
     * 把字符串1111000011110000等转换成字节数组
     *
     * @param binaryString
     * @return
     */
    public static byte[] toBytes(String binaryString) {
        int len = binaryString.length();
        if (len % 8 != 0) {
            return null;
        }

        len /= 8;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            String s = binaryString.substring(i * 8, i * 8 + 8);
            bytes[i] = toByte(s);
        }

        return bytes;
    }

    /**
     * 把任意一个文件转换成以0和1字符存储的文本
     *
     * @param binaryFilePath
     * @param textFilePath
     */
    public static void binaryToText(String binaryFilePath, String textFilePath) {
        try {
            FileInputStream fis = new FileInputStream(binaryFilePath);
            int len;
            byte[] buf = new byte[1024];
            StringBuilder builder = new StringBuilder();
            while ((len = fis.read(buf)) != -1) {
                builder.append(BinaryUtil.toBinaryString(buf, null, 0, len));
            }
            fis.close();
            FileUtil.write(builder.toString(), textFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把以0和1字符存储的文本转换成文件
     *
     * @param textFilePath
     * @param binaryFilePath
     */
    public static void textToBinary(String textFilePath, String binaryFilePath) {
        try {
            String s = FileUtil.read(textFilePath);
            byte[] bytes = toBytes(s);
            if (bytes != null && bytes.length > 0) {
                FileOutputStream fos = new FileOutputStream(binaryFilePath);
                fos.write(bytes);
                fos.flush();
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long toNumber(String binaryString) {
        long temp = 1;
        long result = 0;
        for (int i = binaryString.length() - 1; i >= 0; i--) {
            char c = binaryString.charAt(i);
            if (c == '1') {
                result += temp;
            } else if (c != '0') {
                throw new IllegalArgumentException("not binary string, with illegal char: " + c);
            }
            temp *= 2;
        }
        return result;
    }

    /**
     * 返回形如ffffff格式的字符串
     *
     * @param r red,0-255
     * @param g green,0-255
     * @param b blue,0-255
     * @return
     */
    public static String toRGBString(int r, int g, int b) {
        String rs = to16String(r);
        String gs = to16String(b);
        String bs = to16String(r);
        if (rs.length() == 1) {
            rs = "0" + rs;
        }
        if (gs.length() == 1) {
            gs = "0" + gs;
        }
        if (bs.length() == 1) {
            bs = "0" + bs;
        }
        return rs + gs + bs;
    }

    /**
     * 十进制数转换成16进制的字符串
     */
    public static String to16String(int number) {
        String result = "";
        int temp;// 商
        do {
            temp = number / 16;
            int i = number % 16;// 余数
            if (i < 10) {
                result = i + result;
            } else {
                switch (i) {
                    case 10:
                        result = "A" + result;
                        break;
                    case 11:
                        result = "B" + result;
                        break;
                    case 12:
                        result = "C" + result;
                        break;
                    case 13:
                        result = "D" + result;
                        break;
                    case 14:
                        result = "E" + result;
                        break;
                    case 15:
                        result = "F" + result;
                        break;
                }
            }
            number = temp;
        } while (temp > 0);

        return result;
    }

    /**
     * 把16进制的字符串转换成10进制数字
     */
    public static int to10Number(String s16) {
        s16 = s16.toUpperCase();
        int result = 0;
        int temp = 1;
        for (int i = s16.length() - 1; i >= 0; i--) {
            char c = s16.charAt(i);
            if (c >= 48 && c <= 57) {
                result += temp * (c - 48);
            } else if (c >= 65 && c <= 90) {
                result += temp * (c - 65 + 10);
            } else {
                System.out.println("error: with illegal char: " + c);
                return -1;
            }
            temp *= 16;
        }
        return result;
    }

    /**
     * 获取一个byte中某个区段的bit组成的值
     * 例子：
     * 0b10101010, 0, 1 => 0b10 => 2
     * 0b10101010, 1, 3 => 0b101 => 5
     */
    public static int getBitValue(byte data, int low, int high) {
        // 思路：1.先把data右移low位，使得需要读取的区段的bit在最左侧 2.使用掩码把最左侧需要读取的区段的bit投影出来
        return (data >> low) & (0b11111111 >> (8 - (high - low + 1)));
    }

}
