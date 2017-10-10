package com.wangrg.java_lib.java_program.student_manage_system.bean;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * by wangrongjun on 2017/9/11.
 */
public class Student {

    @Id
    @GeneratedValue
    private Long studentId;
    @Column(length = 20, nullable = false, unique = true)
    private String studentName;
    @Column(length = 20, nullable = false)
    private String password;
    @ManyToOne
    private Location location;

    @ManyToOne
    private int selecteCourseCount;

    public Student() {
    }

    public Student(Long studentId) {
        this.studentId = studentId;
    }

    public Student(Long studentId, String studentName, String password, Location location) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.password = password;
        this.location = location;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getSelecteCourseCount() {
        return selecteCourseCount;
    }

    public void setSelecteCourseCount(int selecteCourseCount) {
        this.selecteCourseCount = selecteCourseCount;
    }
}
