package com.wangrg.java_lib.data_structure;

import java.util.ArrayList;
import java.util.List;

/**
 * by 王荣俊 on 2016/10/6.
 */
public class PairList<T1, T2> {

    private List<Entity> entityList;

    public PairList() {
        entityList = new ArrayList<>();
    }

    public T1 getLeft(int index) {
        return entityList.get(index).getT1();
    }

    public T2 getRight(int index) {
        return entityList.get(index).getT2();
    }

    public void add(T1 t1, T2 t2) {
        entityList.add(new Entity(t1, t2));
    }

    public int size() {
        return entityList.size();
    }

    public void remove(int index) {
        entityList.remove(index);
    }

    private class Entity {
        private T1 t1;
        private T2 t2;

        Entity(T1 t1, T2 t2) {
            this.t1 = t1;
            this.t2 = t2;
        }

        T1 getT1() {
            return t1;
        }

        T2 getT2() {
            return t2;
        }

    }
}
