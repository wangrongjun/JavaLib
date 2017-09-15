package com.wangrg.db2.example.bean;


import com.wangrg.db2.Column;
import com.wangrg.db2.Id;
import com.wangrg.db2.Ignore;
import com.wangrg.db2.Reference;

import java.util.Date;

/**
 * by wangrongjun on 2017/6/14.
 */

public class Employee {

    @Ignore
    public static final int GENDER_MAN = 1;
    @Ignore
    public static final int GENDER_WOMAN = 0;

    @Id(autoIncrement = false)
    private Long employeeId;
    @Column(length = 20, nullable = false)
    private String name;
    private Long gender;
    @Reference
    private Position position;
    private Double salary;
    private Date startTime;//入职时间

    private String departmentName;

    public Employee() {
    }

    public Employee(Long employeeId, String name, Long gender, Position position, Double salary,
                    Date startTime) {
        this.employeeId = employeeId;
        this.name = name;
        this.gender = gender;
        this.position = position;
        this.salary = salary;
        this.startTime = startTime;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
