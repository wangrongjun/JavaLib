package com.wangrj.java_lib.hibernate;

import com.wangrj.java_lib.java_util.TextUtil;

/**
 * by wangrongjun on 2017/10/7.
 */
public class Q {

    private Where where = null;
    /**
     * 相对于第一行记录的偏移量。例如offset=0指向第一行，offset=1指向第二行。
     * 如果小于0，则忽略（sql语句不包含limit）。
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

    public static Q where(Where where) {
        Q q = new Q();
        q.setWhere(where);
        return q;
    }

    /**
     * 返回如" order by name desc,age,id desc"
     * 如果orderBy为空或长度为0，返回""
     * 如果orderBy有元素，但所有元素都为空或长度为0，返回""
     */
    public static String createOrderBy(String[] orderBy) {
        if (orderBy == null || orderBy.length == 0) {
            return "";
        }
        String sql = "";
        for (String s : orderBy) {
            if (TextUtil.isEmpty(s)) {//防止orderBy数组不为空，但元素为空的情况
                continue;
            }
            if (s.startsWith("-")) {
                sql += s.substring(1) + " desc,";
            } else {
                sql += s + ",";
            }
        }
        if (sql.length() > 0) {
            sql = " order by " + sql;
            sql = sql.substring(0, sql.length() - 1);//去掉最后多余的逗号
        }
        return sql;
    }

    public Q limit(int offset, int rowCount) {
        this.offset = offset;
        this.rowCount = rowCount;
        return this;
    }

    public Q orderBy(String... orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
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

}
