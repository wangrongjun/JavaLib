package com.wangrg.java_lib.java_program.shopping_system.dao;

import com.wangrg.java_lib.java_program.shopping_system.bean.Orders;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class OrdersDao extends CustomDao<Orders> {

    public List<Orders> query(int userId) throws SQLException {
        return query("userId", userId + "");
    }

    @Override
    protected Class<Orders> getEntityClass() {
        return Orders.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
