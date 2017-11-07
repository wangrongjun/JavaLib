package com.wangrj.java_lib.mybatis;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;

/**
 * by wangrongjun on 2017/11/7.
 */
public class MybatisUtil {

    public static void initDao(Object testClassInstance, SqlSession session) throws Exception {
        Field[] fields = testClassInstance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!field.getName().endsWith("Dao")) {
                continue;
            }
            field.setAccessible(true);
            Object dao = session.getMapper(field.getType());
            field.set(testClassInstance, dao);
        }
    }

}
