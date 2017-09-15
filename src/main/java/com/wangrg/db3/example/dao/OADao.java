package com.wangrg.db3.example.dao;

import com.wangrg.db3.BaseDao;
import com.wangrg.db3.Config;
import com.wangrg.db3.Dao;
import com.wangrg.db3.db.OracleDatabase;
import com.wangrg.db3.db.SqliteDatabase;

/**
 * by wangrongjun on 2017/8/23.
 */

public class OADao<T> extends BaseDao<T> implements Dao<T> {
    public OADao() {
//        super(new Config().setUsername("wang").setPassword("123").setDb(new OracleDatabase("orcl")).setUseDbcp(true));
        super(new Config().setDb(new SqliteDatabase("E:/OA.db")));
//        super(new Config().setUsername("root").setPassword("21436587").setDb(new MysqlDatabase("oa")));
    }
}
