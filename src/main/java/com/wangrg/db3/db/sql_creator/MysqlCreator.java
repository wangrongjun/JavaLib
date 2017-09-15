package com.wangrg.db3.db.sql_creator;

import com.wangrg.db2.Query;
import com.wangrg.db2.TableUtil;
import com.wangrg.db2.Where;
import com.wangrg.db3.main.TableField;
import com.wangrg.java_util.ListUtil;
import com.wangrg.java_util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public class MysqlCreator extends DefaultCreator {
    @Override
    public List<String> createTableSql(String tableName, List<TableField> tableFieldList,
                                       List<String> unionUniqueList) {
        String createTableSql = "create table if not exists " + tableName + "(";
        List<String> createForeignKeySqlList = new ArrayList<>();

        for (TableField tableField : tableFieldList) {
            createTableSql += "\n\t" + tableField.getName() + " " + getType(tableField);
            if (tableField.isPrimaryKey()) {
                if (tableField.isAutoIncrement()) {
                    createTableSql += " primary key auto_increment,";
                } else {
                    createTableSql += " primary key,";
                }
                continue;
            }
            if (tableField.isUnique()) {
                createTableSql += " unique";
            }
            if (!tableField.isNullable()) {
                createTableSql += " not null";
            }
            if (!TextUtil.isEmpty(tableField.getDefaultValue())) {
                createTableSql += " default '" + tableField.getDefaultValue() + "'";
            }
            if (tableField.isForeignKey()) {
                String sql = TableUtil.foreignKeySql(
                        tableName,
                        tableField.getName(),
                        tableField.getReferenceTable(),
                        tableField.getReferenceColumn(),
                        tableField.getOnDeleteAction(),
                        tableField.getOnUpdateAction()
                );
                createForeignKeySqlList.add(sql);
            }
            createTableSql += ",";
        }

        createTableSql = createTableSql.substring(0, createTableSql.length() - 1) + "\n)";

        // 在BaseDao的executeUpdate中，会判空，所以这里不用考虑sql为空的问题
        List<String> sqlList = ListUtil.build(createTableSql, createUnionUniqueKeySql(tableName, unionUniqueList));
        sqlList.addAll(createForeignKeySqlList);
        return sqlList;
    }

    @Override
    public List<String> dropTableSql(String tableName) {
        String dropTableSql = "drop table if exists " + tableName;
        return ListUtil.build(dropTableSql);
    }

    @Override
    public String querySql(String tableName, Query query) {
        String sql;

        Where where = query.getWhere();
        if (where != null && where.size() > 0) {
            sql = "select * from " + tableName + " where " + where;
        } else {
            sql = "select * from " + tableName;
        }

        sql += " " + createOrderBy(query.getOrderBy());

        int offset = query.getOffset();
        int rowCount = query.getRowCount();
        if (rowCount > 0 && offset >= 0) {
            sql += " limit " + offset + "," + rowCount;
        }

        return sql;
    }

    @Override
    public String queryAutoIncrementCurrentValue(String tableName) {
        return null;
    }

    @Override
    public String wrapLimit(String sql, int offset, int rowCount) {
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        return sql + (" limit " + offset + "," + rowCount);
    }

    private static String getType(TableField tableField) {
        int length = tableField.getLength();
        switch (tableField.getType()) {
            case NUMBER_INT:
                return "int";
            case NUMBER_LONG:
                return "bigint";
            case NUMBER_FLOAT:
                return "float";
            case NUMBER_DOUBLE:
                return "double";
            case TEXT:
                return length == 0 ? "text" : "varchar(" + length + ")";
            case DATE:
                return "datetime";
            default:
                return null;
        }
    }

}
