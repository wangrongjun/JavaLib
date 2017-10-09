package com.wangrg.java_lib.java_program.student_manage_system.service.impl;

import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Manager;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrg.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrg.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrg.java_lib.java_program.student_manage_system.dao.ICourseDao;
import com.wangrg.java_lib.java_program.student_manage_system.dao.IStudentDao;
import com.wangrg.java_lib.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_lib.java_program.student_manage_system.service.IManagerService;

import java.util.List;

/**
 * by wangrongjun on 2017/9/14.
 */
public class ManagerServiceImpl implements IManagerService {

    private ICourseDao courseDao = Spring.getBean(ICourseDao.class);
    private IStudentDao studentDao = Spring.getBean(IStudentDao.class);

    private static Manager manager;

    @Override
    public Manager getManagerFromLocal() {
        return manager;
    }

    @Override
    public void setManagerToLocal(Manager manager) {
        ManagerServiceImpl.manager = manager;
    }

    @Override
    public List<Course> getAllCourse(int pageIndex, int pageSize, CourseSortType sortType) {
        return courseDao.queryAllWithCount(pageIndex * pageSize, pageSize, sortType);
    }

    @Override
    public int getCourseAllCount() {
        return courseDao.queryCount(null);
    }

    @Override
    public boolean addCourse(Course course) {
        return courseDao.insert(course);
    }

    @Override
    public boolean deleteCourse(int courseId) {
        return courseDao.deleteById(courseId);
    }

    @Override
    public List<Student> getAllStudent(int pageIndex, int pageSize, StudentSortType sortType) {
        return studentDao.queryAllWithSelectCount(pageIndex * pageSize, pageSize, sortType);
    }

    @Override
    public int getStudentAllCount() {
        return studentDao.queryCount(null);
    }

}
