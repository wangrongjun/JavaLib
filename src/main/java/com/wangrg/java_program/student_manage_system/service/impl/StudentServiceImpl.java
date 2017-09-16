package com.wangrg.java_program.student_manage_system.service.impl;

import com.wangrg.java_program.student_manage_system.bean.Course;
import com.wangrg.java_program.student_manage_system.bean.SC;
import com.wangrg.java_program.student_manage_system.bean.Student;
import com.wangrg.java_program.student_manage_system.dao.ICourseDao;
import com.wangrg.java_program.student_manage_system.dao.ISCDao;
import com.wangrg.java_program.student_manage_system.dao.IStudentDao;
import com.wangrg.java_program.student_manage_system.framework.Spring;
import com.wangrg.java_program.student_manage_system.service.IStudentService;

import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public class StudentServiceImpl implements IStudentService {

    private ICourseDao courseDao = Spring.getBean(ICourseDao.class);
    private IStudentDao studentDao = Spring.getBean(IStudentDao.class);
    private ISCDao scDao = Spring.getBean(ISCDao.class);

    private static Student student;

    @Override
    public Student getStudentFromLocal() {
        return student;
    }

    @Override
    public void setStudentToLocal(Student student) {
        StudentServiceImpl.student = student;
    }

    @Override
    public List<Course> getAllCourse(long studentId, int pageIndex, int pageSize) {
        return courseDao.queryAllWithCountAndSelectedState(studentId, pageIndex * pageSize, pageSize);
    }

    @Override
    public int getCourseAllCount() {
        return courseDao.queryCount(null);
    }

    @Override
    public boolean updateStudent(Student student) {
        return studentDao.update(student);
    }

    @Override
    public SC selectedCourse(long studentId, int courseId) {
        return scDao.insert(studentId, courseId);
    }

}
