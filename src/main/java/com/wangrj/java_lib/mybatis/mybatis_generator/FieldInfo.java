package com.wangrj.java_lib.mybatis.mybatis_generator;

/**
 * by wangrongjun on 2017/11/5.
 */
public class FieldInfo {

    private int type;// 0：基本数据类型。1：主键。2：外键对象类型
    private String name;
    private String fkClassName;// 外键对象的类名，如com.bean.Job
    private String fkIdName;// 外键对象的id名，如jobId

    public FieldInfo(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public FieldInfo(int type, String name, String fkClassName, String fkIdName) {
        this.type = type;
        this.name = name;
        this.fkClassName = fkClassName;
        this.fkIdName = fkIdName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFkClassName() {
        return fkClassName;
    }

    public void setFkClassName(String fkClassName) {
        this.fkClassName = fkClassName;
    }

    public String getFkIdName() {
        return fkIdName;
    }

    public void setFkIdName(String fkIdName) {
        this.fkIdName = fkIdName;
    }
}
