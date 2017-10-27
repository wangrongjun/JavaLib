package com.wangrj.java_lib.java_program.shopping_system.dao;

import com.wangrj.java_lib.java_program.shopping_system.bean.GoodImage;
import com.wangrj.java_lib.java_program.shopping_system.bean.GoodImage;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GoodImageDao extends CustomDao<GoodImage> {
    /**
     * 根据商品id查询该商品的所有图片
     */
    public List<GoodImage> query(int goodId) throws SQLException {
        return query("goodId", goodId + "");
    }

    @Override
    protected Class<GoodImage> getEntityClass() {
        return GoodImage.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
