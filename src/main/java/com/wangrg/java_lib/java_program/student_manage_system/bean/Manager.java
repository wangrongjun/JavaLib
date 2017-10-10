package com.wangrg.java_lib.java_program.student_manage_system.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * by wangrongjun on 2017/9/11.
 */
public class Manager {

    @Id
    @GeneratedValue
    private Integer managerId;
    @Column(length = 20, nullable = false, unique = true)
    private String managerName;
    @Column(length = 20, nullable = false)
    private String password;

    public Manager() {
    }

    public Manager(String managerName, String password) {
        this.managerName = managerName;
        this.password = password;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
