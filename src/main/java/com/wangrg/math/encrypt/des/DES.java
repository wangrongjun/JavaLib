package com.wangrg.math.encrypt.des;

import com.wangrg.java_util.BinaryUtil;

/**
 * by wangrongjun on 2017/6/1.
 */

public class DES {

    /**
     * DES加密
     *
     * @param m 明文（64位）
     * @param k 密钥（64位）
     * @return 密文c
     */
    public static byte[] encrypt(byte[] m, byte[] k) throws Exception {
        if (m.length != 8 || k.length != 8) {
            throw new Exception("length of m and k must be 64");
        }
        byte[][] kList = createKChildren(k);
        return null;
    }

    private static final int[] C0 = new int[]{
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36
    };
    private static final int[] D0 = new int[]{
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    /**
     * 64位密钥经过置换选择1，循环左移，置换选择2等变换，产生出16个48位长的子密钥。
     * 置换选择1：1.从64位密钥中去掉8个奇偶校验位（不检查） 2.打乱重排，前28位作为C0，后28位作为D0
     *
     * @param k 64位密钥
     * @return 16个48位长的子密钥
     */
    private static byte[][] createKChildren(byte[] k) {
        String s1 = BinaryUtil.toBinaryString(k, "");
        String s2 = "";
        // 开始置换选择1
        for (int i : C0) {
            s2 += s1.charAt(i - 1);
        }
        for (int i : D0) {
            s2 += s1.charAt(i - 1);
        }
        // 开始循环左移

        // 开始置换选择2


        return new byte[0][];

    }

//    private enum Bit {ONE, ZERO}

    /**
     * 获取byte的某一位
     *
     * @param b
     * @param index 7到0，高位到低位
     */
    private static int getBit(byte b, int index) {
        return (b >> index) & 0xfe;
    }
}
