package com.wangrg.java_lib.db3.db;

import com.wangrg.java_lib.db3.db.sql_creator.ISqlCreator;
import com.wangrg.java_lib.db3.db.sql_creator.SqliteCreator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * by wangrongjun on 2017/9/11.
 */
public class SqliteDatabase extends MysqlDatabase {

    private String dbFilePath;

    public SqliteDatabase(String dbFilePath) {
        super(dbFilePath);// 无任何意义，只是为了使构造方法不出错而已
        this.dbFilePath = dbFilePath;
    }

    @Override
    public String getDriverName() {
        return "org.sqlite.JDBC";
    }

    @Override
    public String getUrl() {
        return "jdbc:sqlite:" + dbFilePath;
    }

    @Override
    public ISqlCreator getSqlCreator() {
        return new SqliteCreator();
    }

    @Override
    public long insert(Connection conn, String tableName, boolean autoIncrement, String sql) throws SQLException {
        Statement stat = conn.createStatement();
        stat.executeUpdate(sql);
        int id = 0;
        if (autoIncrement) {
            ResultSet rs = stat.executeQuery("select seq from sqlite_sequence where name='" + tableName + "'");
            if (rs.next()) {
                id = rs.getInt(1);
            }
            rs.close();
        }
        stat.close();
        return id;
    }
}
