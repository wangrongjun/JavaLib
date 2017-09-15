package com.wangrg.math.fraction;

import com.wangrg.java_util.MathUtil;

/**
 * by wangrongjun on 2016/11/3.
 * 分数类型
 * 允许分母为0，表示为NaN。如果NaN与其他任何分数进行任一运算，结果为NaN
 */
public class Fraction {
    /**
     * 分子，必须为非负
     */
    private long son;
    /**
     * 分母，必须为正整数
     */
    private long mother;
    /**
     * 是否为正数（包括0）
     */
    private boolean positive;

    public Fraction(long son, long mother) {

        if (mother == 0) {
            this.son = 0;
            this.mother = 0;
            return;
        }
        if (son == 0) {
            this.son = 0;
            this.mother = 1;
            this.positive = true;
            return;
        }

        if (son < 0) {
            if (mother < 0) {
                son = -son;
                mother = -mother;
                this.positive = true;
            } else {
                son = -son;
                this.positive = false;
            }
        } else {//son > 0
            if (mother < 0) {
                mother = -mother;
                this.positive = false;
            } else {
                this.positive = true;
            }
        }

        long maxCommonDivisor = MathUtil.maxCommonDivisor(son, mother);
        son = son / maxCommonDivisor;
        mother = mother / maxCommonDivisor;
        this.son = son;
        this.mother = mother;
    }

    public Fraction add(Fraction fraction) {
        if (this.getMother() == 0 || fraction.getMother() == 0) {
            return new Fraction(0, 0);
        }
        long minCommonMultiple = MathUtil.minCommonMultiple(this.getMother(), fraction.getMother());
        long son1 = this.getSon() * minCommonMultiple / this.getMother();
        son1 *= (this.isPositive() ? 1 : -1);
        long son2 = fraction.getSon() * minCommonMultiple / fraction.getMother();
        son2 *= (fraction.isPositive() ? 1 : -1);
        return new Fraction(son1 + son2, minCommonMultiple);
    }

    public Fraction subtract(Fraction fraction) {
        if (this.getMother() == 0 || fraction.getMother() == 0) {
            return new Fraction(0, 0);
        }
        long minCommonMultiple = MathUtil.minCommonMultiple(this.getMother(), fraction.getMother());
        long son1 = this.getSon() * minCommonMultiple / this.getMother();
        son1 *= (this.isPositive() ? 1 : -1);
        long son2 = fraction.getSon() * minCommonMultiple / fraction.getMother();
        son2 *= (fraction.isPositive() ? 1 : -1);
        return new Fraction(son1 - son2, minCommonMultiple);
    }

    //TODO 可以进行优化，在分子与分子相乘，分母与分母相乘之前进行预处理，避免数据溢出
    public Fraction multiply(Fraction fraction) {
        if (this.getMother() == 0 || fraction.getMother() == 0) {
            return new Fraction(0, 0);
        }
        long son = this.getSon() * fraction.getSon();
        long mother = this.getMother() * fraction.getMother();
        boolean positive = !this.isPositive() ^ fraction.isPositive();
        if (!positive) {
            son = -son;
        }
        return new Fraction(son, mother);
    }

    public Fraction divide(Fraction fraction) {
        return multiply(fraction.reverse());
    }

    /**
     * 如果为NaN，返回1（无穷大）
     */
    public int compare(Fraction fraction) {
        if (mother == 0) {
            return 1;
        }
        if (this.getSon() == fraction.getSon() &&
                this.getMother() == fraction.getMother() &&
                this.isPositive() == fraction.isPositive()) {
            return 0;
        }
        double n1 = (this.isPositive() ? 1 : -1) * this.getSon() / this.getMother();
        double n2 = (fraction.isPositive() ? 1 : -1) * fraction.getSon() / fraction.getMother();
        return n1 < n2 ? -1 : 1;
    }

    public Fraction reverse() {
        if (!isPositive()) {
            return new Fraction(mother, -son);
        } else {
            return new Fraction(mother, son);
        }
    }

    @Override
    public String toString() {
        if (getMother() == 0) {
            return "NaN";
        }
        if (getSon() == 0) {
            return "0";
        }
        if (getMother() == 1) {
            return (isPositive() ? "" : "-") + getSon();
        }
        return (isPositive() ? "" : "-") + getSon() + "/" + getMother();
    }

    public long toLong() {
        return son / mother;
    }

    public long getSon() {
        return son;
    }

    public long getMother() {
        return mother;
    }

    public boolean isPositive() {
        return positive;
    }

    public static void test() {
        for (int i = 0; i < 20; i++) {

            int son1 = MathUtil.random(-10, 10);
            int mother1 = MathUtil.random(-10, 10);
            Fraction fraction1 = new Fraction(son1, mother1);

            int son2 = MathUtil.random(-10, 10);
            int mother2 = MathUtil.random(-10, 10);
            Fraction fraction2 = new Fraction(son2, mother2);

            System.out.println(son1 + "/" + mother1 + "\t" + son2 + "/" + mother2);
            System.out.println(fraction1 + " / " + fraction2 + " = " + fraction1.divide(fraction2));
            System.out.println();
        }
    }
}
