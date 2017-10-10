package com.wangrg.java_lib.db2.example.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * by wangrongjun on 2017/6/14.
 */

public class Position {

    @Id
    @GeneratedValue
    private int positionId;
    @Column(length = 20, nullable = false)
    private String name;
    @ManyToOne
    private Department department;

    public Position() {
    }

    public Position(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
