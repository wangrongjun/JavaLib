package com.wangrg.example;

import com.wangrg.db.Dao;
import com.wangrg.db.connection.DbcpMysqlDbHelper;
import com.wangrg.java_program.shopping_system.bean.Comment;
import com.wangrg.java_program.shopping_system.bean.Favourite;
import com.wangrg.java_program.shopping_system.bean.Good;
import com.wangrg.java_program.shopping_system.bean.GoodImage;
import com.wangrg.java_program.shopping_system.bean.Orders;
import com.wangrg.java_program.shopping_system.bean.Shop;
import com.wangrg.java_program.shopping_system.bean.User;

import java.sql.SQLException;

/**
 * by 王荣俊 on 2016/6/4.
 */
public abstract class ExampleDao<T> extends Dao<T> {

    static {
        DbcpMysqlDbHelper helper = new DbcpMysqlDbHelper("root", "21436587", "shopping_system");
        Dao dao = new Dao(helper) {
            @Override
            protected Class getEntityClass() {
                return null;
            }

            @Override
            protected boolean isPrintSql() {
                return true;
            }
        };
        try {

            System.out.println("\n\n\n\n---------------------------------------------------------");

            dao.createTable(User.class);
            dao.createTable(Shop.class);
            dao.createTable(Orders.class);
            dao.createTable(Good.class);
            dao.createTable(GoodImage.class);
            dao.createTable(Favourite.class);
            dao.createTable(Comment.class);

            dao.createReferences(User.class);
            dao.createReferences(Shop.class);
            dao.createReferences(Orders.class);
            dao.createReferences(Good.class);
            dao.createReferences(GoodImage.class);
            dao.createReferences(Favourite.class);
            dao.createReferences(Comment.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("CustomDao:static  ---  create tables finished");
    }

    public ExampleDao() {
        super(new DbcpMysqlDbHelper("root", "21436587", "shopping_system"));
    }

}