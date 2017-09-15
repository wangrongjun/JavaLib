package com.wangrg.math.encrypt.rsa;

import java.math.BigInteger;

/**
 * by wangrongjun on 2016/12/1.
 */
public class RSAData {
    private BigInteger e;
    private BigInteger n;
    private BigInteger clear;

    public RSAData(BigInteger e, BigInteger n, BigInteger clear) {
        this.e = e;
        this.n = n;
        this.clear = clear;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getClear() {
        return clear;
    }
}
