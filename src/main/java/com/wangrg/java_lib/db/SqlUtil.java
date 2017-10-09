package com.wangrg.java_lib.db;

import com.wangrg.java_lib.db.basis.Action;
import com.wangrg.java_lib.db.basis.DbType;
import com.wangrg.java_lib.db.basis.FieldType;
import com.wangrg.java_lib.db.basis.TableField;
import com.wangrg.java_lib.db.basis.TableValue;
import com.wangrg.java_lib.db.basis.ValueType;
import com.wangrg.java_lib.db.basis.Where;
import com.wangrg.java_lib.java_util.TextUtil;

import java.util.List;

/**
 * 生成各种sql语句的工具类。使用之前需要先配置SqlUtil.dbType，默认为SqlUtil.TYPE_MYSQL
 */
public class SqlUtil {

    public static DbType dbType = DbType.MYSQL;

    /**
     * 创建表的生存方法
     */
    public static String createTableSql(String tableName, List<TableField> tableFields) {
        StringBuilder sql = new StringBuilder();
        sql.append("create table if not exists ").append(tableName).append("(\n");
        for (int i = 0; i < tableFields.size(); i++) {
            TableField field = tableFields.get(i);
            String s = field.name + " " +
                    toFieldTypeString(dbType, field.type) +
                    (field.unsigned ? " unsigned" : "") +
                    (field.primaryKey ? " primary key auto_increment" : "") +
                    ((!field.primaryKey && field.notNull) ? " not null" : "") +
                    ((!field.primaryKey && field.unique) ? " unique key" : "") +
                    (field.defaultValue != null ? " default " + field.defaultValue.value : "") +
                    (i < tableFields.size() - 1 ? ",\n" : "\n) ");
            sql.append(s);
        }

        if (dbType == DbType.MYSQL) {
            sql.append("default charset=utf8;");
            return sql.toString();
        } else if (dbType == DbType.SQLITE) {
            sql.append(";");
            return sql.toString().replace("auto_increment", "autoincrement");
        } else {
            return "";
        }

    }

    public static String dropTableSql(String tableName) {
        return "drop table if exists " + tableName + ";";
    }

    public static String getLatestAutoIncrementNumberSql(String tableName, String primaryKeyName) {
        //第一种方法：select last_insert_rowid()，暂时不知执行sql后怎么在Cursor中获取结果。
        //第二种方法：select max(ID) from 表名
        return "select max(" + primaryKeyName + ") from " + tableName + ";";
    }

    public static String foreignKeySql(String mainTableName,
                                       String mainFieldName,
                                       String referenceTableName,
                                       String referenceFieldName,
                                       Action onDeleteAction,
                                       Action onUpdateAction) {

        String sql = "alter table " + mainTableName + " add foreign key (" + mainFieldName + ") " +
                "references " + referenceTableName + "(" + referenceFieldName + ") ";

        String onDeleteActionSql = "";
        String onUpdateActionSql = "";

        switch (onDeleteAction) {
            case NO_ACTION:
                onDeleteActionSql = "on delete no action";
                break;
            case SET_NULL:
                onDeleteActionSql = "on delete set null";
                break;
            case CASCADE:
                onDeleteActionSql = "on delete cascade";
                break;
        }

        switch (onUpdateAction) {
            case NO_ACTION:
                onUpdateActionSql = "on update no action";
                break;
            case SET_NULL:
                onUpdateActionSql = "on update set null";
                break;
            case CASCADE:
                onUpdateActionSql = "on update cascade";
                break;
        }

        if (!TextUtil.isEmpty(onDeleteActionSql)) {
            sql += onDeleteActionSql + " ";
        }
        if (!TextUtil.isEmpty(onUpdateActionSql)) {
            sql += onUpdateActionSql + " ";
        }

        sql += ";";
        return sql;
    }

    /**
     * 生成插入语句
     */
    public static String insertSql(String tableName, List<TableValue> tableValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert ").append(tableName);
        String nameList = "(";
        String valueList = "values(";
        for (int i = 0; i < tableValues.size(); i++) {
            TableValue tv = tableValues.get(i);

            nameList += tv.name + (i < tableValues.size() - 1 ? "," : ")");

            switch (tv.type) {
                case INT:
                    valueList += Integer.parseInt(tv.value + "");
                    break;
                case DOUBLE:
                    valueList += Double.parseDouble(tv.value + "");
                    break;
                case TEXT:
                    //一定要进行特殊字符'和\的转义，否则sql语法出错！！！
                    valueList += "'" + toCorrectValue(tv.value) + "'";
                    break;
            }

            valueList += i < tableValues.size() - 1 ? "," : ")";
        }

        sql.append(nameList).append(" ").append(valueList).append(";");

        if (dbType == DbType.MYSQL) {
            return sql.toString();
        } else if (dbType == DbType.SQLITE) {
            return sql.toString().replace("insert", "insert into");
        } else {
            return "";
        }

    }

    /**
     * 生成删除语句 - 具体实现
     */
    public static String deleteSql(String tableName, Where where) {
        String sql = "delete from " + tableName;
        if (where != null && where.size() > 0) {
            sql += " " + toWhereString(where);
        }
        sql += ";";
        return sql;
    }

    /**
     * 生成更新语句 - 具体实现
     */
    public static String updateSql(String tableName, List<TableValue> setValues, Where where) {
        String sql = "update " + tableName + " set ";
        sql += getExpressionList(setValues, ",");
        if (where != null && where.size() > 0) {
            sql += " " + toWhereString(where);
        }
        sql += ";";
        return sql;
    }

    /**
     * 生成查询语句 - 具体实现
     *
     * @param tableName   数据表名字
     * @param where       查询条件，若为空或长度为0，则查询所有记录
     * @param orderByList 排序字段的列表（字段名字前加负号"-"，则为倒序，否则正序）
     * @param begin       分页的起始（begin或length为空则不分页）
     * @param length      分页的长度
     */
    public static String querySql(String tableName, Where where, List<String> orderByList,
                                  Integer begin, Integer length) {
        String sql = "select * from " + tableName;

        if (where != null && where.size() > 0) {
            sql += " " + toWhereString(where);
        }

        if (orderByList != null && orderByList.size() != 0) {
            sql += " order by ";
            for (int i = 0; i < orderByList.size(); i++) {
                String s = orderByList.get(i);
                if (s.startsWith("-")) {
                    sql += s.replace("-", "") + " desc";
                } else {
                    sql += s;
                }
                if (i < orderByList.size() - 1) {//若不是最后一个
                    sql += ",";
                }
            }
        }

        if (begin != null && length != null) {
            sql += " limit " + begin + "," + length;
        }

        sql += ";";
        return sql;
    }

    /**
     * 生成查询语句 - 接口实现1
     */
    public static String querySql(String tableName, Where where, List<String> orderByList) {
        return querySql(tableName, where, orderByList, null, null);
    }

    /**
     * 生成查询语句 - 接口实现2
     */
    public static String querySql(String tableName, Where where) {
        return querySql(tableName, where, null, null, null);
    }

    /**
     * 生成模糊查询语句，匹配符：%whereValue%
     */
    public static String queryFuzzySql(String tableName, String whereName, String whereValue,
                                       List<String> orderByList) {
        whereValue = "%" + whereValue + "%";
        Where where = new Where().add(whereName, whereValue, ValueType.TEXT, Where.QueryMode.LIKE);
        return querySql(tableName, where, orderByList, null, null);
    }

    public static String queryCountSql(String tableName, Where where) {
        String sql = "select count(*) from " + tableName;
        if (where != null && where.size() > 0) {
            sql += " " + toWhereString(where);
        }
        sql += ";";
        return sql;
    }

    /**
     * @return where username='wang' and password='123' or gender=1 or nickname like '%abc%'
     */
    private static String toWhereString(Where where) {
        if (where == null || where.size() == 0) {
            return "";
        }

        String sql = "where ";
        for (int i = 0; i < where.size(); i++) {
            Where.Equation equation = where.get(i);
            // 1.字段名字
            sql += equation.name;
            // 2.查询模式
            switch (equation.queryMode) {
                case EQUAL:
                    sql += "=";
                    break;
                case NOT_EQUAL:
                    sql += "!=";
                    break;
                case LIKE:
                    sql += " like ";
                    break;
            }
            // 3.赋值
            switch (equation.valueType) {
                case INT:
                case DOUBLE:
                    sql += equation.value;
                    break;
                case TEXT:
                    sql += "'" + equation.value + "'";
                    break;
            }
            // 4.如果不是最后一个查询条件，添加查询逻辑
            if (i < where.size() - 1) {
                switch (equation.queryLogic) {
                    case AND:
                        sql += " and ";
                        break;
                    case OR:
                        sql += " or ";
                        break;
                }
            }
        }
        return sql;
    }

    private static String toFieldTypeString(DbType dbType, FieldType type) {
        String strType = "";
        switch (type) {
            case TINYINT:
                strType = "tinyint";
                break;
            case INT:
                if (dbType == DbType.MYSQL) {
                    strType = "int";
                } else if (dbType == DbType.SQLITE) {
                    strType = "integer";
                }
                break;
            case DOUBLE:
                if (dbType == DbType.MYSQL) {
                    strType = "double";
                } else if (dbType == DbType.SQLITE) {
                    strType = "real";
                }
                break;
            case VARCHAR_10:
                strType = "varchar(10)";
                break;
            case VARCHAR_20:
                strType = "varchar(20)";
                break;
            case VARCHAR_50:
                strType = "varchar(50)";
                break;
            case VARCHAR_100:
                strType = "varchar(100)";
                break;
            case VARCHAR_500:
                strType = "varchar(500)";
                break;
            case TEXT:
                strType = "text";
                break;
        }
        return strType;
    }

    /**
     * @param separator 表达式之间的分隔符。常见的有" and "(where条件语句) 和 ","（update赋值语句）
     * @return 如 "name='wang',date='2015-03-11',sex=1" 或 "group_id=4 and date='2015-06-12'"
     */
    private static String getExpressionList(List<TableValue> tableValues, String separator) {
        String expressionList = "";
        for (int i = 0; i < tableValues.size(); i++) {
            TableValue tv = tableValues.get(i);
            String value = "";
            switch (tv.type) {
                case INT:
                    value += Integer.parseInt(tv.value + "");
                    break;
                case DOUBLE:
                    value += Double.parseDouble(tv.value + "");
                    break;
                case TEXT:
                    value += "'" + tv.value + "'";
                    break;
            }
            expressionList += tv.name + "=" + value + (i < tableValues.size() - 1 ? separator : "");
        }

        return expressionList;
    }

    /**
     * 对插入、查询等需要用到的value的特殊字符进行转义
     * <p>
     * 1.若出现单引号，进行转义
     * <p>
     * 2.末尾若出现转义字符，进行二次转义
     */
    private static String toCorrectValue(String value) {
        value = value.replaceAll("'", "\\'");
        if (value.endsWith("\\")) {
            value = value + "\\";
        }
        return value;
    }

}
