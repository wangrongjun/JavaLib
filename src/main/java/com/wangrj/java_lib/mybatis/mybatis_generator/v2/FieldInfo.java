package com.wangrj.java_lib.mybatis.mybatis_generator.v2;

/**
 * by wangrongjun on 2017/11/5.
 */
public class FieldInfo {

    private int type;// 0：基本数据类型。1：主键。2：外键对象类型。
    private String name;
    private String getter;// 属性的getter方法名，如getUserId
    private String fkClassName;// 外键对象的类名，如com.bean.Job
    private String fkIdName;// 外键对象的id名，如jobId

    public FieldInfo(String name, String getter) {
        this.name = name;
        this.getter = getter;
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

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
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
