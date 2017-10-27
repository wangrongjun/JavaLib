package com.wangrj.java_lib.java_program.student_manage_system.dao.impl;

import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.bean.SC;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.dao.ISCDao;
import com.wangrj.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrj.java_lib.java_program.student_manage_system.dao.ISCDao;
import com.wangrj.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;

/**
 * by wangrongjun on 2017/9/11.
 */
public class SCDaoImpl extends StudentManageSystemDao<SC> implements ISCDao {

    @Override
    public boolean insert(long studentId, int courseId) {
        SC sc = new SC(new Student(studentId), new Course(courseId));
        return insert(sc);
    }

    @Override
    public boolean delete(long studentId, int courseId) {
        Where where = new Where().
                equal("student", String.valueOf(studentId)).
                and().
                equal("course", String.valueOf(courseId));
        return delete(where);
    }

}
