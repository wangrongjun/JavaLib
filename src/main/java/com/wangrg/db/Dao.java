package com.wangrg.db;

import com.wangrg.db.basis.DbType;
import com.wangrg.db.basis.TypeAnno;
import com.wangrg.java_util.DebugUtil;
import com.wangrg.db.connection.DbHelper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 拥有已经实现好的万能增删查改等方法的类
 */
public abstract class Dao<T> {

    protected DbHelper dbHelper;

    public Dao(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
        SqlUtil.dbType = dbHelper.getDbType();
        SqlEntityUtil.dbType = dbHelper.getDbType();
    }

    protected abstract Class<T> getEntityClass();

    protected abstract boolean isPrintSql();

    /**
     * 根据entityClass创建数据表，字段属性为可空。只支持int，double，String三种数据类型，
     * 在数据表中分别对应int，double,text。
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.创建的数据表名将会与entity类名相同。
     * <p/>
     * 3.创建的数据表所有字段将会与entity类对应成员变量名相同。
     */
    public void createTable(Class entityClass) throws SQLException {
        String createTableSql = SqlEntityUtil.createTableSql(entityClass);
        printSql(createTableSql);
        execute(createTableSql);
    }

    public void createTable() throws SQLException {
        createTable(getEntityClass());
    }

    /**
     * 创建某个表的外键（最好先创建好所有表）
     */
    public void createReferences(Class entityClass) throws SQLException {
        List<String> referenceSqlList = SqlEntityUtil.createReferenceSqlList(entityClass);
        for (String sql : referenceSqlList) {
            printSql(sql);
            execute(sql);
        }
    }

    public void createReferences() throws SQLException {
        createReferences(getEntityClass());
    }

    /**
     * 根据主键查询表中的一个entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     */
    public T queryById(String id) throws SQLException {
        Class<T> entityClass = getEntityClass();
        String sql = SqlEntityUtil.queryByIdSql(entityClass, id);
        printSql(sql);
        List<T> entityList = executeQuery(entityClass, sql);
        if (entityList.size() == 0) {
            try {
                return entityClass.newInstance();
            } catch (Exception e) {
                throw new SQLException(e.toString());
            }
        } else {
            return entityList.get(0);
        }
    }

    /**
     * 根据指定的一个条件查询多个entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     *
     * @param whereName  查询条件的字段名称
     * @param whereValue 查询条件的字段值，可以为整型，浮点型，字符串等
     */
    public List<T> query(String whereName, String whereValue)
            throws SQLException {
        Class<T> entityClass = getEntityClass();
        String sql = SqlEntityUtil.querySql(entityClass, whereName, whereValue);
        printSql(sql);
        return executeQuery(entityClass, sql);
    }

    /**
     * 查询表中所有entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     */
    public List<T> queryAll() throws SQLException {
        Class<T> entityClass = getEntityClass();
        String sql = SqlEntityUtil.queryAllSql(entityClass);
        printSql(sql);
        return executeQuery(entityClass, sql);
    }

    /**
     * 根据主键插入一个entity
     * <p/>
     * 使用前提：
     * <p/>
     * 1.已配置好dbHelper。
     * <p/>
     * 2.数据表名与entity类名相同。
     * <p/>
     * 3.数据表所有字段与entity类对应成员变量名相同。
     * <p/>
     * 4.entity类第一个成员变量作为自增主键。
     *
     * @return 返回插入后自增的主键id
     */
    public synchronized int insert(T entity) throws SQLException {

        String sql = SqlEntityUtil.insertSql(entity);
        printSql(sql);

        if (dbHelper.getDbType() == DbType.SQLITE) {
            PreparedStatement ps = dbHelper.getConnection().prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            dbHelper.close();
            return 0;
        } else {
            return executeInsert(sql);
        }

    }

    /**
     * 根据主键找到相应的记录并修改所有非主键z字段的值
     *
     * @return 成功修改的记录数目
     */
    public synchronized int updateById(T entity) throws SQLException {
        String sql = SqlEntityUtil.updateByIdSql(entity);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 依据id作为where条件修改某一个字段的值
     *
     * @return 成功修改的记录数目
     */
    public synchronized int update(String id, String setName, String setValue)
            throws SQLException {
        Class<T> entityClass = getEntityClass();
        String sql = SqlEntityUtil.updateSql(entityClass, id, setName, setValue);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 依据id作为where条件删除某一条记录
     *
     * @return 成功删除的记录数目
     */
    public synchronized int deleteById(String id) throws SQLException {
        Class<T> entityClass = getEntityClass();
        String sql = SqlEntityUtil.deleteByIdSql(entityClass, id);
        printSql(sql);
        return executeUpdate(sql);
    }

    /**
     * 删除某一条记录
     *
     * @return 成功删除的记录数目
     */
    public synchronized int delete(String whereName, String whereValue)
            throws SQLException {
        Class<T> entityClass = getEntityClass();
        String sql = SqlEntityUtil.deleteSql(entityClass, whereName, whereValue);
        printSql(sql);
        return executeUpdate(sql);
    }

    public void execute(String sql) throws SQLException {
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        ps.close();
        dbHelper.close();
    }

    public List<T> executeQuery(Class<T> entityClass, String sql) throws SQLException {
        PreparedStatement ps = dbHelper.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        List<T> entityList = getResult(entityClass, rs);
        rs.close();
        ps.close();
        dbHelper.close();
        return entityList;
    }

    public static <T> List<T> getResult(Class<T> entityClass, ResultSet rs) throws SQLException {

        List<T> entityList = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();

        while (rs.next()) {
            T entity;
            try {
                entity = entityClass.newInstance();
            } catch (Exception e) {
                throw new SQLException(e.toString());
            }
            for (Field field : fields) {
                if (field.getAnnotation(TypeAnno.class) == null) {
                    continue;
                }
                field.setAccessible(true);
                Object object = rs.getObject(field.getName());
                try {
                    field.set(entity, object);
                } catch (IllegalAccessException e) {
                    throw new SQLException(e.toString());
                }
            }
            entityList.add(entity);
        }
        return entityList;
    }

    /**
     * 获取select count(*)的结果
     */
    public static int getCount(ResultSet rs) throws SQLException {
        if (rs == null) {
            throw new SQLException("ResultSet is null");
        }
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("count not exists");
        }
    }

    /**
     * 执行插入的sql语句
     *
     * @return 返回自增主键的id值
     */
    public int executeInsert(String sql) throws SQLException {
        PreparedStatement ps = dbHelper.getConnection().prepareStatement(sql);
        ps.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            long i = (long) rs.getObject(1);
            rs.close();
            ps.close();
            dbHelper.close();
            return (int) i;
        } else {
            rs.close();
            ps.close();
            dbHelper.close();
            throw new SQLException(sql + "\nfailed to get id after insert");
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        Connection conn = dbHelper.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        int count = ps.executeUpdate();
        ps.close();
        dbHelper.close();
        return count;
    }

    public Connection getConnection() throws SQLException {
        return dbHelper.getConnection();
    }

    private void printSql(String sql) {
        if (isPrintSql()) {
            System.out.println(DebugUtil.getDebugMessage(sql + "\n", 2));
        }
    }

}
