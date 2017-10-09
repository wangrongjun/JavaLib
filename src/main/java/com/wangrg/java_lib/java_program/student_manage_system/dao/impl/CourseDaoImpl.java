package com.wangrg.java_lib.java_program.student_manage_system.dao.impl;

import com.wangrg.java_lib.db3.db.sql_creator.DefaultCreator;
import com.wangrg.java_lib.java_program.student_manage_system.bean.Course;
import com.wangrg.java_lib.java_program.student_manage_system.constant.CourseSortType;
import com.wangrg.java_lib.java_program.student_manage_system.dao.ICourseDao;
import com.wangrg.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;

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

    @Override
    public List<Course> queryAllWithCountAndSelectedState(long studentId, int offset, int rowCount,
                                                          CourseSortType sortType) {
        String[] orderBy;
        if (sortType == CourseSortType.SORT_BY_COURSE_ID) {
            orderBy = new String[]{"courseId"};
        } else {
            orderBy = new String[]{"selectedCount", "courseId"};
        }
        return queryWithCountAndSelectedState(false, studentId, offset, rowCount, orderBy);
    }

    @Override
    public List<Course> querySelectedWithCountAndSelectedState(long studentId, int offset, int rowCount) {
        return queryWithCountAndSelectedState(true, studentId, offset, rowCount, new String[]{"courseId"});
    }

    @Override
    public int querySelectedCount(long studentId) {
        String sql = "select count(1) from SC where student=" + studentId;
        return executeQueryCount(sql);
    }

    /**
     * select courseId, courseName, selectedCount, selectedCount/sc_count.c percentage
     * from Course
     * cross join (select count(1) c from SC) sc_count
     * left join (select course, count(1) selectedCount from SC group by course) course_count
     * on Course.courseId = course_count.course
     */
    @Override
    public List<Course> queryAllWithCount(int offset, int rowCount, CourseSortType sortType) {
        String[] orderBy;
        if (sortType == CourseSortType.SORT_BY_COURSE_ID) {
            orderBy = new String[]{"courseId"};
        } else {
            orderBy = new String[]{"selectedCount", "courseId"};
        }

        List<Course> courseList = new ArrayList<>();

        String sql = "select courseId, courseName, selectedCount, selectedCount/sc_count.c percentage" +
                "\nfrom Course" +
                "\ncross join (select count(1) c from SC) sc_count" +
                "\nleft join (select course, count(1) selectedCount from SC group by course) course_count" +
                "\non Course.courseId = course_count.course\n" + DefaultCreator.createOrderBy(orderBy);

        sql = getDb().getSqlCreator().wrapLimit(sql, offset, rowCount);
        printSql(sql);

        try (Connection conn = getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Course course = new Course(rs.getInt("courseId"), rs.getString("courseName"));
                course.setSelectedCount(rs.getInt("selectedCount"));
                course.setSelectedPercentage(rs.getDouble("percentage"));
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

    /**
     * select courseId, courseName, selectedCount, student, selectedCount/(select count(1) from SC) percentage
     * from Course
     * left join (select course, count(1) selectedCount from SC group by course) course_count
     * on Course.courseId = course_count.course
     * left join SC -- 这个连接的目的就是为了获取学号student
     * on Course.courseId = SC.course and student = '3114006535' -- 避免记录中有多个重复课程（因为学号不同）
     */
    public List<Course> queryWithCountAndSelectedState(boolean onlySelectedCourse, long studentId,
                                                       int offset, int rowCount, String[] orderBy) {

        List<Course> courseList = new ArrayList<>();
//        String sql = "SELECT * FROM sc RIGHT JOIN course ON sc.course=course.courseid ORDER BY courseid";
        String sql = "select courseId, courseName, selectedCount, student, selectedCount/(select count(1) from SC) percentage" +
                "\nfrom Course" +
                "\nleft join (select course, count(1) selectedCount from SC group by course) course_count" +
                "\non Course.courseId = course_count.course" +
                "\nleft join SC" +
                "\non Course.courseId = SC.course\n" +
                (onlySelectedCourse ? "where" : "and") +
                " student = '" + studentId + "'\n" +
                DefaultCreator.createOrderBy(orderBy);

        sql = getDb().getSqlCreator().wrapLimit(sql, offset, rowCount);
        printSql(sql);

        try (Connection conn = getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Course course = new Course(rs.getInt("courseId"), rs.getString("courseName"));
                course.setSelectedCount(rs.getInt("selectedCount"));
                String student = String.valueOf(rs.getLong("student"));
                course.setSelected(onlySelectedCourse || String.valueOf(studentId).equals(student));
                course.setSelectedPercentage(rs.getDouble("percentage"));
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
