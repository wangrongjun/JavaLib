package com.wangrj.java_lib.data_structure;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/5/9.
 */
public class CursorList<T> {

    private List<T> list;
    private int nextPosition;

    public CursorList() {
        this.list = new ArrayList<>();
        nextPosition = 0;
    }

    public CursorList(List<T> list) {
        this.list = list;
        nextPosition = 0;
    }

    public void resetCursor() {
        nextPosition = 0;
    }

    public void append(T element) {
        list.add(element);
    }

    public boolean hasElement() {
        return nextPosition >= 0 && nextPosition < list.size();
    }

    public T previous() {
        return list.get(nextPosition--);
    }

    public T next() {
        return list.get(nextPosition++);
    }

}
