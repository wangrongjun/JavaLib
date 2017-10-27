package com.wangrj.java_lib.db2.example.dao;

import com.wangrj.java_lib.db2.Dao;
import com.wangrj.java_lib.db2.example.bean.Department;
import com.wangrj.java_lib.db2.Dao;
import com.wangrj.java_lib.db2.example.bean.Department;

import java.util.List;

/**
 * by wangrongjun on 2017/6/15.
 */

public interface DepartmentDao extends Dao<Department> {
    List<Department> queryByName(String name);
}
