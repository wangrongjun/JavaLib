package com.wangrj.java_lib.java_program.student_manage_system.service;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrj.java_lib.java_program.student_manage_system.constant.StudentSortType;

import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface IManagerService {

    Manager getManagerFromLocal();

    void setManagerToLocal(Manager manager);

    /**
     * 获取所有课程（包括选课人数和当前用户是否已选）
     */
    List<Course> getAllCourse(int pageIndex, int pageSize, CourseSortType sortType);

    int getCourseAllCount();

    boolean addCourse(Course course);

    boolean deleteCourse(int courseId);

    /**
     * 获取所有学生（包括学生选课数量）
     */
    List<Student> getAllStudent(int pageIndex, int pageSize, StudentSortType sortType);

    int getStudentAllCount();

}
