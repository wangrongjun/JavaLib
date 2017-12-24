package com.wangrj.java_lib.db3;

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

    public static <T> List<T> getResult(Class<T> entityClass, ResultSet rs) throws SQLException {
        List<T> entityList = new ArrayList<>();
        while (rs.next()) {
            T entity = ReflectUtil.setObjectValue(entityClass, false, new ReflectUtil.GetValue() {
                @Override
                public Object get(Field field, boolean isBasicType) {
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

    /**
     * @param testClassInstance 包含各种Dao成员变量的测试类实例。其中dao必须继承v3.BaseDao
     */
    public static void dropAndCreateTables(Object testClassInstance) throws Exception {
        Field[] fields = testClassInstance.getClass().getDeclaredFields();
        for (int i = fields.length - 1; i >= 0; i--) {
            Field field = fields[i];
            if (!field.getName().endsWith("Dao")) {
                continue;
            }
            field.setAccessible(true);// 获取权限：读取obj里面设置为private的xxDao变量
            Method dropTable = field.getType().getMethod("dropTable");
            dropTable.setAccessible(true);
            dropTable.invoke(field.get(testClassInstance));
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (!field.getName().endsWith("Dao")) {
                continue;
            }
            field.setAccessible(true);
            Method createTable = field.getType().getMethod("createTable");
            createTable.setAccessible(true);
            createTable.invoke(field.get(testClassInstance));
        }
    }

    /**
     * 移除并创建表
     * <p>
     * 本方法的局限性是测试类中的 dao 必须有 queryById 方法且返回 List 集合对象
     */
    public static void dropAndCreateTables(Config config, Object testClassInstance) {
        List<Class> pojoClassList = new ArrayList<>();
        Field[] fields = testClassInstance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().endsWith("Dao")) {
                Class pojoClass = null;
                try {
                    pojoClass = field.getType().getDeclaredMethod("queryById", Integer.class).getReturnType();
                } catch (NoSuchMethodException ignored) {
                }
                try {
                    pojoClass = field.getType().getDeclaredMethod("queryById", Long.class).getReturnType();
                } catch (NoSuchMethodException ignored) {
                }
                if (pojoClass == null) {
                    throw new RuntimeException(new NoSuchMethodException("queryById"));
                }
                pojoClassList.add(pojoClass);
            }
        }
        dropAndCreateTables(config, pojoClassList);
    }

    /**
     * 移除并创建表。按照列表倒序移除表，按照列表正序创建表。
     * <p>
     * 例如：classList：Job,Emp，Emp持有Job的引用。执行顺序如下：
     * 1.移除Emp表
     * 2.移除Job表
     * 3.创建Job表
     * 4.创建Emp表
     */
    public static void dropAndCreateTables(Config config, List<Class> classList) {
        for (int i = classList.size() - 1; i >= 0; i--) {
            Class cls = classList.get(i);
            class TempDao extends BaseDao {
                @Override
                protected Class getEntityClass() {
                    if (entityClass == null) {
                        entityClass = cls;
                    }
                    return entityClass;
                }

                private TempDao() {
                    super(config);
                }
            }
            new TempDao().dropTable();
        }
        for (int i = 0; i < classList.size(); i++) {
            Class cls = classList.get(i);
            class TempDao extends BaseDao {
                @Override
                protected Class getEntityClass() {
                    if (entityClass == null) {
                        entityClass = cls;
                    }
                    return entityClass;
                }

                private TempDao() {
                    super(config);
                }
            }
            new TempDao().createTable();
        }
    }

}
