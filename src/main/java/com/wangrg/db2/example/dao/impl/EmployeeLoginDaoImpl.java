package com.wangrg.db2.example.dao.impl;

import com.wangrg.db2.Query;
import com.wangrg.db2.example.bean.EmployeeLogin;
import com.wangrg.db2.example.dao.EmployeeLoginDao;
import com.wangrg.db2.example.dao.OADao;

import java.util.Collections;
import java.util.List;

/**
 * by wangrongjun on 2017/6/15.
 */

public class EmployeeLoginDaoImpl extends OADao<EmployeeLogin> implements EmployeeLoginDao {
    @Override
    protected Class<EmployeeLogin> getEntityClass() {
        return EmployeeLogin.class;
    }

    @Override
    protected List<EmployeeLogin> executeQuery(String sql, int maxQueryForeignKeyLevel, List<String> ignoreReferenceList, List<String> requiredReferenceVariableList) {
        return super.executeQuery(
                sql,
                1,
                null,
                Collections.singletonList("name")
        );
    }

    @Override
    public List<EmployeeLogin> query(Query query) {
        return super.query(query);
    }
}
