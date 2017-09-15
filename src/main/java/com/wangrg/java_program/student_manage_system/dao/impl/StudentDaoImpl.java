package com.wangrg.java_program.student_manage_system.dao.impl;

import com.wangrg.db2.Where;
import com.wangrg.java_program.student_manage_system.bean.Student;
import com.wangrg.java_program.student_manage_system.dao.IStudentDao;
import com.wangrg.java_program.student_manage_system.dao.base.StudentManageSystemDao;

import java.util.List;

/**
 * by wangrongjun on 2017/9/11.
 */
public class StudentDaoImpl extends StudentManageSystemDao<Student> implements IStudentDao {

    public List<Student> queryByArea(int areaId) {
        return query(Where.build("area", areaId + ""));
    }

    public Student queryByName(long studentId) {
        List<Student> studentList = query(Where.build("studentId", studentId + ""));
        if (studentList.size() > 0) {
            Student student = studentList.get(0);
            if (password != null && password.equals(student.getPassword())) {
                return student;
            }
        }
        return null;
    }

    @Override
    protected List<Student> executeQuery(String sql, int maxQueryForeignKeyLevel, List<String> ignoreReferenceList,
                                         List<String> requiredReferenceVariableList) {
        maxQueryForeignKeyLevel = maxQueryForeignKeyLevel == 0 ? 1 : maxQueryForeignKeyLevel;
        return super.executeQuery(sql, maxQueryForeignKeyLevel, ignoreReferenceList, requiredReferenceVariableList);
    }

}
