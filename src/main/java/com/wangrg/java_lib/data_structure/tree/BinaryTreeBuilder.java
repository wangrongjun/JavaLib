package com.wangrg.java_lib.data_structure.tree;

import com.wangrg.java_lib.data_structure.CursorList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * by wangrongjun on 2017/5/9.
 */
public class BinaryTreeBuilder {

    public static class TreeExpressionErrorException extends RuntimeException {
        public TreeExpressionErrorException() {
            super("tree expression is error");
        }
    }

    /**
     * 根据先序表达式创建二叉树
     *
     * @param perOrderExpression 先序表达式，如AB#D##C##。其中#当作叶子节点的左右孩子（实际上不存在）
     */
    public static BinaryTree<String> createByPerOrderExpression(String perOrderExpression)
            throws TreeExpressionErrorException {
        char[] charArray = perOrderExpression.toCharArray();
        List<String> list = new ArrayList<>();
        for (char c : charArray) {
            if (c == '#') {
                list.add(null);
            } else {
                list.add(c + "");
            }
        }
        return createByPerOrderList(new CursorList<>(list));
    }

    /**
     * 根据先序列表创建二叉树，列表中需要插入null来充当叶子节点的左右孩子（实际上不存在）
     * <p/>
     * 如果需要输入中序/后序列表来创建二叉树，只需要改变binaryTree.setData(data)的位置即可
     *
     * @param cursorList 先序列表，如A,B,null,D,null,null,C,null,null
     */
    public static <T> BinaryTree<T> createByPerOrderList(CursorList<T> cursorList)
            throws TreeExpressionErrorException {
        if (!cursorList.hasElement()) {
            throw new TreeExpressionErrorException();
        }
        T data = cursorList.next();
        if (data == null) {
            return null;
        } else {
            BinaryTree<T> binaryTree = new BinaryTree<>();
            binaryTree.setData(data);
            binaryTree.setLeftChild(createByPerOrderList(cursorList));
            binaryTree.setRightChild(createByPerOrderList(cursorList));
            return binaryTree;
        }
    }

    /**
     * 按层次顺序创建二叉树，列表中的null表明当前对应于完全二叉树吸相同位置的节点不存在
     * <p/>
     * 1.先在nodeList后面补nul，使得size==2^k-1
     * 2.逐层构建二叉树。比如第一次循环构建根，并把根存起来。第二次循环根的两个孩子，并把两个孩子存起来，
     * 第三次循环构建四个孙子，并把四个孙子存起来。如此类推，直至队列为空或者层次顺序列表读完了。
     */
    public static <T> BinaryTree<T> createByLevelOrderList(List<T> nodeList) {

        // 1.先在nodeList后面补nul，使得size==2^k-1
        int n = 1;
        while (n < nodeList.size()) {
            n *= 2;
        }
        while (nodeList.size() < n - 1) {// 现在的n就是2^k
            nodeList.add(null);
        }

        // 2.逐层构建二叉树
        CursorList<T> cursorList = new CursorList<>(nodeList);
        BinaryTree<T> root = new BinaryTree<>();
        root.setData(cursorList.next());
        Queue<BinaryTree<T>> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty() && cursorList.hasElement()) {
            BinaryTree<T> tree = queue.poll();
            T leftData = cursorList.next();
            T rightData = cursorList.next();
            if (leftData != null) {
                BinaryTree<T> leftChild = new BinaryTree<>(leftData);
                tree.setLeftChild(leftChild);
                queue.add(leftChild);
            }
            if (rightData != null) {
                BinaryTree<T> rightChild = new BinaryTree<>(rightData);
                tree.setRightChild(rightChild);
                queue.add(rightChild);
            }
        }

        return root;
    }

    /**
     * 按层次顺序表达式创建二叉树
     *
     * @param levelOrderExpression 层次顺序表达式，如ABCDEFG###H
     */
    public static BinaryTree<String> createByLevelOrderExpression(String levelOrderExpression) {
        char[] charArray = levelOrderExpression.toCharArray();
        List<String> list = new ArrayList<>();
        for (char c : charArray) {
            if (c == '#') {
                list.add(null);
            } else {
                list.add(c + "");
            }
        }
        return createByLevelOrderList(list);
    }

}
