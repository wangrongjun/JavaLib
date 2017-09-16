package com.wangrg.java_program.student_manage_system.dao.impl;

import com.wangrg.java_program.student_manage_system.bean.Course;
import com.wangrg.java_program.student_manage_system.bean.SC;
import com.wangrg.java_program.student_manage_system.bean.Student;
import com.wangrg.java_program.student_manage_system.dao.ISCDao;
import com.wangrg.java_program.student_manage_system.dao.base.StudentManageSystemDao;

/**
 * by wangrongjun on 2017/9/11.
 */
public class SCDaoImpl extends StudentManageSystemDao<SC> implements ISCDao {

    @Override
    public SC insert(long studentId, int courseId) {
        SC sc = new SC(new Student(studentId), new Course(courseId));
        if (insert(sc)) {
            return sc;
        }
        return null;
    }

}
