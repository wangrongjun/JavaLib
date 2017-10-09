package com.wangrg.java_lib.java_program.shopping_system.dao;

import com.wangrg.java_lib.db.Dao;
import com.wangrg.java_lib.db.connection.MysqlDbHelper;

/**
 * by 王荣俊 on 2016/6/4.
 */
public abstract class CustomDao<T> extends Dao<T> {
    /*
        static {
            MysqlDbHelper helper = new MysqlDbHelper("root", "21436587", "shopping_system");
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
    */
    public CustomDao() {
        super(new MysqlDbHelper("root", "21436587", "shopping_system"));
    }

}
