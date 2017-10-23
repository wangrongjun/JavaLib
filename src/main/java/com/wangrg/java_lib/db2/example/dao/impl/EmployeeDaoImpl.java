package com.wangrg.java_lib.db2.example.dao.impl;

import com.wangrg.java_lib.db2.Query;
import com.wangrg.java_lib.db2.Where;
import com.wangrg.java_lib.db2.example.bean.Employee;
import com.wangrg.java_lib.db2.example.dao.EmployeeDao;
import com.wangrg.java_lib.db2.example.dao.OADao;

import java.util.List;

/**
 * by wangrongjun on 2017/6/14.
 */

public class EmployeeDaoImpl extends OADao<Employee> implements EmployeeDao {
    @Override
    protected Class<Employee> getEntityClass() {
        return Employee.class;
    }

    @Override
    public List<Employee> queryByDepartmentId(int departmentId) {
        String sql = "select Employee.* from Employee,Position,Department" +
                " where " +
                "Employee.position=Position.positionId" +
                " and " +
                "Position.department=Department.departmentId" +
                " and " +
                "Department.departmentId='" + departmentId + "';";
        // maxQueryForeignKeyLevel=1：只查到Employee中的Position就够了。不再继续查询Position中的Department
        return executeQuery(sql, 1, null, null);
    }

    /**
     * 查询月薪在4500以上的男性或者4500以下的女性
     */
    @Override
    public List<Employee> queryNew() {
        Where where = new Where().
                more("salary", "4500").and().equal("gender", Employee.GENDER_MAN + "").
                or().
                less("salary", "4500").and().equal("gender", Employee.GENDER_WOMAN + "");
        return query(Query.where(where).maxQueryForeignKeyLevel(1));
    }

}
