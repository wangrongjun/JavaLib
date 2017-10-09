package com.wangrg.java_lib.java_program.student_manage_system.dao;

import com.wangrg.java_lib.db3.Dao;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.constant.CourseSortType;

import java.util.List;

/**
 * by wangrongjun on 2017/9/12.
 */
public interface ICourseDao extends Dao<Course> {

    /**
     * 查询出所有课程（包括选课人数和当前用户是否已选）
     */
    List<Course> queryAllWithCountAndSelectedState(long studentId, int offset, int rowCount, CourseSortType sortType);

    /**
     * 查询出某个学生所有已选课程
     */
    List<Course> querySelectedWithCountAndSelectedState(long studentId, int offset, int rowCount);

    /**
     * 查询出所有课程（包括选课人数）
     */
    List<Course> queryAllWithCount(int offset, int rowCount, CourseSortType sortType);

    /**
     * 查询出某个学生所有已选课程的数量
     */
    int querySelectedCount(long studentId);

}
