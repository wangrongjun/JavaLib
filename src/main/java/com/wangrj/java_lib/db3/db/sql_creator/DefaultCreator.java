package com.wangrj.java_lib.db3.db.sql_creator;

import com.wangrj.java_lib.data_structure.Pair;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.db3.main.UpdateSetValue;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.db2.Where;

import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public abstract class DefaultCreator implements ISqlCreator {

    @Override
    public String insertSql(String tableName, List<Pair<String, String>> nameValuePairList) {
        String nameList = "";
        String valueList = "";
        for (Pair<String, String> nameValuePair : nameValuePairList) {
            nameList += nameValuePair.first + ",";
            valueList += "" + nameValuePair.second + ",";
        }
        nameList = nameList.substring(0, nameList.length() - 1);
        valueList = valueList.substring(0, valueList.length() - 1);
        return "insert into " + tableName + "(" + nameList + ") values(" + valueList + ")";
    }

    @Override
    public String updateSql(String tableName, UpdateSetValue setValue, Where where) {
        return "update " + tableName + " set " + setValue + " where " + where;
    }

    @Override
    public String deleteSql(String tableName, Where where) {
        return "delete from " + tableName + " where " + where;
    }

    @Override
    public String queryCountSql(String tableName, Where where) {
        String sql = "select count(1) from " + tableName;
        sql += where == null || where.size() == 0 ? "" : (" where " + where);
        return sql;
    }

    protected String createUnionUniqueKeySql(String tableName, List<String> unionUniqueList) {
        // 例子：alter table SC add constraint union_uq_SC_s_c unique (student,course)
        if (unionUniqueList == null || unionUniqueList.size() == 0) {
            return null;
        }
        String uniqueKeyName = "union_uq_" + tableName;
        String uniqueFieldList = "";
        for (String uniqueFieldName : unionUniqueList) {
            uniqueKeyName += "_" + uniqueFieldName.substring(0, 1);// 联合主键名字过长的话会出错，所以使用缩写
            uniqueFieldList += uniqueFieldName + ",";
        }
        uniqueFieldList = uniqueFieldList.substring(0, uniqueFieldList.length() - 1);
        return "alter table SC add constraint " +
                uniqueKeyName + " unique (" + uniqueFieldList + ")";
    }

    /**
     * 返回如" order by name desc,age,id desc"
     */
    public static String createOrderBy(String[] orderBy) {
        if (orderBy == null || orderBy.length == 0) {
            return "";
        }
        String sql = " order by ";
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
        sql = sql.substring(0, sql.length() - 1);//去掉最后多余的逗号
        return sql;
    }

}
