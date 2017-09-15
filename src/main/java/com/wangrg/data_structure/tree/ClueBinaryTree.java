package com.wangrg.data_structure.tree;

/**
 * by wangrongjun on 2017/5/10.
 * 线索二叉树
 */
public class ClueBinaryTree<T> extends BinaryTree<T> {

    private boolean leftClue;// true:左指针指向某一遍历次序的前驱。false:左指针指向左孩子。
    private boolean rightClue;// true:右指针指向某一遍历次序的后驱。false:右指针指向右孩子。

    /**
     * 中序遍历线索化
     */
    public void inThreading() {
        inThreading(this);
    }

    /**
     * 中序遍历线索化 - 递归方法
     */
    private static <T> void inThreading(ClueBinaryTree<T> tree) {

    }

}
