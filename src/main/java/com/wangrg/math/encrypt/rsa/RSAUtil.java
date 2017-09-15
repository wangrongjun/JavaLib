package com.wangrg.math.encrypt.rsa;

import com.wangrg.java_util.MathUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/11/29.
 */
public class RSAUtil {

    /**
     * 判断是否为大素数，通过输入不同的testNumberList，多次调用可提高正确性。
     * http://www.cnblogs.com/Norlan/p/5350243.html
     * <p/>
     * 测试13是否素数（以2为底）：
     * 2^12 mod 13 = 1
     * 2^6 mod 13 = 12 = 13-1（断定是素数）
     * <p/>
     * 测341是否素数（以2为底）：
     * 2^340 mod 341 = 1
     * 2^170 mod 341 = 1
     * 2^85 mod 341 = 32 != 1 or 340（85已经不能再拆出2了，断定不是素数）
     *
     * @param testNumberList 2到n-1的任意数的数组，用作素数测试。
     */
    private static boolean isPrime(BigInteger m, List<BigInteger> testNumberList) {

        if (m.equals(new BigInteger("1"))) {
            return false;
        }

        if (m.equals(new BigInteger("2"))) {
            return true;
        }

        for (BigInteger a : testNumberList) {
            //如果是上面素数的其中一个，返回true。随机生成的话，这里就不需要。
            if (m.equals(a)) {
                return true;
            }
            BigInteger b = m.subtract(BigInteger.ONE);
            b = b.multiply(new BigInteger("2"));// b = (m - 1)*2
            while (b.mod(new BigInteger("2")).equals(BigInteger.ZERO)) {// while(b % 2 == 0)
                b = b.divide(new BigInteger("2"));// b = b/2
                BigInteger indexModule = MathUtil.indexModule(a, b, m);
                // if(indexModule == m-1)
                if (indexModule.equals(new BigInteger(m.subtract(BigInteger.ONE).toString()))) {
                    return true;
                } else if (!indexModule.equals(BigInteger.ONE)) {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 生成大素数
     *
     * @param n 位数
     */
    public static BigInteger createBigPrime(int n) {

        List<BigInteger> testNumberList = new ArrayList<>();//先构造默认必须测试的数
        testNumberList.add(new BigInteger("2"));
        testNumberList.add(new BigInteger("3"));
        testNumberList.add(new BigInteger("5"));
        testNumberList.add(new BigInteger("7"));
        testNumberList.add(new BigInteger("11"));
        testNumberList.add(new BigInteger("13"));
        testNumberList.add(new BigInteger("17"));
        testNumberList.add(new BigInteger("19"));
        testNumberList.add(new BigInteger("23"));
        testNumberList.add(new BigInteger("29"));

        if (n >= 10) {//如果是10位数以上，再构造从30到n-1的100个随机测试数
            for (int i = 0; i < 100; i++) {
                StringBuilder builder = new StringBuilder();
                int nn = MathUtil.random(2, n - 1);//新测试数的位数
                for (int j = 0; j < nn; j++) {
                    builder.append(MathUtil.random(0, 9));
                }
                testNumberList.add(new BigInteger(builder.toString()));
            }
        }

//        DebugUtil.printlnEntity(testNumberList);

        while (true) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (i == n - 1) {//最后一位数
                    int a = MathUtil.random(0, 4) * 2 + 1;//a为奇数
                    builder.append(a);
                } else {
                    builder.append(MathUtil.random(0, 9));
                }
            }
//            System.out.println(builder.toString());
            if (isPrime(new BigInteger(builder.toString()), testNumberList)) {
//                System.out.println("is prime\n");
                return new BigInteger(builder.toString());
            }
//            System.out.println("not prime\n");
        }
    }

    /**
     * 求 ed mod n = 1中的d，要求e与n互质
     * <p/>
     * RSA算法中利用欧几里得算法求d详细过程  http://blog.sina.com.cn/s/blog_4fcd1ea30100yh3a.html
     * <p/>
     * 过程如下：
     * （4）则私钥d应该满足：79*d mod 3220 = 1；
     * 那么这个式子（4）如何解呢？这里就要用到欧几里得算法（又称辗转相除法），解法如下：
     * （a）式子（4）可以表示成79*d-3220*k=1（其中k为正整数）；
     * （b）将3220对79取模得到的余数60代替3220，则变为79*d-60*k=1；
     * （c）同理，将79对60取模得到的余数19代替79，则变为19*d-60*k=1；
     * （d）同理，将60对19取模得到的余数3代替60，则变为19*d-3*k=1；
     * （e）同理，将19对3取模得到的余数1代替19，则变为d-3*k=1；
     * 当d的系数最后化为1时（当k的系数n最后化为1时（，
     * 令k=0，代入（e）式中，得d=1（令d=0，代入（e1）式中，得k=1）；
     * 将d=1代入（d）式，得k=6；（如此类推。。。）
     * 将k=6代入（c）式，得d=19；
     * 将d=19代入（b）式，得k=25；
     * 将k=25代入（a）式，得d=1019，这个值即我们要求的私钥d的最终值。
     */
    public static BigInteger modReverse(BigInteger e, BigInteger n, boolean returnD) {

        // ed - nk = 1
        BigInteger k = null;
        BigInteger d = null;
        if (e.equals(BigInteger.ONE)) {
            k = BigInteger.ZERO;
        } else if (n.equals(BigInteger.ONE)) {
            d = BigInteger.ZERO;
        } else if (e.compareTo(n) == -1) {//如果e < n
            if (returnD) {
                k = modReverse(e, n.mod(e), !returnD);
            } else {
                d = modReverse(e, n.mod(e), !returnD);
            }
        } else {
            if (returnD) {
                k = modReverse(e.mod(n), n, !returnD);
            } else {
                d = modReverse(e.mod(n), n, !returnD);
            }
        }

        if (returnD) {
            // return | (1 + n * k) / e |
            return n.multiply(k).add(BigInteger.ONE).divide(e).abs();
        } else {
            // return | (ed - 1) / n |
            return e.multiply(d).subtract(BigInteger.ONE).divide(n).abs();
        }

    }

    /**
     * 求 ed mod n = 1中的d
     */
    public static BigInteger modReverse(BigInteger e, BigInteger n) {
        return modReverse(e, n, true);
    }

}
