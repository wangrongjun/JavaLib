package com.wangrg.java_program.student_manage_system.dao;

import com.wangrg.db3.Dao;
import com.wangrg.java_program.student_manage_system.bean.SC;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface ISCDao extends Dao<SC> {

    SC insert(long studentId, int courseId);

}
