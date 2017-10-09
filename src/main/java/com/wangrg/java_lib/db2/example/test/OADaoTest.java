package com.wangrg.java_lib.db2.example.test;

import com.wangrg.java_lib.db2.Query;
import com.wangrg.java_lib.db2.example.bean.Department;
import com.wangrg.java_lib.db2.example.bean.Employee;
import com.wangrg.java_lib.db2.example.bean.EmployeeLogin;
import com.wangrg.java_lib.db2.example.bean.Position;
import com.wangrg.java_lib.db2.example.dao.DepartmentDao;
import com.wangrg.java_lib.db2.example.dao.EmployeeDao;
import com.wangrg.java_lib.db2.example.dao.EmployeeLoginDao;
import com.wangrg.java_lib.db2.example.dao.PositionDao;
import com.wangrg.java_lib.db2.example.dao.impl.DepartmentDaoImpl;
import com.wangrg.java_lib.db2.example.dao.impl.EmployeeDaoImpl;
import com.wangrg.java_lib.db2.example.dao.impl.EmployeeLoginDaoImpl;
import com.wangrg.java_lib.db2.example.dao.impl.PositionDaoImpl;
import com.wangrg.java_lib.java_util.DateUtil;

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * by wangrongjun on 2017/6/14.
 */

public class OADaoTest {

    private DepartmentDao departmentDao;
    private PositionDao positionDao;
    private EmployeeDao employeeDao;
    private EmployeeLoginDao employeeLoginDao;

    @Test
    public void testOADao() throws SQLException {
        dropAndCreate();
//        createTrigger();

        Department 研发部 = new Department("研发部");
        Department 销售部 = new Department("销售部");
        departmentDao.insert(研发部);
        departmentDao.insert(销售部);

        Position 程序员 = new Position("程序员", 研发部);
        Position 技术总监 = new Position("技术总监", 研发部);
        Position 销售员 = new Position("销售员", 销售部);
        Position 销售总监 = new Position("销售总监", 销售部);
        positionDao.insert(程序员);
        positionDao.insert(技术总监);
        positionDao.insert(销售员);
        positionDao.insert(销售总监);

        Employee 张三 = new Employee(1001L, "张三111", 1L, 程序员, 8000d, DateUtil.toDate("2014-06-07 00:00:00"));
        Employee 李四 = new Employee(1002L, "李四", 0L, 程序员, 7000d, DateUtil.toDate("2014-06-08 00:00:00"));
        Employee 王五 = new Employee(1003L, "王五", 1L, 技术总监, 18000d, DateUtil.toDate("2014-06-09 00:00:00"));
        Employee 赵六 = new Employee(1004L, "赵六", 0L, 销售员, 4000d, DateUtil.toDate("2014-06-10 00:00:00"));
        Employee 田七 = new Employee(1005L, "田七", 0L, 销售员, 5000d, DateUtil.toDate("2014-06-11 00:00:00"));
        Employee 陆八 = new Employee(1006L, "陆八", 1L, 销售总监, 15000d, DateUtil.toDate("2014-09-01 00:00:00"));
        Employee 薄九 = new Employee(1007L, "薄九", 0L, null, 2000d, DateUtil.toDate("2014-09-02 00:00:00"));// 实习生刚入职，还未分配职位
        employeeDao.insert(张三);
        employeeDao.insert(李四);
        employeeDao.insert(王五);
        employeeDao.insert(赵六);
        employeeDao.insert(田七);
        employeeDao.insert(陆八);
        employeeDao.insert(薄九);
        张三.setName("张三");
        employeeDao.update(张三);
        赵六.setGender(1L);
        employeeDao.update(赵六);
        employeeDao.deleteById(田七.getEmployeeId());

        employeeLoginDao.insert(new EmployeeLogin(张三, "123"));
        employeeLoginDao.insert(new EmployeeLogin(李四, "123"));
        employeeLoginDao.insert(new EmployeeLogin(王五, "123"));
        employeeLoginDao.insert(new EmployeeLogin(赵六, "123"));
        employeeLoginDao.insert(new EmployeeLogin(陆八, "123"));
        employeeLoginDao.insert(new EmployeeLogin(薄九, "123"));

        employeeDao.queryByDepartmentId(2);
        employeeDao.query(new Query().
                orderBy("-salary", "name").
                limit(1, 3).
                ignore("department")
        );
        employeeLoginDao.queryAll();
        employeeDao.queryCount(null);

        employeeDao.queryById(1006);

        employeeDao.queryNew();

    }

    /**
     * 这里使用触发器的目的是为了使Employee的departmentName与Department的name同步。
     */
    private void createTrigger() throws SQLException {
//        Connection conn = employeeDao.getConnection();
        Connection conn = null;

        String sql = "drop trigger if exists SyncDepartmentName;";
        PreparedStatement ps = conn.prepareStatement(sql);
        System.out.println(ps.executeUpdate());
        ps.close();

        sql = "create trigger SyncDepartmentName " +
                "before insert on Employee " +
                "for each row " +
                "set new.departmentName=(" +
                "select Department.name from Department,Position" +
                " where " +
                "Department.departmentId=Position.department" +
                " and " +
                "Position.positionId=new.position" +
                ");";
//        ps = conn.prepareStatement(sql);
//        System.out.println(ps.executeUpdate());
//        ps.closeAndOpenMainView();

        conn.close();
    }

    private void dropAndCreate() {
        employeeLoginDao.dropTable();
        employeeDao.dropTable();
        positionDao.dropTable();
        departmentDao.dropTable();

        departmentDao.createTable();
        positionDao.createTable();
        employeeDao.createTable();
        employeeLoginDao.createTable();

        positionDao.createForeignKey();
        employeeDao.createForeignKey();
        employeeLoginDao.createForeignKey();
    }

    @Before
    public void initDao() {
        departmentDao = new DepartmentDaoImpl();
        positionDao = new PositionDaoImpl();
        employeeDao = new EmployeeDaoImpl();
        employeeLoginDao = new EmployeeLoginDaoImpl();
    }

}
