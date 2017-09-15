package com.wangrg.java_program.student_manage_system.dao.impl;

import com.wangrg.java_program.student_manage_system.bean.Course;
import com.wangrg.java_program.student_manage_system.dao.ICourseDao;
import com.wangrg.java_program.student_manage_system.dao.base.StudentManageSystemDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/9/11.
 */
public class CourseDaoImpl extends StudentManageSystemDao<Course> implements ICourseDao {

    /**
     * select courseId, courseName, selectedCount, student
     * from Course
     * left join (select course, count(1) selectedCount from SC group by course) course_count
     * on Course.courseId = course_count.course
     * left join SC
     * on Course.courseId = SC.course and student = '3114006535' -- 避免重复
     */
    @Override
    public List<Course> queryAllWithCountAndSelectedState(long studentId, int offset, int rowCount) {
        List<Course> courseList = new ArrayList<>();
//        String sql = "SELECT * FROM sc RIGHT JOIN course ON sc.course=course.courseid ORDER BY courseid";
        String sql = "select courseId, courseName, selectedCount, student from Course " +
                "\nleft join (select course, count(1) selectedCount from SC group by course) course_count " +
                "\non Course.courseId = course_count.course" +
                "\nleft join SC" +
                "\non Course.courseId = SC.course and student = '" + studentId + "' " +
                "\norder by courseId";
        sql = getDb().getSqlCreator().wrapLimit(sql, offset, rowCount);
        printSql(sql);

        try (Connection conn = getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Course course = new Course(rs.getInt("courseId"), rs.getString("courseName"));
                course.setSelectedCount(rs.getInt("selectedCount"));
                String student = String.valueOf(rs.getLong("student"));
                course.setSelected(String.valueOf(studentId).equals(student));
                courseList.add(course);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        printResult(courseList);
        return courseList;
    }

}
