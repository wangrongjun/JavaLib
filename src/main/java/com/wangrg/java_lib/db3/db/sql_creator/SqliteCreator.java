package com.wangrg.java_lib.db3.db.sql_creator;

import com.wangrg.java_lib.db3.main.TableField;

import java.util.List;

/**
 * by wangrongjun on 2017/9/11.
 */
public class SqliteCreator extends MysqlCreator {

    // TODO 字段名 数据类型 references 外键表(外键列) 经验证，这样无效。猜测以下有效：
    // 先定义，再在最后添加FOREIGN KEY(字段名) REFERENCES 外键表(外键列)
    @Override
    public List<String> createTableSql(String tableName, List<TableField> tableFieldList, List<String> unionUniqueList) {
        List<String> sqlList = super.createTableSql(tableName, tableFieldList, unionUniqueList);
        String newSql = sqlList.get(0).
                replace("bigint primary key", "integer primary key").
                replace("int primary key", "integer primary key").
                replace("auto_increment", "autoincrement");
        sqlList.set(0, newSql);
        return sqlList;
    }

}
