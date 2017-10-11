package com.wangrg.java_lib.db3.db;

import com.wangrg.java_lib.db3.db.sql_creator.ISqlCreator;
import com.wangrg.java_lib.db3.db.sql_creator.OracleCreator;
import com.wangrg.java_lib.java_util.DateUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;

/**
 * by wangrongjun on 2017/8/21.
 */

public class OracleDatabase extends DefaultDatabase {

    private String dbName;// 实例名，通常是“orcl”

    public OracleDatabase(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String getDriverName() {
        return "oracle.jdbc.driver.OracleDriver";
    }

    @Override
    public String getUrl() {
        // 计算机名称,要是自己不知道可以在计算机属性查知
        // 系统实例名一般是默认orcl, 要是不是的话就用select name from v$database;看当前的实例名
        return "jdbc:oracle:thin:localhost:1521:" + dbName;
    }

    @Override
    public ISqlCreator getSqlCreator() {
        return new OracleCreator();
    }

    @Override
    public long insert(Connection conn, String tableName, String idFieldName, boolean autoIncrement, String sql)
            throws SQLException {
//        Statement stat = conn.createStatement();
//        stat.executeUpdate(sql);
//        stat.executeUpdate("commit");
//        int id = 0;
//        if (autoIncrement) {
//            ResultSet rs = stat.executeQuery(getSqlCreator().queryAutoIncrementCurrentValue(tableName));
//            if (rs.next()) {
//                id = rs.getInt(1);
//            }
//            rs.close();
//        }
//        stat.close();
//        return id;

        PreparedStatement ps = conn.prepareStatement(sql, new String[]{idFieldName});
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        int id = rs.getInt(1);
        rs.close();
        ps.executeUpdate("commit");
        ps.close();
        return id;
    }

    @Override
    public <T> String convertInsertOrUpdateValue(Field field, T entity) throws IllegalAccessException {
        Object value = field.get(entity);
        if (value == null) {
            return null;
        }
        switch (field.getType().getSimpleName()) {
            case "Date":
                String time = DateUtil.toDateTimeText((Date) field.get(entity));
                return "to_date('" + time + "','yyyy-MM-dd HH24:mi:ss')";
            case "String":
                return "'" + formatInputValue(field.get(entity).toString()) + "'";
            case "int":
            case "Integer":
            case "long":
            case "Long":
            case "float":
            case "Float":
            case "double":
            case "Double":
                return field.get(entity) + "";
            default:
                throw new RuntimeException("error: type of field " + field.getName() + " is wrong");
        }
    }

    @Override
    public void setRsValueToEntity(Field field, Object entity, Object value) throws IllegalAccessException {
        if (value == null) {
            field.set(entity, null);
            return;
        }
        switch (field.getType().getSimpleName()) {
            case "Date":
                value = new Date(((Timestamp) value).getTime());
                break;
            case "String":
                break;
            case "int":
            case "Integer":
                value = ((BigDecimal) value).toBigInteger().intValue();
                break;
            case "long":
            case "Long":
                value = ((BigDecimal) value).toBigInteger().longValue();
                break;
            case "float":
            case "Float":
                value = ((BigDecimal) value).toBigInteger().floatValue();
                break;
            case "double":
            case "Double":
                value = ((BigDecimal) value).toBigInteger().doubleValue();
                field.set(entity, value);
                break;
            default:
                throw new RuntimeException("error: type of field " + field.getName() + " is wrong");
        }
        field.set(entity, value);
    }

}
