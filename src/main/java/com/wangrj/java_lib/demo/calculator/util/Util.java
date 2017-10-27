package com.wangrj.java_lib.demo.calculator.util;

import com.wangrj.java_lib.demo.calculator.bean.Sign;
import com.wangrj.java_lib.demo.calculator.constant.C;
import com.wangrj.java_lib.demo.calculator.datastruct.BiTree;
import com.wangrj.java_lib.demo.calculator.datastruct.SignsManager;
import com.wangrj.java_lib.demo.calculator.datastruct.SqStack;
import com.wangrj.java_lib.demo.calculator.bean.Sign;
import com.wangrj.java_lib.demo.calculator.constant.C;
import com.wangrj.java_lib.demo.calculator.datastruct.BiTree;
import com.wangrj.java_lib.demo.calculator.datastruct.SqStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Util {

    public static boolean isNumber(String s) {
        try {
            double a = Double.parseDouble(s);
            return a >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 输入如："a=1 b=2 c=3" 的字符串，从而对tree的变量赋值
     */
    public static void assignParameterFromText(BiTree tree, String text)
            throws Exception {

        String[] parameters = text.trim().split("[ ]+");
        final HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            String[] s = parameters[i].split("[=]");
            if (s.length <= 1) {
                throw new Exception();
            }
            map.put(s[0], s[1]);
        }

        BiTree.preOrderTraverse(tree, new BiTree.IVisit() {

            @Override
            public void visit(BiTree tree) {
                if (map.containsKey(tree.data)) {
                    tree.data = map.get(tree.data);
                }
            }
        });
    }

    // 根据后缀表达式求值
    public static double calculatePostExpression(ArrayList<String> expression)
            throws Exception {

        Stack<String> stack = new Stack<String>();

        for (int i = 0; i < expression.size(); i++) {

            String s = expression.get(i);

            if (SignsManager.isSign(s)) {

                Sign sign = SignsManager.getSignByStrSign(s);
                int operateNum = sign != null ? sign.getOperateNum() : 0;
                if (operateNum != 0) {
                    try {
                        double n1 = 0, n2 = 0;
                        n2 = Double.parseDouble(stack.pop());

                        if (operateNum == 2) {
                            n1 = Double.parseDouble(stack.pop());
                        } else {
                            n1 = n2;
                        }
                        stack.push(calculateBySign(C.binDir, sign, n1, n2) + "");
                    } catch (Exception e) {
                        throw e;
                    }

                } else {
                    // 未定义的运算符
                    Exception e = new Exception("undefine sign");
                    throw e;
                }

            } else {
                stack.push(s);

            }

        }

        if (stack.empty()) {
            throw new Exception();
        }

        try {
            return Double.parseDouble(stack.pop());

        } catch (Exception e) {

            throw e;
        }

    }

    // 调用Fun.class中保存的函数计算结果
    public static double calculateBySign(String binDir, Sign sign, double n1,
                                         double n2) {
        try {
            String midCode = sign.getCode();
            Compiler.compile(C.preCode + midCode + C.postCode);

            Compiler.FunInterface funInterface = Compiler.findInstanceByClassName(
                    C.classFileName, binDir);

            return funInterface.fun(n1, n2);

        } catch (Exception e) {
            System.out.println("Calculator:calculateBySign: error");
            return -1;
        }
    }

    public static ArrayList<String> spiltExpression(String expression) {

        ArrayList<String> arrayExpression = new ArrayList<String>();

        StringBuilder builder = new StringBuilder();// 用来存储一个多位数，每次追加一位

        char[] e = expression.toCharArray();

        for (int i = 0; i < e.length; i++) {

            if (SignsManager.isSign(e[i] + "")) {
                if (builder.length() > 0) {
                    arrayExpression.add(builder.toString());
                    builder = new StringBuilder();
                }
                arrayExpression.add(e[i] + "");

            } else {
                builder.append(e[i] + "");
                if (i == e.length - 1) {
                    arrayExpression.add(builder.toString());

                }
            }
        }

        return arrayExpression;

    }

    /**
     * 1.若为（，进栈 2.若为），出栈到（为止，过程中若stack为空，则括号不匹配 3.若为数字，直接输出
     * 4.若为运算符，且该运算符优先级比栈顶元素低，则栈顶元素出栈，直到优先级 相等或遇到左括号才结束出栈，最后把该运算入栈 .如：进加之前
     * 有个乘，则乘要出栈 stack: - * （ * / 当前准备入栈的是+， 则先出栈/ *
     * 5.若为运算符，且该运算符优先级比栈顶元素高或1相等，直接进栈
     */
    public static ArrayList<String> inToPostExpression(ArrayList<String> inExpr)
            throws Exception {

        ArrayList<String> postExpr = new ArrayList<String>();

        SqStack<String> stack = new SqStack<String>(
                new String[C.SQSTACK_MAXSIZE], C.SQSTACK_MAXSIZE);

        for (int i = 0; i < inExpr.size(); i++) {

            String s = inExpr.get(i);

            String sign;

            if (SignsManager.isSign(s)) {

                if (s.equals("(")) {
                    stack.push(s);

                } else if (s.equals(")")) {
                    if (stack.empty()) {// 若括号不匹配
                        throw new Exception("() dismatch");
                    }

                    while (!(sign = stack.pop()).equals("(")) {
                        postExpr.add(sign);
                    }

                } else {

                    while (!stack.empty()) {
                        sign = stack.lastElement();

                        if (sign.equals("(")) {
                            break;
                        }

                        // 若栈顶运算符优先级比当前运算符小
                        if (SignsManager.getPriorBySign(sign) <= SignsManager
                                .getPriorBySign(s)) {
                            break;
                        }

                        postExpr.add(sign);

                        stack.pop();

                    }

                    stack.push(s);

                }

            } else {
                postExpr.add(s);
            }

        }

        while (!stack.empty()) {
            postExpr.add(stack.pop());
        }

        return postExpr;

    }

    public static ArrayList<String> inToPreExpression(ArrayList<String> inExpr) {
        ArrayList<String> inExprOpposite = new ArrayList<String>();

        // 中序取逆（逆中序）注意左括号变右括号，右括号变左括号
        for (int i = inExpr.size() - 1; i >= 0; i--) {
            String s = inExpr.get(i);
            if (s.equals("(")) {
                s = ")";
            } else if (s.equals(")")) {
                s = "(";
            }
            inExprOpposite.add(s);
        }

        ArrayList<String> postExprOpposite = new ArrayList<>();

        // 从逆中序获取逆后序
        try {
            postExprOpposite = inToPostExpression(inExprOpposite);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<String> preExpr = new ArrayList<String>();

        // 从逆后序获取前序
        for (int i = postExprOpposite.size() - 1; i >= 0; i--) {
            preExpr.add(postExprOpposite.get(i));
        }

        return preExpr;
    }

}
