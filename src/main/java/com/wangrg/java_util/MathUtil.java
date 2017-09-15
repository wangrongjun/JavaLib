package com.wangrg.java_util;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * by 王荣俊 on 2016/4/1.
 */
public class MathUtil {

    /**
     * 指数模运算
     * http://blog.csdn.net/yanheng0130/article/details/8948263
     *
     * @return a ^ b % m
     */
    public static BigInteger indexModule(BigInteger a, BigInteger b, BigInteger m) {
        BigInteger result = new BigInteger("1");
        // 把b转化为二进制数的字符串表示形式
        String binaryB = BinaryUtil.toBinaryString(b.toByteArray(), "");
        char[] charArrayB = binaryB.toCharArray();
        boolean meetOne = false;
        for (char c : charArrayB) {
            if (!meetOne) {//先忽略掉前面无效的0
                if (c == '0') {
                    continue;
                } else {
                    meetOne = true;
                }
            }
//            d = (d * d) % m;
            result = result.multiply(result).mod(m);
            if (c == '1') {
//                d = (d * a) % m;
                result = result.multiply(a).mod(m);
            }
        }

        DebugUtil.println(a + " ^ " + b + " % " + m + " = " + result);

        return result;
    }

    public static long indexModule(long a, long b, long m) {
        return indexModule(
                BigInteger.valueOf(a),
                BigInteger.valueOf(b),
                BigInteger.valueOf(m)
        ).longValue();
    }

    @Test
    public void testIndexModule() {
        Assert.assertEquals(5, indexModule(26, 7, 33));
    }

    /**
     * 欧几里德算法求多个数的最大公约数
     */
    public static Long gcd(List<Long> longList) {
        long gcd = longList.get(0);
        for (int i = 0; i < longList.size() - 1; i++) {
            gcd = gcd(gcd, longList.get(i + 1));
            if (gcd == 1) return gcd;//如果当前两个的最大公约数已经为1，就没必要继续求解了
        }
        return gcd;
    }

    /**
     * 欧几里德算法的递归实现
     */
    public static long gcd(long a, long b) {
//        保证a不小于b
        if (a < b) {
            long temp = a;
            a = b;
            b = temp;
        }
//        核心算法：gcd(a, b) = gcd(b , a%b)
        if (b == 0) return a;
        else return gcd(b, a % b);
    }

    /**
     * 求两个正整数之最大公因数，使用辗转相除法， 又名欧几里德算法（Euclidean algorithm）
     * <p/>
     * 例：求 15750 与27216的最大公约数。
     * ∵27216=15750×1+11466 ∴（15750，27216）=（15750，11466）
     * ∵15750=11466×1+4284  ∴（15750，11466）=(11466,4284)
     * ∵11466=4284×2+2898  ∴(11466,4284)=（4284，2898）
     * ∵4284=2898×1+1386   ∴（4284，2898）=（2898，1386）
     * ∵2898=1386×2+126   ∴（2898，1386）=（1386，126）
     * ∵1386=126×11     ∴（1386，126）= （126，0）
     * 所以（15750，27216）=216
     */
    public static long maxCommonDivisor(long n1, long n2) {

        if (n1 < 1 || n2 < 1) {
            return -1;
        }

        if (n1 < n2) {//保证n1 >= n2
            long temp = n1;
            n1 = n2;
            n2 = temp;
        }
        while (true) {
            //例如，此时是（1386，126）
            long temp = n2;
            n2 = n1 % n2;
            n1 = temp;
            //到这里，就变成（126，0），其中1386%126=0
            if (n2 == 0) {
                return n1;
            }
        }
    }

    /**
     * 求最小公倍数
     */
    public static long minCommonMultiple(long n1, long n2) {
        return n1 * n2 / maxCommonDivisor(n1, n2);
    }

    /**
     * 5000位--6秒
     */
    public static String getPai(int length, int eachLineNumber) {
        int n = (int) (length * 1.5);
        int[] temp = new int[n];
        int[] pai = new int[n];
        int i, count = 0, up = 2, down = 5, result, leave;
        boolean finish = false;

        //初始化
        for (i = 0; i < n; i++) {
            pai[i] = 0;
            temp[i] = 6;
        }
        pai[0] = 2;
        temp[0] = 0;

        while (!finish && count++ < 1000000) {
            for (i = n - 1, leave = 0; i >= 0; i--)/*累加*/ {
                result = pai[i] + temp[i] + leave;
                pai[i] = result % 10;
                leave = result / 10;
            }

            for (i = n - 1, leave = 0; i >= 0; i--)/*乘分子*/ {
                result = temp[i] * up + leave;
                temp[i] = result % 10;
                leave = result / 10;
            }

            for (i = 0, leave = 0; i < n; i++)/*除分母*/ {
                result = temp[i] + leave * 10;
                temp[i] = result / down;
                leave = result % down;
            }

            finish = true;

            for (i = 0; i < n; i++)/*判断temp[]是否全为0,是则结束*/ {
                if (temp[i] != 0) {
                    finish = false;
                }
            }

            up++;
            down += 2;
        }

        StringBuilder builder = new StringBuilder();
        for (i = 0; i < length; i++) {
            builder.append(pai[i]);
            if (i == 0) {
                builder.append(".");
                continue;
            }
            if ((i + 1) % eachLineNumber == 0)
                builder.append("\r\n");
        }

        return builder.toString();
    }

    /**
     * 使接近0和接近100的这两边变缓慢。相当于3次方对称点在（50，50）的抛物线图，
     * 输入scale为横坐标，输出scale为纵坐标
     *
     * @param scale
     * @return 接近0和接近100的这两边变缓慢的新的scale
     */
    public static int toPowerScale(int scale) {
        return (int) ((scale - 50) * (scale - 50) * (scale - 50) / 2500.0 + 50);
    }

    /**
     * 得到left至right范围内的一个随机整数（包括left和right）。注意当left>right时返回0。
     */
    public static int random(int left, int right) {
        if (left > right || left < 0 || right <= 0) return 0;
        Random random = new Random();
        int i = random.nextInt(right - left + 1);//例如random.nextInt(10)，会返回0至9的一个随机整数
        return i + left;
    }

    /**
     * 得到left至right范围内的n个不重复随机整数（包括left和right）。
     *
     * @return 当left>right或n过大时返回null。
     */
    public static List<Integer> randomList(int left, int right, int n) {
        if (left > right || left < 0 || right <= 0) return null;
        if (right - left <= n - 2) return null;
        List<Integer> list = new ArrayList<>();
        while (n > 0) {
            int random = random(left, right);
            boolean exists = false;
            for (int i : list) {
                if (i == random) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                list.add(random);
                n--;
            }
        }
        return list;
    }

    public static int pow(int a, int b) {
        if (a <= 0 || b < 0) {
            return -1;
        }
        int result = 1;
        for (int i = 1; i <= b; i++) {
            result *= a;
        }
        return result;
    }

}
