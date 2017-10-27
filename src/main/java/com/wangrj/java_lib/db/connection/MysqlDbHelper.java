package com.wangrj.java_lib.db.connection;

import com.wangrj.java_lib.db.Dao;
import com.wangrj.java_lib.db.SqlUtil;
import com.wangrj.java_lib.db.basis.DbType;
import com.wangrj.java_lib.db.basis.Where;
import com.wangrj.java_lib.db.Dao;
import com.wangrj.java_lib.db.basis.Where;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * by wangrongjun on 2017/2/14.
 */
public class MysqlDbHelper implements DbHelper {

    protected static final String driver = "com.mysql.jdbc.Driver";

    protected String url;
    protected String username;
    protected String password;
    protected Connection connection;

    public MysqlDbHelper(String username, String password, String mysqlDbName) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://localhost:3306/" + mysqlDbName;
    }

    public MysqlDbHelper(String username, String password, String mysqlDbName, String hostIP) {
        this.username = username;
        this.password = password;
        this.url = "jdbc:mysql://" + hostIP + ":3306/" + mysqlDbName;
    }

    @Override
    public DbType getDbType() {
        return DbType.MYSQL;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection = null;
    }

    @Override
    public <T> List<T> executeQuery(Class<T> entityClass, String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<T> result = Dao.getResult(entityClass, rs);
        statement.close();
        return result;
    }

    @Override
    public <T> int executeInsert(Class<T> entityClass, String sql) throws SQLException {
        return 0;
    }

    @Override
    public void execute(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        statement.execute(sql);
        statement.close();
    }

    @Override
    public int queryCount(String tableName, Where where) throws SQLException {
        String sql = SqlUtil.queryCountSql(tableName, where);
        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return Dao.getCount(rs);
    }

}
