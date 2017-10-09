package com.wangrg.java_lib.java_program.student_manage_system.dao;

import com.wangrg.java_lib.db3.Dao;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrg.java_lib.java_program.student_manage_system.constant.StudentSortType;

import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface IStudentDao extends Dao<Student> {

    List<Student> queryAllWithSelectCount(int offset, int rowCount, StudentSortType sortType);

}
