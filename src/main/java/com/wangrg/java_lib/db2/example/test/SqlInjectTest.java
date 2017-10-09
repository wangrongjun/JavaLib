package com.wangrg.java_lib.db2.example.test;

import com.wangrg.java_lib.db2.example.bean.Department;
import com.wangrg.java_lib.db2.example.dao.DepartmentDao;
import com.wangrg.java_lib.db2.example.dao.impl.DepartmentDaoImpl;
import com.wangrg.java_lib.java_util.GsonUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * by wangrongjun on 2017/6/17.
 */

public class SqlInjectTest {

    private DepartmentDao departmentDao;

    @Before
    public void insertRecord() {
        departmentDao = new DepartmentDaoImpl();
        departmentDao.dropTable();
        departmentDao.createTable();
        departmentDao.insert(new Department("Research and development department"));
        departmentDao.insert(new Department("Finance department"));
        departmentDao.insert(new Department("Personnel department"));
        departmentDao.insert(new Department("Sales department"));
    }

    @Test
    public void testSqlInject() {

        /**
         * 如果后面还有条件或limit等，则可能会失效。
         */
        String name1 = "name' or '1'='1";
        /**
         * name2比name1好的地方就在于name2通过末尾的-- 来把后面的语句全部注释了。
         */
        String name2 = "name' or 1=1;-- ";

        GsonUtil.printFormatJson(departmentDao.queryByName(name1));
        GsonUtil.printFormatJson(departmentDao.queryByName(name2));
    }

}
