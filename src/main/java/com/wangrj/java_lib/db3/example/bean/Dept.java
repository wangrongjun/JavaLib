package com.wangrj.java_lib.db3.example.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * by wangrongjun on 2017/8/23.
 */

public class Dept {

    @Id
    @GeneratedValue
    @Column(length = 7)
    private int deptno;
    @Column(length = 50, nullable = false)
    private String dname;

    public Dept() {
    }

    public Dept(String dname) {
        this.dname = dname;
    }

    public int getDeptno() {
        return deptno;
    }

    public void setDeptno(int deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

}
