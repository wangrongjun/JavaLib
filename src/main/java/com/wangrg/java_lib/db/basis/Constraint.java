package com.wangrg.java_lib.db.basis;

/**
 * by wangrongjun on 2016/12/31.
 * 如果以后要添加新的约束类型，请同时到Dao的createTable方法添加该约束类型的具体实现
 */
public enum Constraint {
    NULL,
    PRIMARY_KEY,
    UNIQUE,
    NOT_NULL,
    UNIQUE_NOT_NULL,
    UNSIGNED,
    DEFAULT,
    FOREIGN_KEY
}
