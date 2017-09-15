package com.wangrg.demo.calculator.datastruct;

public class SqStack<E> {

    private E[] element;
    private int pos;
    private int size;

    public SqStack(E[] signs, int size) {
        this.element = signs;
        pos = 0;
        this.size = size;
    }

    public void push(E e) {
        if (pos < size) {
            element[pos++] = e;
        }
    }

    public E pop() {
        if (pos > 0) {
            return element[--pos];
        } else {
            return null;
        }
    }

    public E lastElement() {
        if (pos > 0) {
            return element[pos - 1];
        } else {
            return null;
        }
    }

    public boolean empty() {
        return pos == 0;
    }

}
