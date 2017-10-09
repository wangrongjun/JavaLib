package com.wangrg.java_lib.java_program.shopping_system;

import com.wangrg.java_lib.db.Dao;
import com.wangrg.java_lib.db.SqlUtil;
import com.wangrg.java_lib.db.connection.MysqlDbHelper;
import com.wangrg.java_lib.java_program.shopping_system.bean.Comment;
import com.wangrg.java_lib.java_program.shopping_system.bean.Favourite;
import com.wangrg.java_lib.java_program.shopping_system.bean.Good;
import com.wangrg.java_lib.java_program.shopping_system.bean.GoodImage;
import com.wangrg.java_lib.java_program.shopping_system.bean.Orders;
import com.wangrg.java_lib.java_program.shopping_system.bean.Shop;
import com.wangrg.java_lib.java_program.shopping_system.bean.User;
import com.wangrg.java_lib.java_program.shopping_system.dao.GoodDao;
import com.wangrg.java_lib.java_program.shopping_system.dao.OrdersDao;
import com.wangrg.java_lib.java_program.shopping_system.dao.ShopDao;
import com.wangrg.java_lib.java_program.shopping_system.dao.UserDao;
import com.wangrg.java_lib.java_util.DebugUtil;
import com.wangrg.java_lib.java_util.FileUtil;
import com.wangrg.java_lib.java_util.HtmlCreateUtil;

import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/1/4.
 */
public class TestDao {

    public static Class[] entityClassList = {
            Orders.class,
            Comment.class,
            GoodImage.class,
            Favourite.class,
            Good.class,
            Shop.class,
            User.class,
    };

    @Test
    public void test() throws Exception {
        testCreateTable();
        testInsertData();
    }

    @Test
    public void testCreateTable() throws Exception {

        Dao dao = new Dao(new MysqlDbHelper("root", "21436587", "shopping_system")) {
            @Override
            protected Class getEntityClass() {
                return null;
            }

            @Override
            protected boolean isPrintSql() {
                return true;
            }
        };

        // 1.删除原先的表
        for (Class entityClass : entityClassList) {
            String sql = SqlUtil.dropTableSql(entityClass.getSimpleName());
            dao.execute(sql);
        }

        // 2.创建表
        for (Class entityClass : entityClassList) {
            dao.createTable(entityClass);
        }

        // 3.创建外键
        for (Class entityClass : entityClassList) {
            dao.createReferences(entityClass);
        }
    }

    @Test
    public void testInsertData() throws Exception {
        // 1.添加数据
        UserDao userDao = new UserDao();
        System.out.println("-----------------   start userDao.insert   -------------------");
        int wangUserId = userDao.insert(new User("15521302230", "123", "wang_rong_jun", "handsome", 1));
        int moBaoUserId = userDao.insert(new User("13023796942", "123", "mo_bao_er", "beautiful", 0));
        int customerAId = userDao.insert(new User("13710512639", "123", "customerA", "strong", 1));
        int customerBId = userDao.insert(new User("13710512630", "123", "customerB", "pretty", 0));
        ShopDao shopDao = new ShopDao();
        System.out.println("-----------------   start shopDao.insert   -------------------");
        int iphoneShopId = shopDao.insert(new Shop(wangUserId, "iphone shop"));
        int gamePlayerShopId = shopDao.insert(new Shop(wangUserId, "game player shop"));
        int chickenShopId = shopDao.insert(new Shop(moBaoUserId, "chicken shop"));
        int clothesShopId = shopDao.insert(new Shop(moBaoUserId, "clothes shop"));
        GoodDao goodDao = new GoodDao();
        System.out.println("-----------------   start goodDao.insert   -------------------");
        int iPhone4Id = goodDao.insert(new Good(iphoneShopId, "iPhone4", "apple phone, very good!!!", 3000));
        int iPhone7Id = goodDao.insert(new Good(iphoneShopId, "iPhone7", "apple phone, very good!!!", 6499));
        int gbaPlayerId = goodDao.insert(new Good(gamePlayerShopId, "gba player", "play many gba game", 200));
        int ndsPlayerId = goodDao.insert(new Good(gamePlayerShopId, "nds player", "play many nds game", 400));
        int pspPlayerId = goodDao.insert(new Good(gamePlayerShopId, "psp player", "play many psp game", 800));
        int chickenGirlId = goodDao.insert(new Good(chickenShopId, "chicken girl", "you can do love!!!", 300));
        int tShirtId = goodDao.insert(new Good(clothesShopId, "t-shirt", "very red", 80));
        int capId = goodDao.insert(new Good(clothesShopId, "cap", "very cool", 20));
        OrdersDao ordersDao = new OrdersDao();
        System.out.println("-----------------   start ordersDao.insert   -------------------");
        ordersDao.insert(new Orders(customerAId, gbaPlayerId, 3, "2016-11-11"));
        ordersDao.insert(new Orders(customerAId, ndsPlayerId, 1, "2016-11-11"));
        ordersDao.insert(new Orders(customerAId, pspPlayerId, 1, "2016-11-11"));
        ordersDao.insert(new Orders(customerAId, chickenGirlId, 1, "2016-11-12"));
        ordersDao.insert(new Orders(customerBId, tShirtId, 2, "2016-03-01"));
        ordersDao.insert(new Orders(customerBId, capId, 3, "2016-03-01"));
        ordersDao.insert(new Orders(customerBId, iPhone4Id, 1, "2016-03-01"));
        ordersDao.insert(new Orders(customerBId, iPhone7Id, 1, "2016-03-010"));

        System.out.println("\n\n--------------------   showCourse data   -----------------\n\n");

        // 2.显示数据
        List<User> userList = userDao.queryAll();
        List<Shop> shopList = shopDao.queryAll();
        List<Good> goodList = goodDao.queryAll();
        List<Orders> ordersList = ordersDao.queryAll();
        DebugUtil.printlnEntity(userList);
        DebugUtil.printlnEntity(shopList);
        DebugUtil.printlnEntity(goodList);
        DebugUtil.printlnEntity(ordersList);

        // 3.把数据显示到html
        Connection conn = userDao.getConnection();
        List<HtmlCreateUtil.Table> tableList = new ArrayList<>();
        tableList.add(HtmlCreateUtil.createHtmlTable(User.class, conn));
        tableList.add(HtmlCreateUtil.createHtmlTable(Shop.class, conn));
        tableList.add(HtmlCreateUtil.createHtmlTable(Good.class, conn));
        tableList.add(HtmlCreateUtil.createHtmlTable(Orders.class, conn));
        conn.close();
        String html = HtmlCreateUtil.createHtml(tableList);
        FileUtil.write(html, "E:/shopping_system_db.html");
    }

}
