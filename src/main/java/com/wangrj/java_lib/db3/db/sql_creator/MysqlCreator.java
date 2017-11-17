package com.wangrj.java_lib.db3.db.sql_creator;

import com.wangrj.java_lib.db2.Query;
import com.wangrj.java_lib.db2.TableUtil;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.db3.DefaultTypeLength;
import com.wangrj.java_lib.db3.main.TableField;
import com.wangrj.java_lib.java_util.ListUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public class MysqlCreator extends DefaultCreator {
    @Override
    public List<String> createTableSql(String tableName, String tableComment,
                                       List<TableField> tableFieldList,
                                       List<String> unionUniqueList) {
        String createTableSql = "create table if not exists " + tableName + "(";
        // 对于MySQL而言，在表中直接定义外键（refercences），虽然不报错，但无效。所以需要额外定义。
        List<String> createForeignKeySqlList = new ArrayList<>();

        for (int i = 0; i < tableFieldList.size(); i++) {
            TableField tableField = tableFieldList.get(i);
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
            if (!TextUtil.isEmpty(tableField.getComment())) {
                createTableSql += " comment '" + tableField.getComment() + "'";
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
            createTableSql += i < tableFieldList.size() - 1 ? "," : "";
        }
        String unionUniqueKeySql = createUnionUniqueKeySql(unionUniqueList);
        if (unionUniqueKeySql != null) {
            createTableSql += ",\n\t" + unionUniqueKeySql;
        }
        createTableSql += "\n)";

        if (tableComment != null) {
            createTableSql += " comment='" + tableComment + "'";
        }

        List<String> sqlList = ListUtil.build(createTableSql);
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
            case BOOLEAN:
                return "boolean";
            case TEXT:
                if (length == 0) {
                    return "varchar(" + DefaultTypeLength.MYSQL_STRING_LENGTH + ")";
                } else if (length == Integer.MAX_VALUE) {
                    return "text";
                }
            case DATE:
                return "datetime";
            default:
                return null;
        }
    }

}
