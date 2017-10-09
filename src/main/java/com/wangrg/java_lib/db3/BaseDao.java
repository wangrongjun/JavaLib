package com.wangrg.java_lib.db3;

import com.wangrg.java_lib.db.connection.Dbcp;
import com.wangrg.java_lib.db2.Id;
import com.wangrg.java_lib.db2.Ignore;
import com.wangrg.java_lib.db2.Query;
import com.wangrg.java_lib.db2.Reference;
import com.wangrg.java_lib.db2.Where;
import com.wangrg.java_lib.db3.db.IDataBase;
import com.wangrg.java_lib.java_util.GsonUtil;
import com.wangrg.java_lib.java_util.ListUtil;
import com.wangrg.java_lib.java_util.LogUtil;
import com.wangrg.java_lib.java_util.ReflectUtil;
import com.wangrg.java_lib.java_util.TextUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T> implements Dao<T> {

    protected String username;
    protected String password;
    protected IDataBase db;
    private static boolean printSql;
    private static boolean printResult;
    private static boolean printLogHint;
    private boolean useDbcp;

    protected Class<T> entityClass;

    public BaseDao(Config config) {
        this.username = config.getUsername();
        this.password = config.getPassword();
        this.db = config.getDb();
        BaseDao.printSql = config.isPrintSql();
        BaseDao.printResult = config.isPrintResult();
        BaseDao.printLogHint = config.isPrintLogHint();
        useDbcp = config.isUseDbcp();
    }

    protected Class<T> getEntityClass() {
        if (entityClass == null) {
            Class<? extends BaseDao> cls = this.getClass();
            ParameterizedType type = (ParameterizedType) cls.getGenericSuperclass();
            Type[] types = type.getActualTypeArguments();
            if (types != null && types.length > 0) {
                entityClass = (Class<T>) types[0];
            }
        }
        return entityClass;
    }

    public Connection getConnection() {
        try {
            if (useDbcp) {
                return Dbcp.getConnection(db.getUrl(), db.getDriverName(), username, password);
            } else {
                Class.forName(db.getDriverName());
                return DriverManager.getConnection(db.getUrl(), username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected IDataBase getDb() {
        return db;
    }

    public void close(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Field idField;

    private Field getIdField() {
        if (idField == null) {
            idField = ReflectUtil.findByAnno(getEntityClass(), Id.class);
        }
        return idField;
    }

    private String getTableName() {
        return getEntityClass().getSimpleName();
    }

    protected static void printSql(String sql) {
        if (printSql) {
            if (printLogHint) {
                System.out.println();
                LogUtil.print(sql, BaseDao.class.getName());
                System.out.println();
            } else {
                System.out.println("\n" + sql);
            }
        }
    }

    protected static void printResult(List list) {
        if (printResult) {
            System.out.println("\n----------- begin -----------");
            GsonUtil.printFormatJson(list);
            System.out.println("-----------  end  -----------\n");
        }
    }

    protected static void printResult(long count) {
        if (printResult) {
            System.out.println("\n----------- begin -----------");
            GsonUtil.printFormatJson("count: " + count);
            System.out.println("-----------  end  -----------\n");
        }
    }

    protected boolean executeUpdate(String sql) {
        return executeUpdate(ListUtil.build(sql));
    }

    protected synchronized boolean executeUpdate(List<String> sqlList) {
        boolean succeed = true;
        Connection conn = getConnection();
        for (String sql : sqlList) {
            if (TextUtil.isEmpty(sql)) {
                continue;
            }
            printSql(sql);
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate(sql);
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
                succeed = false;
            }
        }
        close(conn);
        return succeed;
    }

    @Override
    public boolean createTable() {
        List<String> sqlList = db.createTableSql(getEntityClass());
        return executeUpdate(sqlList);
    }

    @Override
    public boolean dropTable() {
        List<String> sqlList = db.dropTableSql(getEntityClass());
        return executeUpdate(sqlList);
    }

    @Override
    public synchronized boolean insert(T entity) {
        boolean succeed = false;
        String sql = db.insertSql(entity);
        printSql(sql);
        Connection conn = getConnection();
        try {
            Field idField = getIdField();
            idField.setAccessible(true);
            boolean autoIncrement = idField.getAnnotation(Id.class).autoIncrement();
            long id = db.insert(conn, entity.getClass().getSimpleName(), autoIncrement, sql);
            if (autoIncrement) {// 如果id是自增，才把insert返回的id设置进entity
                switch (idField.getType().getSimpleName()) {
                    case "int":
                        idField.setInt(entity, (int) id);
                    case "Integer":
                        idField.set(entity, Integer.parseInt(id + ""));
                        break;
                    case "long":
                        idField.setLong(entity, id);
                    case "Long":
                        idField.set(entity, Long.valueOf(id));
                        break;
                }
            }
            succeed = true;
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close(conn);
        }
        return succeed;
    }

    @Override
    public synchronized boolean delete(Where where) {
        db.deleteSql(getEntityClass());
        String sql = db.getSqlCreator().deleteSql(getTableName(), where);
        return executeUpdate(sql);
    }

    @Override
    public boolean deleteById(long id) {
        String idName = getIdField().getName();
        return delete(Where.build(idName, id + ""));
    }

    @Override
    public boolean deleteAll() {
        return delete(null);
    }

    @Override
    public boolean update(T entity) {
        String sql = db.updateSql(entity);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * @param currentLevel                  当前查询层次，首次递归为0
     * @param maxQueryForeignKeyLevel       最大查询层次。如果为0，则不查询外键对象（列表）。
     *                                      如果为1，查询外键对象（列表），但不查询
     *                                      外键对象（列表）的外键对象（列表）。如此类推。
     * @param ignoreReferenceList           查询中应该忽略的变量名列表。如果查到某个@Reference对象
     *                                      存在于忽略列表，就忽略（但还是会查询该对象的id属性）。
     * @param requiredReferenceVariableList 若不为空，则查询外键对象（该外键对象不存在于
     *                                      ignoreReferenceList中）中存在于requiredList的变量。
     *                                      <p>
     *                                      注意：外键对象的id变量和currentLevel=0时的非外键变量
     *                                      是一定要查询的，不受requiredList的影响。
     *                                      <p>
     *                                      例如，requiredList={"username"}，则查询Shop（Shop表包含
     *                                      User）时，shop对象的所有属性都有值，shop对象的
     *                                      user对象中，只有username变量有值。
     */
    private static <T> List<T> executeQuery(String sql, Class<T> entityClass,
                                            Connection conn, IDataBase db,
                                            int currentLevel, int maxQueryForeignKeyLevel,
                                            List<String> ignoreReferenceList,
                                            List<String> requiredReferenceVariableList) {
        if (currentLevel > maxQueryForeignKeyLevel) {
            return null;
        }

        printSql(sql);

        List<T> entityList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                T entity = entityClass.newInstance();
                for (Field field : entityClass.getDeclaredFields()) {
                    if (field.getAnnotation(Ignore.class) != null) {
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.getAnnotation(Reference.class) == null) {
                        // 正常赋值
                        boolean require = true;
                        if (currentLevel > 0 && field.getAnnotation(Id.class) == null &&
                                requiredReferenceVariableList != null &&
                                requiredReferenceVariableList.size() > 0) {
                            require = false;
                            for (String requireReferenceVariable : requiredReferenceVariableList) {
                                if (field.getName().equals(requireReferenceVariable)) {
                                    require = true;
                                    break;
                                }
                            }
                        }
                        if (require) {
                            db.setRsValueToEntity(field, entity, rs.getObject(field.getName()));
                        }
                    } else {
                        // 查询外键对象并赋值
                        Class innerEntityClass = field.getType();
                        Field innerIdField = ReflectUtil.findByAnno(innerEntityClass, Id.class);
                        assert innerIdField != null;
                        innerIdField.setAccessible(true);
                        String innerIdName = innerIdField.getName();
                        Object innerIdValue = rs.getObject(field.getName());
                        // 如果id<=0，则外键对象为null，没必要去判断忽略和查询外键对象，结束本次循环
                        // 一开始是使用Integer.parseInt()，结果因为数字超出范围而抛异常，改为Long
                        if (innerIdValue == null || Long.parseLong(innerIdValue + "") <= 0) {
                            continue;
                        }
                        // 判断当前变量是否要忽略
                        boolean ignore = false;
                        if (ignoreReferenceList != null && ignoreReferenceList.size() > 0) {
                            for (String ignoreName : ignoreReferenceList) {
                                if (field.getName().equals(ignoreName)) {
                                    ignore = true;
                                    break;
                                }
                            }
                        }
                        // 如果到了最后一次的递归或者需要忽略该外键对象，就只查询外键对象的id值
                        if (currentLevel >= maxQueryForeignKeyLevel || ignore) {
                            Object innerEntity = innerEntityClass.newInstance();
                            db.setRsValueToEntity(innerIdField, innerEntity, innerIdValue);
//                            innerIdField.set(innerEntity, innerIdValue);
                            field.set(entity, innerEntity);
                        } else {// 否则查询外键对象所有属性的值
                            String innerTableName = innerEntityClass.getSimpleName();
                            Query query = Query.build(Where.build(innerIdName, innerIdValue + ""));
                            String innerSql = db.getSqlCreator().querySql(innerTableName, query);
                            List innerEntityList = executeQuery(innerSql, innerEntityClass, conn,
                                    db, currentLevel + 1, maxQueryForeignKeyLevel,
                                    ignoreReferenceList, requiredReferenceVariableList);
                            if (innerEntityList != null && innerEntityList.size() > 0) {
                                field.set(entity, innerEntityList.get(0));
                            }
                        }

                    }
                }
                entityList.add(entity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return entityList;
    }

    protected List<T> executeQuery(String sql, int maxQueryForeignKeyLevel,
                                   List<String> ignoreReferenceList,
                                   List<String> requiredReferenceVariableList) {
        Connection connection = getConnection();
        List<T> list = executeQuery(sql, getEntityClass(), connection, db, 0,
                maxQueryForeignKeyLevel, ignoreReferenceList, requiredReferenceVariableList);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        printResult(list);
        return list;
    }

    /**
     * @param sql 必须以select count(*) ...开头
     */
    protected int executeQueryCount(String sql) {
        printSql(sql);
        int count = 0;
        Connection conn = getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn);
        }
        printResult(count);
        return count;
    }

    @Override
    public List<T> query(Query query) {
        String sql = db.getSqlCreator().querySql(getTableName(), query);
        return executeQuery(sql, query.getMaxQueryForeignKeyLevel(),
                query.getIgnoreReferenceList(),
                query.getRequiredReferenceVariableList()
        );
    }

    @Override
    public List<T> query(Where where) {
        return query(Query.build(where));
    }

    @Override
    public T queryById(long id) {
        Where where = Where.build(getIdField().getName(), id + "");
        List<T> list = query(where);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<T> queryAll() {
        return query(new Query());
    }

    @Override
    public int queryCount(Where where) {
        String sql = db.getSqlCreator().queryCountSql(getTableName(), where);
        return executeQueryCount(sql);
    }

}
