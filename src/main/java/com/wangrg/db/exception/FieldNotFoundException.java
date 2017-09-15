package com.wangrg.db.exception;

import java.sql.SQLException;

/**
 * by wangrongjun on 2017/1/23.
 */
public class FieldNotFoundException extends SQLException {

    public FieldNotFoundException(String field) {
        super("field not found: " + field);
    }

}
