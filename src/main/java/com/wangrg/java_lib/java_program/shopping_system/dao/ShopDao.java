package com.wangrg.java_lib.java_program.shopping_system.dao;

import com.wangrg.java_lib.java_program.shopping_system.bean.Shop;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class ShopDao extends CustomDao<Shop> {

    /**
     * 根据商店id进行模精确查询
     */
    public Shop query(int shopId) throws SQLException {
        List<Shop> shopList = query("shopId", shopId + "");
        if (shopList != null && shopList.size() > 0) {
            return shopList.get(0);
        }
        return null;
    }

    /**
     * 根据商店名进行模糊查询
     */
    public List<Shop> queryFuzzy(String shopName) throws SQLException {
        // TODO
//        return query(Shop.class, "shopName", shopName, true);
        return null;
    }

    @Override
    protected Class<Shop> getEntityClass() {
        return Shop.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
