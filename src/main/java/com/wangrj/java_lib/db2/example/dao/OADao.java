package com.wangrj.java_lib.db2.example.dao;

import com.wangrj.java_lib.db2.BaseDao;
import com.wangrj.java_lib.java_util.ConfigUtil;

/**
 * by wangrongjun on 2017/6/14.
 */

public abstract class OADao<T> extends BaseDao<T> {

    static {
        System.out.println("\n--------- load config start ---------");
        ConfigUtil.read(OADatabaseConfig.class, "OADatabaseConfig.txt", true);
        System.out.println("username:" + OADatabaseConfig.username);
        System.out.println("password:" + OADatabaseConfig.password);
        System.out.println("dbName:" + OADatabaseConfig.dbName);
        System.out.println("printSql:" + OADatabaseConfig.printSql);
        System.out.println("printResult:" + OADatabaseConfig.printResult);
        System.out.println("--------- load config finish --------\n");
    }

    public OADao() {
        super(OADatabaseConfig.username, OADatabaseConfig.password, OADatabaseConfig.dbName,
                OADatabaseConfig.printSql, OADatabaseConfig.printResult);
    }

    @Override
    public boolean executeUpdate(String sql) {
        return super.executeUpdate(sql);
    }

    private static class OADatabaseConfig {
        static String username = "root";
        static String password = "21436587";
        static String dbName = "oa";
        static boolean printSql = true;
        static boolean printResult = true;
    }

}
