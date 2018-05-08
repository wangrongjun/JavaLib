package com.wangrj.java_lib.db3.example.bean;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * by wangrongjun on 2017/8/23.
 */

public class Dept {

    @Id
    @GeneratedValue
    @Column(length = 7)
    private int deptId;
    @Column(length = 50, nullable = false)
    private String deptName;
    @OneToMany
    @JoinColumn(name = "deptId")
    private List<Pos> posList;

    public static Dept getTestData() {
        Dept dept = new Dept(1, "dept");

        Pos pos_a = new Pos(1L, "pos_a", dept);
        Pos pos_b = new Pos(2L, "pos_b", dept);

        Emp emp_1 = new Emp(1L, "emp_1", pos_a);
        Emp emp_2 = new Emp(2L, "emp_2", pos_a);
        Emp emp_3 = new Emp(3L, "emp_3", pos_b);
        Emp emp_4 = new Emp(4L, "emp_4", pos_b);

        pos_a.setEmpList(Arrays.asList(emp_1, emp_2));
        pos_b.setEmpList(Arrays.asList(emp_3, emp_4));

        dept.setPosList(Arrays.asList(pos_a, pos_b));

        return dept;
    }

    public Dept() {
    }

    public Dept(String deptName) {
        this.deptName = deptName;
    }

    public Dept(int deptId, String deptName) {
        this.deptId = deptId;
        this.deptName = deptName;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public List<Pos> getPosList() {
        return posList;
    }

    public void setPosList(List<Pos> posList) {
        this.posList = posList;
    }
}
