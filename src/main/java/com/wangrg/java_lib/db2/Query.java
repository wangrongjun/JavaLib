package com.wangrg.java_lib.db2;

import java.util.Arrays;
import java.util.List;

/**
 * by wangrongjun on 2017/6/17.
 */

public class Query {

    private Where where = null;
    /**
     * 递归查询外键对象到第level层为止。
     */
    private int maxQueryForeignKeyLevel = 32;
    /**
     * 查询中应该忽略的变量名列表。如果查到某个@Reference对象
     * 存在于忽略列表当中，就忽略（但还是会查询该对象的id属性）。
     */
    private List<String> ignoreReferenceList;
    /**
     * 若不空，则查询外键对象（该外键对象不存在于ignoreReferenceList中）中存在于requiredList的变量。
     */
    private List<String> requiredReferenceVariableList;
    /**
     * 相对于第一行记录的偏移量。例如offset=0指向第一行，offset=1指向第二行。
     */
    private int offset = 0;
    /**
     * 返回的记录数量。如果为0，则忽略（sql语句不包含limit）。
     */
    private int rowCount = 0;
    /**
     * 按照指定的字段排序。如果前面有负号-，则倒序排序。
     */
    private String[] orderBy = null;

    public static Query build(Where where) {
        return new Query().where(where);
    }

    public Query where(Where where) {
        this.where = where;
        return this;
    }

    public Query maxQueryForeignKeyLevel(int maxQueryForeignKeyLevel) {
        this.maxQueryForeignKeyLevel = maxQueryForeignKeyLevel;
        return this;
    }

    public Query ignore(String... ignoreReferenceName) {
        ignoreReferenceList = Arrays.asList(ignoreReferenceName);
        return this;
    }

    public Query required(String... requiredReferenceVariableName) {
        requiredReferenceVariableList = Arrays.asList(requiredReferenceVariableName);
        return this;
    }

    public Query limit(int offset, int rowCount) {
        this.offset = offset;
        this.rowCount = rowCount;
        return this;
    }

    public Query orderBy(String... orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Where getWhere() {
        return where;
    }

    public int getMaxQueryForeignKeyLevel() {
        return maxQueryForeignKeyLevel;
    }

    public int getOffset() {
        return offset;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String[] getOrderBy() {
        return orderBy;
    }

    public List<String> getIgnoreReferenceList() {
        return ignoreReferenceList;
    }

    public List<String> getRequiredReferenceVariableList() {
        return requiredReferenceVariableList;
    }
}
