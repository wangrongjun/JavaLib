package com.wangrg.java_lib.java_program.student_manage_system.service;

import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrg.java_lib.java_program.student_manage_system.constant.CourseSortType;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 */
public interface IStudentService {

    Student getStudentFromLocal();

    void setStudentToLocal(Student student);

    /**
     * 获取所有课程（包括选课人数和当前用户是否已选）
     */
    List<Course> getAllCourse(long studentId, int pageIndex, int pageSize, CourseSortType sortType);

    /**
     * 获取当前用户所有已选课程
     */
    List<Course> getSelectedCourse(long studentId, int pageIndex, int pageSize);

    int getCourseAllCount();

    int getCourseSelectedCount(long studentId);

    boolean updateStudent(Student student);

    boolean selectedCourse(long studentId, int courseId);

    boolean cancelCourse(long studentId, int courseId);

}
