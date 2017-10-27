package com.wangrj.java_lib.java_util;

import org.junit.Test;

import java.security.MessageDigest;

/**
 * by wangrongjun on 2017/9/29.
 */
public class Md5Util {
    /**
     * @return 返回长度为30的摘要
     */
    public static String md5(String s) {
        if (TextUtil.isEmpty(s)) {
            return s;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testMd5() {
        System.out.println(md5(null));// null
        System.out.println(md5(""));// ""

        System.out.println(md5("123"));// "202cb962ac59075b964b07152d234b70"
        System.out.println(md5("123!@#$%^&*()acdvsd-=_+-=>"));// "468f8bb3fef515784b61013952747c0b"

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 100000001; i++) {
            builder.append("a");
        }
        System.out.println(md5(builder.toString()));
        // 100000    - "1af6d6f2f682f76f80e606aeaaee1680"
        // 1000000   - "7707d6ae4e027c70eea2a935c2296f21"
        // 10000000  - "7095bae098259e0dda4b7acc624de4e2"
        // 100000000 - "458a3045ba5c1f9a4cde4176be274f2b"
        // 100000001 - "161640a3be9258460c9ee5810743493c"
    }

}
