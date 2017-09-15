package com.wangrg.data_structure;

/**
 * by wangrongjun on 2016/11/1.
 */
public class Pair<F, S> {

    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof Pair)) {
            return false;
        } else {
            Pair var2 = (Pair) var1;
            if (this.first != null) {
                if (!this.first.equals(var2.first)) {
                    return false;
                }
            } else if (var2.first != null) {
                return false;
            }

            if (this.second != null) {
                if (!this.second.equals(var2.second)) {
                    return false;
                }
            } else if (var2.second != null) {
                return false;
            }

            return true;
        }
    }

    /**
     * Compute a hash code using the hash codes of the underlying objects
     *
     * @return a hashcode of the Pair
     */
    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    @Override
    public String toString() {
        return first + "  :  " + second;
    }
}
