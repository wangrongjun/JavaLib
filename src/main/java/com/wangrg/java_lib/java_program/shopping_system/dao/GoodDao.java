package com.wangrg.java_lib.java_program.shopping_system.dao;

import com.wangrg.java_lib.java_program.shopping_system.bean.Good;
import com.wangrg.java_lib.java_util.DebugUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GoodDao extends CustomDao<Good> {

    public List<Good> queryGoodListByShopId(int shopId) throws SQLException {
        return query("shopId", shopId + "");
    }

    /**
     * 获取按照销售量从大到小排序的商品列表
     */
    public List<Good> queryGoodListOrderBest() throws SQLException {
        String sql = "select * from Good,Orders where Good.goodId=Orders.goodId " +
                "group by Good.goodId order by sum(count) desc;";
        DebugUtil.println(sql + "\n");
        return executeQuery(Good.class, sql);
    }

    @Override
    protected Class<Good> getEntityClass() {
        return Good.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
