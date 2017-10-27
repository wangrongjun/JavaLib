package com.wangrj.java_lib.db.connection;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * http://blog.csdn.net/kalision/article/details/7645959
 * DBCP 数据连接池的配置和使用
 * <p/>
 * http://blog.csdn.net/shenzhennba/article/details/20855149
 * 连接池 DBCP 参数意义和设置
 */
public class Dbcp {

    private static BasicDataSource basicDataSource;

    public static Connection getConnection(String url, String driverClassName, String username,
                                           String password) throws SQLException {
        if (basicDataSource == null) {
            basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(url);
            basicDataSource.setDriverClassName(driverClassName);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
        }
        return basicDataSource.getConnection();
    }

    public static void close() throws SQLException {
        if (basicDataSource != null) {
            basicDataSource.close();
            basicDataSource = null;
        }
    }

}
