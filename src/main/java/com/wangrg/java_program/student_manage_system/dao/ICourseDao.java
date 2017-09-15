package com.wangrg.java_program.student_manage_system.dao;

import com.wangrg.db3.Dao;
import com.wangrg.java_program.student_manage_system.bean.Course;

import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface ICourseDao extends Dao<Course> {

    /**
     * 查询出所有课程（包括选课人数和当前用户是否已选）
     */
    List<Course> queryAllWithCountAndSelectedState(long studentId, int offset, int rowCount);

}
