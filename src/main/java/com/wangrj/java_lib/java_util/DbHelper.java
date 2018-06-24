package com.wangrj.java_lib.java_util;

import com.wangrj.java_lib.db2.Query;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.db3.BaseDao;
import com.wangrj.java_lib.db3.Config;
import com.wangrj.java_lib.db3.db.MysqlDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2018/6/22.
 */
public class DbHelper {

    private Config config;

    public static DbHelper buildForMysql(String username, String password, String dbName) {
        return new DbHelper(new Config().
                setDb(new MysqlDatabase(dbName)).
                setUsername(username).
                setPassword(password)
        );
    }

    public DbHelper(Config config) {
        this.config = config;
    }

    public boolean createTable(Class<?> entityClass) {
        return getBaseDao(entityClass).createTable();
    }

    public boolean dropTable(Class<?> entityClass) {
        return getBaseDao(entityClass).dropTable();
    }

    public boolean insert(Object entity) {
        return getBaseDao(entity.getClass()).insert(entity);
    }

    public boolean executeUpdate(String sql) {
        return getBaseDao(null).executeUpdate(sql);
    }

    public boolean update(Object entity) {
        return getBaseDao(entity.getClass()).update(entity);
    }

    public List<Map<String, Object>> queryMap(String[] resultSetColumnList, String sql, Object... paramList) throws SQLException {
        if (paramList != null) {
            for (Object param : paramList) {
                sql = sql.replaceFirst("\\?", "'" + param.toString() + "'");
            }
        }

        List<Map<String, Object>> rowList = new ArrayList<>();

        Connection conn = new BaseDao(config).getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (String column : resultSetColumnList) {
                    Object value = rs.getObject(column);
                    row.put(column, value);
                }
                rowList.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rowList;
    }

    public <T> List<T> executeQuery(Class<T> entityClass, String sql, Object... paramList) {
        if (paramList != null) {
            for (Object param : paramList) {
                sql = sql.replaceFirst("\\?", "'" + param.toString() + "'");
            }
        }
        return getBaseDao(entityClass).executeQuery(sql);
    }

    public int executeQueryCount(String sql, Object... paramList) {
        if (paramList != null) {
            for (Object param : paramList) {
                sql = sql.replaceFirst("\\?", "'" + param.toString() + "'");
            }
        }
        return getBaseDao(null).executeQueryCount(sql);
    }

    public <T> List<T> query(Class<T> entityClass, Where where) {
        return getBaseDao(entityClass).query(where);
    }

    public <T> List<T> query(Class<T> entityClass, Query query) {
        return getBaseDao(entityClass).query(query);
    }

    public <T> T queryById(Class<T> entityClass, long id) {
        return (T) getBaseDao(entityClass).queryById(id);
    }

    public <T> List<T> queryAll(Class<T> entityClass) {
        return getBaseDao(entityClass).queryAll();
    }

    private BaseDao getBaseDao(Class entityCls) {
        if (entityCls == null) {
            return new BaseDao<>(config);
        }

        class DbHelperDao extends BaseDao {
            private DbHelperDao() {
                super(DbHelper.this.config);
            }

            @Override
            protected Class getEntityClass() {
                return entityCls;
            }
        }
        return new DbHelperDao();
    }
}
