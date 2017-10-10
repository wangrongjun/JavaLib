package com.wangrg.java_lib.java_program.student_manage_system.bean;

import com.wangrg.java_lib.db3.main.UnionUniqueKey;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * by wangrongjun on 2017/9/11.
 */
public class SC {

    @Id
    private Integer scId;
    @UnionUniqueKey
    @ManyToOne
    private Student student;
    @UnionUniqueKey
    @ManyToOne
    private Course course;

    public SC() {
    }

    public SC(Student student, Course course) {
        this.student = student;
        this.course = course;
    }

    public SC(Integer scId, Student student, Course course) {
        this.scId = scId;
        this.student = student;
        this.course = course;
    }

    public Integer getScId() {
        return scId;
    }

    public void setScId(Integer scId) {
        this.scId = scId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
