package com.wangrg.db3.example.bean;

import com.wangrg.db2.Column;
import com.wangrg.db2.Id;
import com.wangrg.db2.Reference;

/**
 * by wangrongjun on 2017/8/23.
 */

public class Pos {

    @Id
    private long posId;
    @Column(nullable = false)
    private String posName;
    @Reference
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
