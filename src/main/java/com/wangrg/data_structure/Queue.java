package com.wangrg.data_structure;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/5/10.
 */
public class Queue<T> {

    private List<T> list;

    public Queue() {
        list = new ArrayList<>();
    }

    public void add(T element) {
        list.add(element);
    }

    public T poll() {
        T element = list.get(0);
        list.remove(0);
        return element;
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public int size() {
        return list.size();
    }

}
