package com.wangrg.db.exception;

import java.sql.SQLException;

/**
 * by wangrongjun on 2017/1/23.
 */
public class PrimaryKeyNotFoundException extends SQLException {

    public PrimaryKeyNotFoundException() {
        super("primary key not exists");
    }
}
