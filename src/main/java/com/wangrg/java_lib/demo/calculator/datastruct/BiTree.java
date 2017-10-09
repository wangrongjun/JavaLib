package com.wangrg.java_lib.demo.calculator.datastruct;

import com.wangrg.java_lib.demo.calculator.constant.C;
import com.wangrg.java_lib.demo.calculator.util.Util;

import java.util.ArrayList;


public class BiTree {
    public String data = null;
    public BiTree lchild = null;
    public BiTree rchild = null;

    public BiTree() {

    }

    /**
     * preE以空格为分隔符，如："* + 3.5 5 - 6.0 4"
     *
     * @param strExpr
     * @param isPreExpresson true代表前缀表达式，false代表中缀表达式
     */
    public BiTree(String strExpr, boolean isPreExpresson) {

        SqStack<String> stack = new SqStack<String>(
                new String[C.SQSTACK_MAXSIZE], C.SQSTACK_MAXSIZE);

        ArrayList<String> preExpr = new ArrayList<>();

        BiTree tree;

        // 先把字符串表达式转换为前缀数组
        if (isPreExpresson) {// 前缀则用空格分割后直接顺序存入前缀数组

            String[] exprs = strExpr.trim().split("[ ]+");

            for (int i = 0; i < exprs.length; i++) {
                preExpr.add(exprs[i]);
            }

        } else {// 中序则用spilt分割为中序数组，再用inToPre转换为前缀数组

            // 先删除空格
            strExpr = strExpr.replace(" ", "");

            ArrayList<String> inExpr = Util.spiltExpression(strExpr);

            preExpr = Util.inToPreExpression(inExpr);

        }

        // 把前缀数组的反序存进stack
        for (int i = preExpr.size() - 1; i >= 0; i--) {
            stack.push(preExpr.get(i));
        }

        // 根据与前缀数组的反序的stack构造二叉树
        tree = makeBiTree(stack);

        data = tree.data;
        lchild = tree.lchild;
        rchild = tree.rchild;

    }

    /**
     * 用前缀表达式构造二叉树
     *
     * @param stack 这里的stack要先逆置。比如前缀：/ 4 + 6 3 在stack应为： 3 6 + 4 /
     * @return
     */
    private BiTree makeBiTree(SqStack<String> stack) {
        String s = stack.pop();
        BiTree tree = new BiTree();

        if (s == null) {
            return tree;
        }

        tree.data = s;
        if (SignsManager.isSign(s)) {
            tree.lchild = makeBiTree(stack);

            if (SignsManager.getOperateNumBySign(s) == 2) {
                tree.rchild = makeBiTree(stack);
            }
        }

        return tree;

    }

    public interface IVisit {
        public void visit(BiTree tree);
    }

    // 先序遍历，这里的接口相当于函数指针visit
    public static void preOrderTraverse(BiTree tree, IVisit visit) {
        if (tree == null) {
            return;
        }
        if (visit != null) {
            visit.visit(tree);
        }
        preOrderTraverse(tree.lchild, visit);
        preOrderTraverse(tree.rchild, visit);

    }

    // 中序遍历
    public static void inOrderTraverse(BiTree tree, IVisit visit) {
        if (tree == null) {
            return;
        }
        inOrderTraverse(tree.lchild, visit);
        if (visit != null) {
            visit.visit(tree);
        }
        inOrderTraverse(tree.rchild, visit);

    }

    // 特殊的中序遍历，为了得到含括号的中缀表达式
    public static String inOrderTraverseForMidExpression(BiTree tree) {
        if (tree == null) {
            return "";
        }

        String lExpr = inOrderTraverseForMidExpression(tree.lchild);
        String midExpr = tree.data;
        String rExpr = inOrderTraverseForMidExpression(tree.rchild);

        if (SignsManager.isSign(tree.data)) {
            return "(" + lExpr + midExpr + rExpr + ")";

        } else {
            return lExpr + midExpr + rExpr;

        }

    }

    // 后序遍历
    public static void postOrderTraverse(BiTree tree, IVisit visit) {
        if (tree == null) {
            return;
        }
        postOrderTraverse(tree.lchild, visit);
        postOrderTraverse(tree.rchild, visit);
        if (visit != null) {
            visit.visit(tree);
        }

    }

}
