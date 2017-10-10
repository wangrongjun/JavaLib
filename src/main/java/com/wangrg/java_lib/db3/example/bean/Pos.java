package com.wangrg.java_lib.db3.example.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * by wangrongjun on 2017/8/23.
 */

public class Pos {

    @Id
    @GeneratedValue
    private long posId;
    @Column(nullable = false)
    private String posName;
    @ManyToOne
    private Dept dept;

    public Pos() {
    }

    public Pos(String posName, Dept dept) {
        this.posName = posName;
        this.dept = dept;
    }

    public Pos(long posId, String posName, Dept dept) {
        this.posId = posId;
        this.posName = posName;
        this.dept = dept;
    }

    public long getPosId() {
        return posId;
    }

    public void setPosId(long posId) {
        this.posId = posId;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }
}
