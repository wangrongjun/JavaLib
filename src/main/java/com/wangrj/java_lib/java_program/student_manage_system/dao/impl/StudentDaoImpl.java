package com.wangrj.java_lib.java_program.student_manage_system.dao.impl;

import com.wangrj.java_lib.java_program.student_manage_system.bean.Location;
import com.wangrj.java_lib.java_program.student_manage_system.bean.Student;
import com.wangrj.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrj.java_lib.java_program.student_manage_system.dao.IStudentDao;
import com.wangrj.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;
import com.wangrj.java_lib.java_program.student_manage_system.constant.StudentSortType;
import com.wangrj.java_lib.java_program.student_manage_system.dao.IStudentDao;
import com.wangrj.java_lib.java_program.student_manage_system.dao.base.StudentManageSystemDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/9/11.
 */
public class StudentDaoImpl extends StudentManageSystemDao<Student> implements IStudentDao {
    /**
     * select studentId,studentName,password,select_count,locationId,country,province,city,area
     * from Student s
     * left outer join (select count(1) select_count, student
     * from SC
     * group by student) c
     * on c.student = s.studentId
     * join Location loc on s.location=loc.locationId
     * order by select_count
     */
    @Override
    public List<Student> queryAllWithSelectCount(int offset, int rowCount, StudentSortType sortType) {
        List<Student> studentList = new ArrayList<>();

        String orderBy = "";
        if (sortType == StudentSortType.SORT_BY_STUDENT_ID) {
            orderBy += "order by studentId";
        } else if (sortType == StudentSortType.SORT_BY_STUDENT_NAME) {
            orderBy += "order by studentName";
        } else if (sortType == StudentSortType.SORT_BY_SELECT_COUNT) {
            orderBy += "order by select_count,studentId";
        }

        String sql = "select studentId,studentName,password,select_count,locationId,country,province,city,area\n" +
                "  from Student s\n" +
                "  left outer join (select count(1) select_count, student\n" +
                "                     from SC\n" +
                "                    group by student) c\n" +
                "    on c.student = s.studentId\n" +
                "  join Location loc on s.location=loc.locationId\n" +
                orderBy;
        sql = getDb().getSqlCreator().wrapLimit(sql, offset, rowCount);
        printSql(sql);

        try (Connection conn = getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Location location = new Location(rs.getString("country"), rs.getString("province"),
                        rs.getString("city"), rs.getString("area"));
                location.setLocationId(rs.getInt("locationId"));
                Student student = new Student(rs.getLong("studentId"), rs.getString("studentName"),
                        rs.getString("password"), location);
                student.setSelecteCourseCount(rs.getInt("select_count"));

                studentList.add(student);
            }
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        printResult(studentList);
        return studentList;
    }

    @Override
    protected List<Student> executeQuery(String sql, int maxQueryForeignKeyLevel, List<String> ignoreReferenceList,
                                         List<String> requiredReferenceVariableList) {
        // 把默认为32改为默认为1
        maxQueryForeignKeyLevel = maxQueryForeignKeyLevel == 0 ? 1 : maxQueryForeignKeyLevel;
        return super.executeQuery(sql, maxQueryForeignKeyLevel, ignoreReferenceList, requiredReferenceVariableList);
    }
}
