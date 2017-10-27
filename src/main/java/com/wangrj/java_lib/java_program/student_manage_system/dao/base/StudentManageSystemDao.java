package com.wangrj.java_lib.java_program.student_manage_system.dao.base;

import com.wangrj.java_lib.db3.BaseDao;
import com.wangrj.java_lib.db3.Config;
import com.wangrj.java_lib.db3.db.OracleDatabase;

/**
 * by wangrongjun on 2017/9/11.
 */
public class StudentManageSystemDao<T> extends BaseDao<T> {
    private static final Config config =
//            new Config().setDb(new MysqlDatabase("test")).setUsername("root").setPassword("21436587").setPrintResult(false);
            new Config().setDb(new OracleDatabase("orcl")).setUsername("wang").setPassword("123").setPrintResult(false);
//            new Config().setDb(new SqliteDatabase("SC.db"));

    public StudentManageSystemDao() {
        super(config);
    }
}
