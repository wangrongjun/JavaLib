package com.wangrg.java_lib.db3;

import com.wangrg.java_lib.db3.db.IDataBase;

/**
 * by wangrongjun on 2017/8/24.
 */

public class Config {

    private String username;
    private String password;
    private IDataBase db;
    private boolean printSql = true;
    private boolean printResult = true;
    private boolean printLogHint = true;
    private boolean useDbcp = false;

    public String getUsername() {
        return username;
    }

    public Config setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Config setPassword(String password) {
        this.password = password;
        return this;
    }

    public IDataBase getDb() {
        return db;
    }

    public Config setDb(IDataBase db) {
        this.db = db;
        return this;
    }

    public boolean isPrintSql() {
        return printSql;
    }

    public Config setPrintSql(boolean printSql) {
        this.printSql = printSql;
        return this;
    }

    public boolean isPrintResult() {
        return printResult;
    }

    public Config setPrintResult(boolean printResult) {
        this.printResult = printResult;
        return this;
    }

    public boolean isUseDbcp() {
        return useDbcp;
    }

    public Config setUseDbcp(boolean useDbcp) {
        this.useDbcp = useDbcp;
        return this;
    }

    public boolean isPrintLogHint() {
        return printLogHint;
    }

    public Config setPrintLogHint(boolean printLogHint) {
        this.printLogHint = printLogHint;
        return this;
    }
}
