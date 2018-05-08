package com.wangrj.java_lib.db3.example;

import com.wangrj.java_lib.db.connection.Dbcp;
import com.wangrj.java_lib.db2.Query;
import com.wangrj.java_lib.db3.example.bean.Dept;
import com.wangrj.java_lib.db3.example.bean.Emp;
import com.wangrj.java_lib.db3.example.bean.Pos;
import com.wangrj.java_lib.db3.example.dao.DeptDao;
import com.wangrj.java_lib.db3.example.dao.EmpDao;
import com.wangrj.java_lib.db3.example.dao.PosDao;
import com.wangrj.java_lib.java_util.DateUtil;
import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;

/**
 * by wangrgrongjun on 2017/8/23.
 */

public class OADaoTest {

    private DeptDao deptDao = new DeptDao();
    private PosDao posDao = new PosDao();
    private EmpDao empDao = new EmpDao();

    @Test
    public void testInsert() {
        testDropAndCreateTable();

        Dept 研发部 = new Dept("研发部");
        Dept 销售部 = new Dept("销售部");
        deptDao.insert(研发部);
        deptDao.insert(销售部);

        Pos 程序员 = new Pos("程序员", 研发部);
        Pos 技术总监 = new Pos("技术总监", 研发部);
        Pos 销售员 = new Pos("销售员", 销售部);
        Pos 销售总监 = new Pos("销售总监", 销售部);
        posDao.insert(程序员);
        posDao.insert(技术总监);
        posDao.insert(销售员);
        posDao.insert(销售总监);

        Emp 张三 = new Emp(1001L, "张三111", 1L, 程序员, 8000d, DateUtil.toDate("2014-06-07 10:00:00"));
        Emp 李四 = new Emp(1002L, "李四", 0L, 程序员, 7000d, DateUtil.toDate("2014-06-08 11:00:00"));
        Emp 王五 = new Emp(1003L, "王五", 1L, 技术总监, 18000d, DateUtil.toDate("2014-06-09 12:00:00"));
        Emp 赵六 = new Emp(1004L, "赵六", 0L, 销售员, 4000d, DateUtil.toDate("2014-06-10 13:00:00"));
        Emp 田七 = new Emp(1005L, "田七", 0L, 销售员, 5000d, DateUtil.toDate("2014-06-11 14:00:00"));
        Emp 陆八 = new Emp(1006L, "陆八", 1L, 销售总监, 15000d, DateUtil.toDate("2014-09-01 15:00:00"));
        // 实习生薄九刚入职，还未分配职位
        Emp 薄九 = new Emp(1007L, "薄九", 0L, null, 2000d, DateUtil.toDate("2014-09-02 16:00:00"));

        empDao.insert(张三);
        empDao.insert(李四);
        empDao.insert(王五);
        empDao.insert(赵六);
        empDao.insert(田七);
        empDao.insert(陆八);
        empDao.insert(薄九);
        张三.setEmpName("张三");
        empDao.update(张三);
        赵六.setGender(1L);
        empDao.update(赵六);
        empDao.deleteById(田七.getEmpId());
    }

    @Test
    public void testQuery() {
        empDao.queryByDeptno(1);
        empDao.query(new Query().
                orderBy("-salary", "ename").
                limit(1, 3).
                ignore("dept")
        );
        empDao.queryCount(null);
        empDao.queryById(1006);
        deptDao.queryAll();
    }

    @Test
    public void testDropAndCreateTable() {
        empDao.dropTable();
        posDao.dropTable();
        deptDao.dropTable();

        deptDao.createTable();
        posDao.createTable();
        empDao.createTable();
    }

    @After
    public void close() throws SQLException {
        Dbcp.close();
    }

}
