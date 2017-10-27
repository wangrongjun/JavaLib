package com.wangrj.java_lib.db3.example.dao;

import com.wangrj.java_lib.db3.Dao;
import com.wangrj.java_lib.db3.example.bean.Emp;
import com.wangrj.java_lib.db3.Dao;

import java.util.List;

/**
 * by wangrongjun on 2017/8/23.
 */

public class EmpDao extends OADao<Emp> implements Dao<Emp> {

    public List<Emp> queryByDeptno(long deptno) {
        String sql = "select emp.* from emp\n" +
                "\tjoin pos on emp.pos=pos.posId\n" +
                "\tjoin dept on pos.dept=dept.deptno\n" +
                "\twhere deptno=" + deptno;
        return executeQuery(sql, 1, null, null);
    }

}
