package com.wangrj.java_lib.db.connection;

import com.wangrj.java_lib.db.basis.DbType;
import com.wangrj.java_lib.db.basis.Where;
import com.wangrj.java_lib.db.basis.Where;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2017/2/16.
 */
public interface DbHelper {
    DbType getDbType();

    Connection getConnection() throws SQLException;

    <T> List<T> executeQuery(Class<T> entityClass, String sql) throws SQLException;

    /**
     * @return id
     */
    <T> int executeInsert(Class<T> entityClass, String sql) throws SQLException;

    void execute(String sql) throws SQLException;

    int queryCount(String tableName, Where where) throws SQLException;

    void close();
}
