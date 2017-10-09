package com.wangrg.java_lib.db3.example.bean;

import com.wangrg.java_lib.db2.Column;
import com.wangrg.java_lib.db2.Id;
import com.wangrg.java_lib.db2.Ignore;
import com.wangrg.java_lib.db2.Reference;

import java.util.Date;

/**
 * by wangrongjun on 2017/8/23.
 */

public class Emp {

    @Ignore
    public static final int GENDER_MAN = 1;
    @Ignore
    public static final int GENDER_WOMAN = 0;

    @Id(autoIncrement = false)
    private Long empno;
    @Column(length = 20, nullable = false)
    private String ename;
    private Long gender;
    @Reference
    private Pos pos;
    private Double salary;
    private Date hireDate;//入职时间

    public Emp() {
    }

    public Emp(Long empno, String ename, Long gender, Pos pos, Double salary, Date hireDate) {
        this.empno = empno;
        this.ename = ename;
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

    public Long getEmpno() {
        return empno;
    }

    public void setEmpno(Long empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
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
