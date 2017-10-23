package com.wangrg.java_lib.db3.db;

import com.wangrg.java_lib.db3.db.sql_creator.ISqlCreator;
import com.wangrg.java_lib.db3.db.sql_creator.MysqlCreator;
import com.wangrg.java_lib.java_util.DateUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Date;

/**
 * by wangrongjun on 2017/8/21.
 */

public class MysqlDatabase extends DefaultDatabase {

    private String dbName;

    public MysqlDatabase(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public String getDriverName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    public String getUrl() {
        return "jdbc:mysql://localhost:3306/" + dbName + "?" +
                "useUnicode=true&" +
                "characterEncoding=utf-8&" +
                "useSSL=false";// 为了不显示烦人的安全警告
    }

    @Override
    public ISqlCreator getSqlCreator() {
        return new MysqlCreator();
    }

    @Override
    public long insert(Connection conn, String tableName, String idFieldName, boolean autoIncrement, String sql)
            throws SQLException {
//        Statement stat = conn.createStatement();
//        stat.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//        int id = 0;
//        if (autoIncrement) {
//            ResultSet rs = stat.getGeneratedKeys();
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
                return "'" + DateUtil.toDateTimeText((Date) field.get(entity)) + "'";
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
        field.set(entity, value);
//        if (value == null) {
//            field.set(entity, null);
//            return;
//        }
//        if (field.getType().getName().equals("java.util.Date")) {
//            Timestamp timestamp = (Timestamp) value;
//            field.set(entity, new Date(timestamp.getTime()));
//        } else {
//            field.set(entity, value);
//        }
    }

}
