package com.wangrg.java_lib.db3.db;

import com.wangrg.java_lib.db3.db.sql_creator.ISqlCreator;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public interface IDataBase {

    String getDriverName();

    String getUrl();

    ISqlCreator getSqlCreator();

    /**
     * @return 如果autoIncrement为false，返回0。否则返回插入后自增的id值
     */
    long insert(Connection conn, String tableName, boolean autoIncrement, String sql)
            throws SQLException;

    /**
     * 根据field（非主键）和entity获取值
     * 作用：1.根据数据类型转换为合适的sql语句。比如在Oracle中，Date类型转换为"to_date(...)"
     * 2.防止输入的值中含有非法字符如单引号，斜杠，双横杠等。防止sql注入攻击。
     */
    <T> String convertInsertOrUpdateValue(Field field, T entity) throws IllegalAccessException;

    /**
     * 把value设置到entity中的field（非主键）属性
     */
    void setRsValueToEntity(Field field, Object entity, Object value) throws IllegalAccessException;


// --------------------------以下是根据entity构建sql语句的方法--------------------------------------

    List<String> createTableSql(Class entityClass);

    List<String> dropTableSql(Class entityClass);

    String insertSql(Object entity);

    String deleteSql(Object entity);

    String updateSql(Object entity);

}
