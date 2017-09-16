package com.wangrg.java_program.student_manage_system.bean;

import com.wangrg.db2.Column;
import com.wangrg.db2.Id;
import com.wangrg.db2.Ignore;

/**
 * by wangrongjun on 2017/9/11.
 */
public class Course {

    @Id
    private Integer courseId;
    @Column(length = 20, nullable = false, unique = true)
    private String courseName;

    @Ignore
    private boolean isSelected;
    @Ignore
    private int selectedCount;// 选课人数

    public Course() {
    }

    public Course(Integer courseId) {
        this.courseId = courseId;
    }

    public Course(String courseName) {
        this.courseName = courseName;
    }

    public Course(Integer courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }
}
