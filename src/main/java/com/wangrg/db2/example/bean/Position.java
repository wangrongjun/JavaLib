package com.wangrg.db2.example.bean;

import com.wangrg.db2.Column;
import com.wangrg.db2.Id;
import com.wangrg.db2.Reference;

/**
 * by wangrongjun on 2017/6/14.
 */

public class Position {

    @Id
    private int positionId;
    @Column(length = 20, nullable = false)
    private String name;
    @Reference
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
