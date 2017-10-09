package com.wangrg.java_lib.math.encrypt.rsa;

import com.wangrg.java_lib.java_util.MathUtil;

import java.math.BigInteger;

/**
 * by wangrongjun on 2016/12/1.
 */
public class RSA {

    public static String createMessage;

    private BigInteger p1;
    private BigInteger p2;
    private BigInteger e;
    private BigInteger n;
    private BigInteger d;

    public RSA(int bit) {

        createMessage = "加密位数：" + bit + "位\n";
        createMessage += "1.生成两个大素数\n";

//        1.生成两个大素数
        p1 = RSAUtil.createBigPrime(bit);
        p2 = RSAUtil.createBigPrime(bit);

        createMessage += "大素数p1=" + p1 + "\n";
        createMessage += "大素数p2=" + p2 + "\n";

//        2.求n
        n = p1.multiply(p2);

        createMessage += "公开密钥n=p1*p2=" + n + "\n";

//        3.求fn和e
        BigInteger fn = p1.subtract(BigInteger.ONE).//fn=(p1-1)*(p2-1)
                multiply(p2.subtract(BigInteger.ONE));
        e = new BigInteger("3");
        while (fn.mod(e).compareTo(BigInteger.ONE) != 0) {//while(e % fn != 1) 保证e与fn互质
            e = e.add(BigInteger.ONE);
        }

        createMessage += "加密密钥e=" + e + "\n";

//        4.求d
        d = RSAUtil.modReverse(e, fn);

        createMessage += "私有密钥d=e^-1 mod n=" + d + "\n";

        System.out.println("p1=" + p1);
        System.out.println("p2=" + p2);
        System.out.println("n=" + n);
        System.out.println("fn=" + fn);
        System.out.println("e=" + e);
        System.out.println("d=" + d);
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }

    /**
     * RSA加密
     *
     * @param data 其中的clear要小于n
     * @return 返回密文。若clear不小于n，返回null
     */
    public static BigInteger encode(RSAData data) {
        BigInteger clear = data.getClear();
        BigInteger e = data.getE();
        BigInteger n = data.getN();
        if (clear.compareTo(n) >= 0) {//if(clear>=n)
            return null;
        }
        return MathUtil.indexModule(clear, e, n);
    }

    public BigInteger decode(BigInteger cipher) {
        return MathUtil.indexModule(cipher, d, n);
    }

}
