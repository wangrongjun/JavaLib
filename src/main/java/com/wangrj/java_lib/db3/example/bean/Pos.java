package com.wangrj.java_lib.db3.example.bean;

import javax.persistence.*;
import java.util.List;

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
    @JoinColumn(name = "deptId")
    private Dept dept;
    @OneToMany
    @JoinColumn(name = "posId")
    private List<Emp> empList;

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

    public List<Emp> getEmpList() {
        return empList;
    }

    public void setEmpList(List<Emp> empList) {
        this.empList = empList;
    }
}
