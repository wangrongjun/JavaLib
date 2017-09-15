package com.wangrg.data_structure.tree;

/**
 * by wangrongjun on 2017/5/10.
 */
public class Tree<T> {

    private T data;
    private Tree<T> firstChild;
    private Tree<T> nextBrother;

    public Tree() {
    }

    public Tree(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Tree<T> getFirstChild() {
        return firstChild;
    }

    public void setFirstChild(Tree<T> firstChild) {
        this.firstChild = firstChild;
    }

    public Tree<T> getNextBrother() {
        return nextBrother;
    }

    public void setNextBrother(Tree<T> nextBrother) {
        this.nextBrother = nextBrother;
    }

}
