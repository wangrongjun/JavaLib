package com.wangrg.java_program.student_manage_system.dao.base;

import com.wangrg.db3.BaseDao;
import com.wangrg.db3.Config;
import com.wangrg.db3.db.MysqlDatabase;
import com.wangrg.db3.db.OracleDatabase;
import com.wangrg.db3.db.SqliteDatabase;

/**
 * by wangrongjun on 2017/9/11.
 */
public class StudentManageSystemDao<T> extends BaseDao<T> {
    private static final Config config =
//            new Config().setDb(new MysqlDatabase("test")).setUsername("root").setPassword("21436587");
            new Config().setDb(new OracleDatabase("orcl")).setUsername("wang").setPassword("123").setPrintResult(false);
//            new Config().setDb(new SqliteDatabase("SC.db"));

    public StudentManageSystemDao() {
        super(config);
    }
}
