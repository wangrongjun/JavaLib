package com.wangrg.db2.example.dao.impl;


import com.wangrg.db2.Where;
import com.wangrg.db2.example.bean.Position;
import com.wangrg.db2.example.dao.OADao;
import com.wangrg.db2.example.dao.PositionDao;

import java.util.List;

/**
 * by wangrongjun on 2017/6/14.
 */

public class PositionDaoImpl extends OADao<Position> implements PositionDao {
    @Override
    protected Class<Position> getEntityClass() {
        return Position.class;
    }

    @Override
    public List<Position> queryByDepartmentId(int departmentId) {
        return query(Where.build("department", departmentId + ""));
    }
}
