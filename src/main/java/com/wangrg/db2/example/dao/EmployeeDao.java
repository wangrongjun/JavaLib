package com.wangrg.db2.example.dao;

import com.wangrg.db2.Dao;
import com.wangrg.db2.example.bean.Employee;

import java.util.List;

/**
 * by wangrongjun on 2017/6/17.
 */

public interface EmployeeDao extends Dao<Employee> {

    List<Employee> queryByDepartmentId(int departmentId);

    /**
     * 查询月薪在4500以上的男性或者4500以下的女性
     */
    List<Employee> queryNew();

}
