package com.wangrj.java_lib.db3.example.bean;

import javax.persistence.*;
import java.util.Date;

/**
 * by wangrongjun on 2017/8/23.
 */

public class Emp {

    @Transient
    public static final int GENDER_MAN = 1;
    @Transient
    public static final int GENDER_WOMAN = 0;

    @Id
    @GeneratedValue
    private Long empId;
    @Column(length = 20, nullable = false)
    private String empName;
    private Long gender;
    @ManyToOne
    @JoinColumn(name = "posId")
    private Pos pos;
    private Double salary;
    private Date hireDate;//入职时间

    public Emp() {
    }

    public Emp(Long empId, String empName, Pos pos) {
        this.empId = empId;
        this.empName = empName;
        this.pos = pos;
    }

    public Emp(Long empId, String empName, Long gender, Pos pos, Double salary, Date hireDate) {
        this.empId = empId;
        this.empName = empName;
        this.gender = gender;
        this.pos = pos;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public static int getGenderMan() {
        return GENDER_MAN;
    }

    public static int getGenderWoman() {
        return GENDER_WOMAN;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Long getGender() {
        return gender;
    }

    public void setGender(Long gender) {
        this.gender = gender;
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
}
