package com.wangrj.java_lib.java_util;

import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class DataUtil {

    // 单向加密算法
    public static String encrypt(String text) {
        int len = text.length();
        int[] n = new int[len];
        for (int i = 0; i < len; i++) {
            n[i] = text.charAt(i) % 10;
        }

        long result = 0;

        for (int i = 0; i < len; i++) {
            long temp = 1;
            int power = (i + 3) % 5 + 1;
            for (int j = 0; j < power; j++) {
                temp *= n[i] * 43 + 124;
            }
            result += temp;
        }

        result *= 169;

        String s = get16by10Number(result, true);

        return s.charAt(s.length() - 1) + s.substring(1, s.length() - 2)
                + s.charAt(0);

    }

    /**
     * 从十进制数转化成十六进制
     *
     * @param bigLetter 返回大写字母或小写字母
     */
    public static String get16by10Number(long number, boolean bigLetter) {
        StringBuilder builder = new StringBuilder();

        while (number > 0) {
            // leave为余数
            int leave = (int) (number % 16);

            String s;
            if (leave < 10) {
                s = leave + "";
            } else {

                s = (char) (bigLetter ? 'A' : 'a' + leave - 10) + "";
            }

            builder.append(s);
            number /= 16;
        }

        String s = (new StringBuffer(builder.toString()).reverse()).toString();

        return s;
    }

    public static String md5(String text) {
        if (TextUtil.isEmpty(text)) return "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(md5.digest(text.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String sha1Hex(byte[] data) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

        messageDigest.update(data);

        byte[] sha1 = messageDigest.digest();
        String sha1Hex = "";
        for (int i = 0; i < sha1.length; ++i) {
            String s = Integer.toHexString(sha1[i] & 255);// 参考Groovy的String.encodeHex方法
            if (s.length() < 2) {
                sha1Hex += "0";
            }
            sha1Hex += s;
        }

        return sha1Hex;
    }

    /**
     * 文件转化为字符串
     */
    public static String toBase64String(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        return Base64.encodeToString(StreamUtil.toBytes(fis), Base64.DEFAULT);
    }

    /**
     * 字符串转化为文件
     */
    public static void fromBase64String(String base64String, String savePath) throws IOException {
        byte[] data = Base64.decode(base64String, Base64.DEFAULT);
        FileOutputStream fos = new FileOutputStream(savePath);
        fos.write(data);
        fos.flush();
        fos.close();
    }

    /**
     * DES加密
     * http://www.cnblogs.com/langtianya/p/3715975.html JAVA实现DES加密实现详解
     *
     * @param password 字符串长度不能少于8
     */
    public static byte[] desEncrypt(byte[] dataSource, String password) throws Exception {
        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
//            创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
//            Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
//            用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
//            现在，获取数据并加密
//            正式执行加密操作
        return cipher.doFinal(dataSource);
    }

    /**
     * DES解密
     * http://www.cnblogs.com/langtianya/p/3715975.html JAVA实现DES加密实现详解
     *
     * @param password 字符串长度不能少于8
     */
    public static byte[] desDecrypt(byte[] src, String password) throws Exception {
//        DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
//        创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
//        创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//        将DESKeySpec对象转换成SecretKey对象
        SecretKey secureKey = keyFactory.generateSecret(desKey);
//        Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance("DES");
//        用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secureKey, random);
//        真正开始解密操作
        return cipher.doFinal(src);
    }

}
