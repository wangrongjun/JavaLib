package com.wangrg.java_program.student_manage_system.service;

import com.wangrg.java_program.student_manage_system.bean.Course;
import com.wangrg.java_program.student_manage_system.bean.Student;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 */
public interface IStudentService {

    /**
     * 获取所有课程（包括选课人数和当前用户是否已选）
     */
    List<Course> getAllCourse(long studentId, int pageIndex, int pageSize);

    int getCourseAllCount();

    boolean updateStudent(Student student);

}
