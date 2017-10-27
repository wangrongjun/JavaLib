package com.wangrj.java_lib.db2.example.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * by wangrongjun on 2017/6/15.
 */

public class EmployeeLogin {

    @Id
    @GeneratedValue
    private int employeeLoginId;
    @ManyToOne
    private Employee employee;
    @Column(length = 20, nullable = false)
    private String password;

    public EmployeeLogin() {
    }

    public EmployeeLogin(Employee employee, String password) {
        this.employee = employee;
        this.password = password;
    }

    public int getEmployeeLoginId() {
        return employeeLoginId;
    }

    public void setEmployeeLoginId(int employeeLoginId) {
        this.employeeLoginId = employeeLoginId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
