package com.wangrg.java_lib.java_program.student_manage_system.dao;

import com.wangrg.java_lib.db3.Dao;
import com.wangrg.java_lib.java_program.student_manage_system.bean.SC;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface ISCDao extends Dao<SC> {

    boolean insert(long studentId, int courseId);

    boolean delete(long studentId, int courseId);

}
