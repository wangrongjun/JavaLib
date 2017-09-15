package com.wangrg.demo.calculator.datastruct;

import com.wangrg.demo.calculator.constant.C;
import com.wangrg.demo.calculator.util.Util;

import java.util.ArrayList;


public class Expression {
    public BiTree exprTree;

    public Expression() {
        exprTree = new BiTree();
    }

    /**
     * @param expr
     * @param isPreExpresson true代表前缀表达式，false代表中缀表达式
     */
    public Expression(String expr, boolean isPreExpresson) {
        readExpr(expr, isPreExpresson);
    }

    public void readExpr(String expr, boolean isPreExpresson) {
        exprTree = new BiTree(expr, isPreExpresson);
    }

    public String writePreExpr() {
        final StringBuilder builder = new StringBuilder();
        BiTree.preOrderTraverse(exprTree, new BiTree.IVisit() {

            @Override
            public void visit(BiTree tree) {
                builder.append(tree.data + " ");
            }
        });

        return builder.toString();

    }

    public String writeInExpr() {
        String expr = BiTree.inOrderTraverseForMidExpression(exprTree);
        int len = expr.length();
        if (len <= 2) {
            return expr;
        }
        return expr.substring(1, len - 1);

    }

    public String writePostExpr() {
        final StringBuilder builder = new StringBuilder();
        BiTree.postOrderTraverse(exprTree, new BiTree.IVisit() {

            @Override
            public void visit(BiTree tree) {
                builder.append(tree.data + " ");
            }
        });

        return builder.toString();
    }

    public void assign(final String v, final String c) {
        BiTree.preOrderTraverse(exprTree, new BiTree.IVisit() {

            @Override
            public void visit(BiTree tree) {
                if (tree.data.equals(v)) {
                    tree.data = c + "";
                }
            }
        });
    }

    // 计算
    public double value() throws Exception {
        final ArrayList<String> expression = new ArrayList<String>();

        BiTree.postOrderTraverse(exprTree, new BiTree.IVisit() {

            @Override
            public void visit(BiTree tree) {
                expression.add(tree.data);
            }
        });

        try {
            return Util.calculatePostExpression(expression);
        } catch (Exception e) {
            throw e;
        }
    }

    // 合并两个表达式
    public static Expression compoundExpr(String sign, Expression expr1,
                                          Expression expr2) {
        Expression expr = new Expression();
        expr.exprTree.data = sign;
        expr.exprTree.lchild = expr1.exprTree;
        expr.exprTree.rchild = expr2.exprTree;
        return expr;
    }

    // 合并常数项
    public void mergeConst() {
        BiTree.preOrderTraverse(exprTree, new BiTree.IVisit() {

            @Override
            public void visit(BiTree tree) {
                if (SignsManager.isSign(tree.data)) {
                    if (tree.lchild != null && Util.isNumber(tree.lchild.data)
                            && tree.rchild != null
                            && Util.isNumber(tree.rchild.data)) {
                        double n1 = Double.parseDouble(tree.lchild.data);
                        double n2 = Double.parseDouble(tree.rchild.data);
                        Util.calculateBySign(C.binDir,
                                SignsManager.getSignByStrSign(tree.data), n1,
                                n2);
                    }
                }
            }
        });
    }
}
