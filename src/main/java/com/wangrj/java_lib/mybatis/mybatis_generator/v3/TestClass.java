package com.wangrj.java_lib.mybatis.mybatis_generator.v3;

import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * by wangrongjun on 2017/11/16.
 */
public class TestClass {

    interface MybatisDao<T> {

        @InsertProvider(type = AbstractMybatisDao.class, method = "insertSql")
        @SelectKey(statement = "SELECT sequence_Job.currval FROM dual", keyProperty = "jobId",
                before = false, resultType = int.class)
        default int insert() throws Exception {
            return 0;
        }

        default Class getGenericClass() {
            Class interFace = this.getClass().getInterfaces()[0];
            ParameterizedType type;
            try {
                type = (ParameterizedType) interFace.getGenericInterfaces()[0];
            } catch (ClassCastException e) {
                System.err.println("interface [" + interFace.getName() + "] should has a generic type");
                return Object.class;
            }
            Type[] types = type.getActualTypeArguments();
            Class genericClass = null;
            if (types != null && types.length > 0) {
                genericClass = (Class) types[0];
            }
            return genericClass;
        }
    }

    static class AbstractMybatisDao<T> implements MybatisDao<T> {
        public String insertSql() {
            Class cls = getGenericClass();
            String s = new SQL() {{
                INSERT_INTO(cls.getSimpleName());
            }}.toString();
            System.out.println(s);
            return s;
        }
    }

    interface JobDao extends MybatisDao<Job> {
    }

    public static void main(String[] args) throws Exception {
        new JobDao() {
            @Override
            public int insert() throws Exception {
                Method insert = this.getClass().getDeclaredMethod("insert");
                InsertProvider insertProvider = insert.getAnnotation(InsertProvider.class);
                Object instance = insertProvider.type().newInstance();
                Method method = instance.getClass().getDeclaredMethod(insertProvider.method());
                method.setAccessible(true);
                method.invoke(instance);
                return 0;
            }
        }.insert();
    }

}
