package com.wangrj.java_lib.db3;

import com.wangrj.java_lib.db3.db.MysqlDatabase;
import com.wangrj.java_lib.db3.db.OracleDatabase;
import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.db3.db.MysqlDatabase;
import com.wangrj.java_lib.db3.db.OracleDatabase;
import com.wangrj.java_lib.java_util.ReflectUtil;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/10/11.
 */
public class DbUtil {

    public static Connection getMysqlConnection(String username, String password, String dbName) {
        return new BaseDao(new Config().
                setUsername(username).
                setPassword(password).
                setDb(new MysqlDatabase(dbName)
                )).getConnection();
    }

    public static Connection getOracleConnection(String username, String password, String dbName) {
        return new BaseDao(new Config().
                setUsername(username).
                setPassword(password).
                setDb(new OracleDatabase(dbName)
                )).getConnection();
    }

    /**
     * @param obj 包含各种Dao成员变量的测试类实例
     */
    public static void dropAndCreateTables(Object obj) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            if (!field.getName().endsWith("Dao")) {
                continue;
            }
            field.setAccessible(true);// 获取权限：读取obj里面设置为private的xxDao变量
            Method dropTable = field.getType().getMethod("dropTable");
            dropTable.setAccessible(true);
            dropTable.invoke(field.get(obj));
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.getName().endsWith("Dao")) {
                continue;
            }
            field.setAccessible(true);
            Method createTable = field.getType().getMethod("createTable");
            createTable.setAccessible(true);
            createTable.invoke(field.get(obj));
        }
    }

    public static <T> List<T> getResult(Class<T> entityClass, ResultSet rs) throws SQLException {
        List<T> entityList = new ArrayList<>();
        while (rs.next()) {
            T entity = ReflectUtil.setObjectValue(entityClass, false, new ReflectUtil.GetValue() {
                @Override
                public Object get(Field field) {
                    try {
                        return rs.getObject(field.getName());
                    } catch (SQLException ignored) {
                    }
                    return null;
                }
            });
            entityList.add(entity);
        }
        return entityList;
    }

    public static Field getForeignKeyIdField(Field field) {
        if (field.getAnnotation(ManyToOne.class) == null &&
                field.getAnnotation(OneToOne.class) == null) {
            return null;
        }
        Field[] foreignKeyClassFields = field.getType().getDeclaredFields();
        for (Field f : foreignKeyClassFields) {
            if (f.getAnnotation(Id.class) != null) {
                return f;
            }
        }
        return null;
    }

}
