package com.wangrg.java_program.student_manage_system.bean;

import com.wangrg.db2.Column;
import com.wangrg.db2.Id;

/**
 * by wangrongjun on 2017/9/11.
 */
public class Manager {

    @Id
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
