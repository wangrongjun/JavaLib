package com.wangrj.java_lib.db2;

import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.ReflectUtil;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T> implements Dao<T> {

    protected String username;
    protected String password;
    private String dbName;
    private static boolean printSql;
    private static boolean printResult;
    protected Class<T> entityClass;
    protected Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    /**
     * 防止输入的值中含有非法字符如单引号，末尾的斜杠等。防止sql注入攻击。
     */
    protected InputValueFormatter formatter = new InputValueFormatter.InputValueFormatterImpl();

    public BaseDao(String username, String password, String databaseName,
                   boolean printSql, boolean printResult) {
        this.username = username;
        this.password = password;
        this.dbName = databaseName;
        BaseDao.printSql = printSql;
        BaseDao.printResult = printResult;
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
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + dbName + "?characterEncoding=utf-8&" +
                            "useSSL=false",
                    username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Field getIdField() {
        return ReflectUtil.findByAnno(getEntityClass(), Id.class);
    }

    /**
     * @param entity 外键对象
     * @return 外键对象为空，返回0。外键对象不为空且存在@Id变量，返回该变量的值。否则返回-1。
     */
    private long getIdValue(Object entity) {
        if (entity == null) {
            return 0;
        }
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                field.setAccessible(true);
                try {
                    switch (field.getType().getSimpleName()) {
                        case "int":
                            return field.getInt(entity);
                        case "Integer":
                            return (Integer) field.get(entity);
                        case "long":
                            return field.getLong(entity);
                        case "Long":
                            return (Long) field.get(entity);
                    }
                    throw new RuntimeException("id type must be int!");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    private void setIdValue(Object entity, int value) {
        Field idField = getIdField();
        assert idField != null;
        idField.setAccessible(true);
        try {
            idField.setInt(entity, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String getTableName() {
        return getEntityClass().getSimpleName();
    }

    private static void printSql(String sql) {
        if (printSql) {
            System.out.println();
            LogUtil.print(sql, "com.wangrg.java_lib.db2.BaseDao");
            System.out.println();
        }
    }

    private static void printResult(List list) {
        if (printResult) {
            System.out.println("\n----------- begin -----------");
            GsonUtil.printFormatJson(list);
            System.out.println("-----------  end  -----------\n");
        }
    }

    private static void printResult(long count) {
        if (printResult) {
            System.out.println("\n----------- begin -----------");
            GsonUtil.printFormatJson("count: " + count);
            System.out.println("-----------  end  -----------\n");
        }
    }

    protected synchronized boolean executeUpdate(String sql) {
        boolean succeed = false;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (ps.executeUpdate() > 0) {
                succeed = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close();
        }
        return succeed;
    }

    @Override
    public boolean createTable() {
        String sql = TableUtil.createTableSql(getEntityClass());
        printSql(sql);
        return executeUpdate(sql);
    }

    @Override
    public boolean dropTable() {
        String sql = TableUtil.dropTableSql(getEntityClass());
        printSql(sql);
        return executeUpdate(sql);
    }

    @Override
    public boolean createForeignKey() {
        boolean succeed = true;
        List<String> sqlList = TableUtil.foreignKeySql(getEntityClass());
        for (String sql : sqlList) {
            printSql(sql);
            if (!executeUpdate(sql)) {
                succeed = false;
            }
        }
        return succeed;
    }

    @Override
    public synchronized boolean insert(T entity) {
        boolean succeed = false;
        String tableName = getTableName();
        String columnStringList = "";// (userId,username,password)
        String valueList = "";// ('1','wang','123')
        for (Field field : getEntityClass().getDeclaredFields()) {
            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }
            if (field.getAnnotation(Id.class) != null &&
                    field.getAnnotation(GeneratedValue.class) != null) {
                continue;
            }

            field.setAccessible(true);
            try {
                if (field.getAnnotation(ManyToOne.class) != null) {
                    Object innerEntity = field.get(entity);
                    long idValue = getIdValue(innerEntity);
                    if (idValue > 0) {// 外键id值大于0才把外键id设置到sql语句中
                        columnStringList += "," + field.getName();
                        valueList += ",'" + idValue + "'";
                    }
                } else {
                    columnStringList += "," + field.getName();
                    String value = TableUtil.getValue(field, entity);
                    valueList += ",'" + formatter.format(value) + "'";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        columnStringList = columnStringList.substring(1);
        valueList = valueList.substring(1);

        String sql = "insert into " +
                tableName + " (" + columnStringList + ") values (" + valueList + ");";
        printSql(sql);
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                long l = (long) rs.getObject(1);
                int id = (int) l;
                setIdValue(entity, id);
            }
            succeed = true;
        } catch (SQLException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close();
        }
        return succeed;
    }

    @Override
    public synchronized boolean delete(Where where) {
        boolean succeed = false;
        String tableName = getTableName();
        String sql;
        if (where == null || where.size() == 0) {
            sql = "delete from " + tableName + ";";
        } else {
            sql = "delete from " + tableName + " where " + where + ";";
        }
        printSql(sql);
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            if (ps.executeUpdate() > 0) {
                succeed = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            succeed = false;
        } finally {
            close();
        }
        return succeed;
    }

    @Override
    public boolean deleteById(long id) {
        String idName = getIdField().getName();
        return delete(Where.eq(idName, id + ""));
    }

    @Override
    public boolean deleteAll() {
        return delete(null);
    }

    @Override
    public boolean update(T entity) {
        String tableName = getTableName();
        String setValueList = "";// (userId='1',username='wang',password='123')
        for (Field field : getEntityClass().getDeclaredFields()) {
            if (field.getAnnotation(Transient.class) != null) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {// don't need: && idAnno.autoIncrement()
                continue;
            }

            field.setAccessible(true);
            try {
                setValueList += "," + field.getName() + "=";
                if (field.getAnnotation(ManyToOne.class) != null) {
                    Object innerEntity = field.get(entity);
                    long idValue = getIdValue(innerEntity);
                    setValueList += "'" + idValue + "'";
                } else {
                    String value = TableUtil.getValue(field, entity);
                    setValueList += "'" + formatter.format(value) + "'";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        setValueList = setValueList.substring(1);

        String sql = "update " + tableName + " set " + setValueList + " where " +
                getIdField().getName() + "='" + getIdValue(entity) + "';";
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
    private static <T> List<T> executeQuery(String sql, Class<T> entityClass, Connection conn,
                                            int currentLevel, int maxQueryForeignKeyLevel,
                                            List<String> ignoreReferenceList,
                                            List<String> requiredReferenceVariableList) {
        if (currentLevel > maxQueryForeignKeyLevel) {
            return null;
        }

        List<T> entityList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                T entity = entityClass.newInstance();
                for (Field field : entityClass.getDeclaredFields()) {
                    if (field.getAnnotation(Transient.class) != null) {
                        continue;
                    }
                    field.setAccessible(true);
                    if (field.getAnnotation(ManyToOne.class) == null) {
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
                            TableUtil.setValue(field, entity, rs.getObject(field.getName()));
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
                        if (innerIdValue == null || Integer.parseInt(innerIdValue + "") <= 0) {
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
                            innerIdField.set(innerEntity, innerIdValue);
                            field.set(entity, innerEntity);
                        } else {// 否则查询外键对象所有属性的值
                            String innerSql = "select * from " + innerEntityClass.getSimpleName() +
                                    " where " + innerIdName + "='" + innerIdValue + "';";
                            List innerEntityList = executeQuery(innerSql, innerEntityClass, conn,
                                    currentLevel + 1, maxQueryForeignKeyLevel,
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
        printSql(sql);
        Connection connection = getConnection();
        List<T> list = executeQuery(sql, getEntityClass(), connection, 0,
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
    protected long executeQueryCount(String sql) {
        printSql(sql);
        long count = 0;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        printResult(count);
        return count;
    }

    @Override
    public List<T> query(Query query) {
        String sql;
        Where where = query.getWhere();
        if (where != null && where.size() > 0) {
            sql = "select * from " + getTableName() + " where " + where;
        } else {
            sql = "select * from " + getTableName();
        }

        String[] orderBy = query.getOrderBy();
        if (orderBy != null && orderBy.length > 0) {
            sql += " order by ";
            for (String s : orderBy) {
                if (TextUtil.isEmpty(s)) {//防止orderBy数组不为空，但元素为空的情况
                    continue;
                }
                if (s.startsWith("-")) {
                    sql += s.substring(1) + " desc,";
                } else {
                    sql += s + ",";
                }
            }
            sql = sql.substring(0, sql.length() - 1);//去掉最后多余的逗号
        }

        int offset = query.getOffset();
        int rowCount = query.getRowCount();
        if (rowCount > 0 && offset >= 0) {
            sql += " limit " + offset + "," + rowCount;
        }

        sql += ";";
        return executeQuery(sql, query.getMaxQueryForeignKeyLevel(),
                query.getIgnoreReferenceList(),
                query.getRequiredReferenceVariableList()
        );
    }

    @Override
    public List<T> query(Where where) {
        return query(Query.where(where));
    }

    @Override
    public T queryById(long id) {
        Where where = Where.eq(getIdField().getName(), id + "");
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
        String sql = "select count(*) from " + getTableName();
        sql += where == null || where.size() == 0 ? ";" : (" where " + where + ";");
        long count = executeQueryCount(sql);
        return (int) count;
    }

}
