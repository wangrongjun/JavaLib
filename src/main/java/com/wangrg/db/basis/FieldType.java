package com.wangrg.db.basis;

/**
 * by wangrongjun on 2016/12/31.
 * 数据表字段的类型，一般在定义Bean，生成创建数据表的sql时用到
 */
public enum FieldType {
    TINYINT,
    INT,
    DOUBLE,

    TEXT,
    VARCHAR_10,
    VARCHAR_20,
    VARCHAR_50,
    VARCHAR_100,
    VARCHAR_500,

    EXTRA
}
