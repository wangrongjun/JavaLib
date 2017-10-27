package com.wangrj.java_lib.db2.example.bean;


import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * by wangrongjun on 2017/6/14.
 */

public class Department {

    @Id
    @GeneratedValue
    private int departmentId;
    @Column(length = 50, nullable = false)
    private String name;

    public Department() {
    }

    public Department(String name) {
        this.name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
