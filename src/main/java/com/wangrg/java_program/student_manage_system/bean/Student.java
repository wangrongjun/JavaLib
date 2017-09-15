package com.wangrg.java_program.student_manage_system.bean;

import com.wangrg.db2.Column;
import com.wangrg.db2.Id;
import com.wangrg.db2.Reference;

/**
 * by wangrongjun on 2017/9/11.
 */
public class Student {

    @Id(autoIncrement = false)
    private Long studentId;
    @Column(length = 20, nullable = false, unique = true)
    private String studentName;
    @Column(length = 20, nullable = false)
    private String password;
    @Reference
    private Location location;

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
}
