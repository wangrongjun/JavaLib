package com.wangrj.java_lib.hibernate;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/10/7.
 */
public class Where {

    /**
     * 查询条件的模式（相等，不相等，小于，小于等于，大于，大于等于，模糊查询）
     */
    public enum QueryMode {
        EQUAL,
        NOT_EQUAL,
        LESS,
        LESS_EQUAL,
        MORE,
        MORE_EQUAL,
        LIKE,
        NOT_LIKE,
        IS_NULL,
        IS_NOT_NULL
    }

    /**
     * 当前查询条件与下一个查询条件的逻辑（and,or）
     * 注意：若查询条件Equation为最后一个，则不存在下一个查询条件，也就没有所谓的逻辑，忽略该变量
     */
    public enum QueryLogic {
        AND,
        OR,
    }

    private List<Expression> expressionList;

    public Where() {
        expressionList = new ArrayList<>();
    }

    public int size() {
        return expressionList.size();
    }

    public Where equal(String name, Object value) {
        expressionList.add(new Expression(name, value, QueryMode.EQUAL, QueryLogic.AND));
        return this;
    }

    public Where notEqual(String name, Object value) {
        expressionList.add(new Expression(name, value, QueryMode.NOT_EQUAL, QueryLogic.AND));
        return this;
    }

    public Where less(String name, Object value) {
        expressionList.add(new Expression(name, value, QueryMode.LESS, QueryLogic.AND));
        return this;
    }

    public Where lessEqual(String name, Object value) {
        expressionList.add(new Expression(name, value, QueryMode.LESS_EQUAL, QueryLogic.AND));
        return this;
    }

    public Where more(String name, Object value) {
        expressionList.add(new Expression(name, value, QueryMode.MORE, QueryLogic.AND));
        return this;
    }

    public Where moreEqual(String name, Object value) {
        expressionList.add(new Expression(name, value, QueryMode.MORE_EQUAL, QueryLogic.AND));
        return this;
    }

    public Where isNull(String name) {
        expressionList.add(new Expression(name, null, QueryMode.IS_NULL, QueryLogic.AND));
        return this;
    }

    public Where isNotNull(String name) {
        expressionList.add(new Expression(name, null, QueryMode.IS_NOT_NULL, QueryLogic.AND));
        return this;
    }

    public Where like(String name, String value) {
        expressionList.add(new Expression(name, value, QueryMode.LIKE, QueryLogic.AND));
        return this;
    }

    public Where notLike(String name, String value) {
        expressionList.add(new Expression(name, value, QueryMode.NOT_LIKE, QueryLogic.AND));
        return this;
    }

    public Where and() {
        expressionList.get(size() - 1).queryLogic = QueryLogic.AND;
        return this;
    }

    public Where or() {
        expressionList.get(size() - 1).queryLogic = QueryLogic.OR;
        return this;
    }

    public static Where eq(String whereName, Object whereValue) {
        return new Where().equal(whereName, whereValue);
    }

    /**
     * 返回 " where username='wang' and password='123' or gender='1' or nickname like '%abc%'"
     * 如果expressionList为空或长度为0，返回 ""
     */
    @Override
    public String toString() {
        if (size() == 0) {
            return "";
        }
        String sql = "";
        for (int i = 0; i < expressionList.size(); i++) {
            Expression expression = expressionList.get(i);
            String name = expression.name;
            Object value = expression.value;
            QueryMode mode = expression.queryMode;
            // username='null' 无意义，报错
            if (value == null && mode != QueryMode.IS_NULL && mode != QueryMode.IS_NOT_NULL) {
                throw new IllegalArgumentException("The value of " + name + " is null");
            }
            // TODO 使用占位符（？）或定义参数
            switch (expression.queryMode) {
                case EQUAL:
                    sql += name + " = '" + value + "'";
                    break;
                case NOT_EQUAL:
                    sql += name + " != '" + value + "'";
                    break;
                case LESS:
                    sql += name + " < '" + value + "'";
                    break;
                case LESS_EQUAL:
                    sql += name + " <= '" + value + "'";
                    break;
                case MORE:
                    sql += name + " > '" + value + "'";
                    break;
                case MORE_EQUAL:
                    sql += name + " >= '" + value + "'";
                    break;
                case IS_NULL:
                    sql += name + " IS NULL";
                    break;
                case IS_NOT_NULL:
                    sql += name + " IS NOT NULL";
                    break;
                case LIKE:
                    sql += name + " LIKE '" + value + "'";
                    break;
                case NOT_LIKE:
                    sql += name + " NOT LIKE '" + value + "'";
                    break;
            }
            // 如果不是最后一个查询条件，添加查询逻辑
            if (i < size() - 1) {
                switch (expression.queryLogic) {
                    case AND:
                        sql += " AND ";
                        break;
                    case OR:
                        sql += " OR ";
                        break;
                }
            }
        }
        if (sql.length() > 0) {
            sql = " WHERE " + sql;
        }
        return sql;
    }

    private class Expression {
        String name;
        Object value;
        QueryMode queryMode;
        QueryLogic queryLogic;

        Expression(String name, Object value, QueryMode queryMode,
                   QueryLogic queryLogic) {
            this.name = name;
            this.value = value;
            this.queryMode = queryMode;
            this.queryLogic = queryLogic;
        }
    }

}
