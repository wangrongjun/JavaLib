package com.wangrj.java_lib.mybatis.mybatis_generator.v2;

/**
 * by wangrongjun on 2017/11/5.
 */
public class FieldInfo {

    private int type;// 0：基本数据类型。1：主键。2：外键对象类型。
    private String propertyName;// 属性名
    private String getter;// 属性的getter方法名，如getUserId
    private String columnName;// 字段名
    private String fkClassName;// 外键对象的类名，如com.bean.UserInfo
    private String fkTableName;// 外键对象的表名，如 UserInfo 或 user_info
    private String fkIdName;// 外键对象的id名，如jobId

    public FieldInfo(String propertyName, String columnName) {
        this.propertyName = propertyName;
        this.columnName = columnName;
    }

    public FieldInfo(String propertyName, String getter, String columnName) {
        this.propertyName = propertyName;
        this.getter = getter;
        this.columnName = columnName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public void setFkTableName(String fkTableName) {
        this.fkTableName = fkTableName;
    }

    public String getFkIdName() {
        return fkIdName;
    }

    public void setFkIdName(String fkIdName) {
        this.fkIdName = fkIdName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFkClassName() {
        return fkClassName;
    }

    public void setFkClassName(String fkClassName) {
        this.fkClassName = fkClassName;
    }
}
