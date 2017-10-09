package com.wangrg.java_lib.math;

import com.wangrg.java_lib.math.fraction.Fraction;

import java.math.BigDecimal;

/**
 * by wangrongjun on 2016/10/30.
 */
public interface IOperation<T> {

    T add(T element1, T element2);

    T subtract(T element1, T element2);

    T multiply(T element1, T element2);

    T divide(T element1, T element2);

    int compare(T element1, T element2);

    /**
     * @return 零元（如0）
     */
    T getZero();

    /**
     * @return 单位元（如1）
     */
    T getUnit();

    /**
     * @return 相反数（如输入1，返回-1）
     */
    T opposite(T element);

    T clone(T element);

    IOperation<Integer> integerIOperation = new IOperation<Integer>() {
        @Override
        public Integer add(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 + element2;
        }

        @Override
        public Integer subtract(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 - element2;
        }

        @Override
        public Integer multiply(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 * element2;
        }

        @Override
        public Integer divide(Integer element1, Integer element2) {
            if (element1 == null) element1 = 0;
            if (element2 == null) element2 = 0;
            return element1 / element2;
        }

        @Override
        public int compare(Integer element1, Integer element2) {
            if (element1 > element2) return 1;
            if (element1 == element2) return 0;
            return -1;
        }

        @Override
        public Integer getZero() {
            return 0;
        }

        @Override
        public Integer getUnit() {
            return 1;
        }

        @Override
        public Integer opposite(Integer element) {
            if (element == null) element = 0;
            return -element;
        }

        @Override
        public Integer clone(Integer element) {
            int e = element;
            return e;
        }
    };

    IOperation<Double> doubleIOperation = new IOperation<Double>() {
        @Override
        public Double add(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 + element2;
        }

        @Override
        public Double subtract(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 - element2;
        }

        @Override
        public Double multiply(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 * element2;
        }

        @Override
        public Double divide(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            return element1 / element2;
        }

        @Override
        public int compare(Double element1, Double element2) {
            if (element1 == null) element1 = 0d;
            if (element2 == null) element2 = 0d;
            if (element1 > element2) return 1;
            if (element1.equals(element2)) return 0;
            return -1;
        }

        @Override
        public Double getZero() {
            return 0d;
        }

        @Override
        public Double getUnit() {
            return 1d;
        }

        @Override
        public Double opposite(Double element) {
            if (element == null) element = 0d;
            return -element;
        }

        @Override
        public Double clone(Double element) {
            double e = element;
            return e;
        }

    };

    IOperation<BigDecimal> bigDecimalIOperation = new IOperation<BigDecimal>() {
        @Override
        public BigDecimal add(BigDecimal element1, BigDecimal element2) {
            return element1.add(element2);
        }

        @Override
        public BigDecimal subtract(BigDecimal element1, BigDecimal element2) {
            return element1.subtract(element2);
        }

        @Override
        public BigDecimal multiply(BigDecimal element1, BigDecimal element2) {
            return element1.multiply(element2);
        }

        @Override
        public BigDecimal divide(BigDecimal element1, BigDecimal element2) {
            return element1.divide(element2, 10, BigDecimal.ROUND_HALF_DOWN);
        }

        @Override
        public int compare(BigDecimal element1, BigDecimal element2) {
            return element1.compareTo(element2);
        }

        @Override
        public BigDecimal getZero() {
            return BigDecimal.ZERO;
        }

        @Override
        public BigDecimal getUnit() {
            return BigDecimal.ONE;
        }

        @Override
        public BigDecimal opposite(BigDecimal element) {
            return element.multiply(new BigDecimal(-1));
        }

        @Override
        public BigDecimal clone(BigDecimal element) {
            return element.add(BigDecimal.ZERO);
        }
    };

    IOperation<Fraction> fractionIOperation = new IOperation<Fraction>() {
        @Override
        public Fraction add(Fraction element1, Fraction element2) {
            return element1.add(element2);
        }

        @Override
        public Fraction subtract(Fraction element1, Fraction element2) {
            return element1.subtract(element2);
        }

        @Override
        public Fraction multiply(Fraction element1, Fraction element2) {
            return element1.multiply(element2);
        }

        @Override
        public Fraction divide(Fraction element1, Fraction element2) {
            return element1.divide(element2);
        }

        @Override
        public int compare(Fraction element1, Fraction element2) {
            return element1.compare(element2);
        }

        @Override
        public Fraction getZero() {
            return new Fraction(0, 1);
        }

        @Override
        public Fraction getUnit() {
            return new Fraction(1, 1);
        }

        @Override
        public Fraction opposite(Fraction element) {
            long son = element.getSon();//son一定为正整数
            if (element.isPositive()) {
                return new Fraction(-son, element.getMother());
            } else {
                return new Fraction(son, element.getMother());
            }
        }

        @Override
        public Fraction clone(Fraction element) {
            long son = element.getSon();//son一定为正整数
            if (element.isPositive()) {
                return new Fraction(son, element.getMother());
            } else {
                return new Fraction(-son, element.getMother());
            }
        }
    };

}
