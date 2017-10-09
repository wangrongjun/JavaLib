package com.wangrg.java_lib.db2;

import com.wangrg.java_lib.java_util.DateUtil;
import com.wangrg.java_lib.java_util.ReflectUtil;
import com.wangrg.java_lib.java_util.TextUtil;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * by wangrongjun on 2017/6/15.
 */

public class TableUtil {

    public static String createTableSql(Class entityClass) {
        String sql = "create table if not exists " + entityClass.getSimpleName() + " (\n";
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.getAnnotation(Ignore.class) != null) {
                continue;
            }
            sql += field.getName() + " " + getType(field);
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                if (idAnno.autoIncrement()) {
                    sql += " primary key auto_increment,\n";
                } else {
                    sql += " primary key,\n";
                }
                continue;
            }
            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                if (!columnAnno.nullable()) {
                    sql += " not null";
                }
                if (columnAnno.unique()) {
                    sql += " unique key";
                }
            }
            sql += ",\n";
        }
        sql = sql.substring(0, sql.length() - 2) + "\n) default charset=utf8;";
        return sql;
    }

    /**
     * String -> varchar(20)
     * User -> int/bigint
     */
    protected static String getType(Field field) {
        if (field.getAnnotation(Reference.class) != null) {
            Field innerIdField = ReflectUtil.findByAnno(field.getType(), Id.class);
            switch (innerIdField.getType().getSimpleName()) {
                case "int":
                case "Integer":
                    return "int";
                case "long":
                case "Long":
                    return "bigint";
            }
        }
        switch (field.getType().getSimpleName()) {
            case "int":
            case "Integer":
                return "int";
            case "long":
            case "Long":
                return "bigint";
            case "float":
            case "Float":
                return "float";
            case "double":
            case "Double":
                return "double";
            case "String":
                Column column = field.getAnnotation(Column.class);
                if (column == null || column.length() <= 0) {
                    return "text";
                }
                return "varchar(" + column.length() + ")";
            case "Date":
                return "datetime";
        }
        return null;
    }

    public static String dropTableSql(Class entityClass) {
        return "drop table if exists " + entityClass.getSimpleName() + ";";
    }

    public static List<String> foreignKeySql(Class entityClass) {
        List<String> list = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            Reference reference = field.getAnnotation(Reference.class);
            if (reference == null) {
                continue;
            }
            String mainTableName = entityClass.getSimpleName();
            String mainFieldName = field.getName();
            String referenceTableName = field.getType().getSimpleName();
            String referenceFieldName = ReflectUtil.findByAnno(field.getType(), Id.class).getName();
            list.add(foreignKeySql(mainTableName, mainFieldName, referenceTableName,
                    referenceFieldName, reference.onDeleteAction(), reference.onUpdateAction()));
        }
        return list;
    }

    public static String foreignKeySql(String mainTableName,
                                        String mainFieldName,
                                        String referenceTableName,
                                        String referenceFieldName,
                                        Reference.Action onDeleteAction,
                                        Reference.Action onUpdateAction) {

        String sql = "alter table " + mainTableName + " add foreign key (" + mainFieldName + ") " +
                "references " + referenceTableName + " (" + referenceFieldName + ") ";

        String onDeleteActionSql = "";
        String onUpdateActionSql = "";

        switch (onDeleteAction) {
            case NO_ACTION:
                onDeleteActionSql = "on delete no action";
                break;
            case SET_NULL:
                onDeleteActionSql = "on delete set null";
                break;
            case CASCADE:
                onDeleteActionSql = "on delete cascade";
                break;
        }

        switch (onUpdateAction) {
            case NO_ACTION:
                onUpdateActionSql = "on update no action";
                break;
            case SET_NULL:
                onUpdateActionSql = "on update set null";
                break;
            case CASCADE:
                onUpdateActionSql = "on update cascade";
                break;
        }

        if (!TextUtil.isEmpty(onDeleteActionSql)) {
            sql += onDeleteActionSql + " ";
        }
        if (!TextUtil.isEmpty(onUpdateActionSql)) {
            sql += onUpdateActionSql + " ";
        }

        sql = sql.substring(0, sql.length() - 1) + ";";
        return sql;
    }

    public static String getValue(Field field, Object entity) throws IllegalAccessException {
        if (field.getType().getName().equals("java.util.Date")) {
            return DateUtil.toDateTimeText((Date) field.get(entity));
        } else {
            return field.get(entity) + "";
        }
    }

    public static void setValue(Field field, Object entity, Object value)
            throws IllegalAccessException {
        if (field.getType().getName().equals("java.util.Date")) {
            try {
                Timestamp timestamp = (Timestamp) value;
                field.set(entity, new Date(timestamp.getTime()));
            } catch (DateUtil.DateTextSyntaxErrorException e) {
                e.printStackTrace();
            }
        } else {
            field.set(entity, value);
        }
    }

}
