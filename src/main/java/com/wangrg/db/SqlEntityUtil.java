package com.wangrg.db;

import com.wangrg.db.basis.Constraint;
import com.wangrg.db.basis.ConstraintAnno;
import com.wangrg.db.basis.DbSpecialCharacterChanger;
import com.wangrg.db.basis.DbType;
import com.wangrg.db.basis.FieldType;
import com.wangrg.db.basis.TableField;
import com.wangrg.db.basis.TableValue;
import com.wangrg.db.basis.TypeAnno;
import com.wangrg.db.basis.ValueType;
import com.wangrg.db.basis.Where;
import com.wangrg.db.exception.FieldNotFoundException;
import com.wangrg.db.exception.PrimaryKeyNotFoundException;
import com.wangrg.java_util.TextUtil;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * by wangrongjun on 2017/1/23.
 */
public class SqlEntityUtil {

    public static DbType dbType = DbType.MYSQL;
    public static DbSpecialCharacterChanger changer = new DbSpecialCharacterChanger();

    public static String createTableSql(Class entityClass) {
        List<TableField> tableFields = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno == null) {
                continue;
            }
            FieldType fieldType = typeAnno.type();
            TableField tableField = new TableField(field.getName(), fieldType);

            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null) {
                Constraint constraint = constraintAnno.constraint();
                switch (constraint) {
                    case NULL:
                        break;
                    case PRIMARY_KEY:
                        tableField.primaryKey();
                        break;
                    case UNIQUE:
                        tableField.unique();
                        break;
                    case NOT_NULL:
                        tableField.notNull();
                        break;
                    case UNIQUE_NOT_NULL:
                        tableField.notNull().unique();
                        break;
                    case UNSIGNED:
                        tableField.unsigned();
                    case DEFAULT:
                        //先对特殊字符进行转义，至于之后是否需要还原，看情况（比如\\就不用）
                        String value = changer.encode(constraintAnno.defaultValue());
                        tableField.defaultValue(new TableValue(TableValue.toValueType(fieldType), value));
                        break;
                    case FOREIGN_KEY:
                        break;
                }
            }

            tableFields.add(tableField);

        }

        return SqlUtil.createTableSql(entityClass.getSimpleName(), tableFields);
    }

    /**
     * 创建某个表的所有外键sql列表（最好先创建好所有表）
     */
    public static List<String> createReferenceSqlList(Class entityClass) {

        List<String> sqlList = new ArrayList<>();

        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (typeAnno == null || constraintAnno == null) {
                continue;
            }
            if (constraintAnno.constraint() == Constraint.FOREIGN_KEY) {
                String foreignTable = constraintAnno.foreignTable();
                String foreignField = constraintAnno.foreignField();
                String sql = SqlUtil.foreignKeySql(
                        entityClass.getSimpleName(),
                        field.getName(),
                        foreignTable,
                        foreignField,
                        constraintAnno.onDeleteAction(),
                        constraintAnno.onUpdateAction()
                );
                sqlList.add(sql);
            }
        }

        return sqlList;
    }

    public static String insertSql(Object entity) {

        Field[] fields = entity.getClass().getDeclaredFields();
        List<TableValue> values = new ArrayList<>();

        for (Field field : fields) {

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno == null) {
                continue;
            }
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                continue;//插入时没必要设置主键
            }

            ValueType valueType = TableValue.toValueType(typeAnno.type());
            String value = "";
            field.setAccessible(true);
            try {
                value = field.get(entity) + "";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            values.add(new TableValue(field.getName(), valueType, changer.encode(value)));
        }

        return SqlUtil.insertSql(entity.getClass().getSimpleName(), values);
    }

    public static String updateByIdSql(Object entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        Where where = new Where();
        List<TableValue> setValues = new ArrayList<>();

        for (Field field : fields) {

            TypeAnno typeAnno = field.getAnnotation(TypeAnno.class);
            if (typeAnno == null) {
                continue;
            }
            ValueType valueType = TableValue.toValueType(typeAnno.type());

            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                String whereValue = "";
                field.setAccessible(true);
                try {
                    whereValue = field.get(entity) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                where.add(field.getName(), changer.encode(whereValue), valueType);

            } else {
                String setValue = "";
                field.setAccessible(true);
                try {
                    setValue = field.get(entity) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                setValues.add(new TableValue(field.getName(), valueType, changer.encode(setValue)));
            }

        }

        return SqlUtil.updateSql(entity.getClass().getSimpleName(), setValues, where);
    }

    public static String updateSql(Class entityClass, String id, String setName, String setValue)
            throws PrimaryKeyNotFoundException, FieldNotFoundException {

//        1.获取主键的名称和类型
        Field[] fields = entityClass.getDeclaredFields();
        String idName = null;
        ValueType idValueType = null;
        for (Field field : fields) {
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                idName = field.getName();
                idValueType = TableValue.toValueType(field.getAnnotation(TypeAnno.class).type());
                break;
            }
        }
        if (TextUtil.isEmpty(idName) || idValueType == null) {
            throw new PrimaryKeyNotFoundException();
        }

//        2.获取修改字段的类型
        ValueType setValueType;
        try {
            Field field = entityClass.getDeclaredField(setName);
            setValueType = TableValue.toValueType(field.getAnnotation(TypeAnno.class).type());
        } catch (Exception e) {
            throw new FieldNotFoundException(setName);
        }

//        3.生成并返回sql语句
        TableValue tableValue = new TableValue(setName, setValueType, changer.encode(setValue));
        Where where = new Where().add(idName, changer.encode(id), idValueType);
        return SqlUtil.updateSql(entityClass.getSimpleName(), Collections.singletonList(tableValue),
                where);
    }

    public static String deleteByIdSql(Class entityClass, String id) throws PrimaryKeyNotFoundException {
//        1.获取主键的名称和类型
        Field[] fields = entityClass.getDeclaredFields();
        String idName = null;
        ValueType idValueType = null;
        for (Field field : fields) {
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                idName = field.getName();
                idValueType = TableValue.toValueType(field.getAnnotation(TypeAnno.class).type());
                break;
            }
        }
        if (TextUtil.isEmpty(idName) || idValueType == null) {
            throw new PrimaryKeyNotFoundException();
        }

//        2.生成并返回sql语句
        Where where = new Where().add(idName, changer.encode(id), idValueType);
        return SqlUtil.deleteSql(entityClass.getSimpleName(), where);
    }

    public static String deleteSql(Class entityClass, String whereName, String whereValue)
            throws FieldNotFoundException {
//        1.获取作为查询条件的字段的类型
        ValueType whereValueType;
        try {
            Field field = entityClass.getDeclaredField(whereName);
            whereValueType = TableValue.toValueType(field.getAnnotation(TypeAnno.class).type());
        } catch (Exception e) {
            throw new FieldNotFoundException(whereName);
        }

        Where where = new Where().add(whereName, changer.encode(whereValue), whereValueType);
        return SqlUtil.deleteSql(entityClass.getSimpleName(), where);
    }

    public static String deleteAllSql(Class entityClass) {
        return SqlUtil.deleteSql(entityClass.getSimpleName(), null);
    }

    public static String queryByIdSql(Class entityClass, String id)
            throws PrimaryKeyNotFoundException {
        String primaryKeyName = null;
        ValueType primaryKeyValueType = null;
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            ConstraintAnno constraintAnno = field.getAnnotation(ConstraintAnno.class);
            if (constraintAnno != null && constraintAnno.constraint() == Constraint.PRIMARY_KEY) {
                primaryKeyName = field.getName();
                primaryKeyValueType =
                        TableValue.toValueType(field.getAnnotation(TypeAnno.class).type());
            }
        }
        if (TextUtil.isEmpty(primaryKeyName)) {
            throw new PrimaryKeyNotFoundException();
        }

        Where where = new Where().add(primaryKeyName, changer.encode(id), primaryKeyValueType);
        return SqlUtil.querySql(entityClass.getSimpleName(), where);
    }

    public static String queryAllSql(Class entityClass) throws SQLException {
        return SqlUtil.querySql(entityClass.getSimpleName(), null);
    }

    public static String querySql(Class entityClass, String whereName, String whereValue)
            throws FieldNotFoundException {
        ValueType whereValueType;
        try {
            Field field = entityClass.getDeclaredField(whereName);
            whereValueType = TableValue.toValueType(field.getAnnotation(TypeAnno.class).type());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FieldNotFoundException(whereName);
        }

        Where where = new Where().add(whereName, changer.encode(whereValue), whereValueType);
        return SqlUtil.querySql(entityClass.getSimpleName(), where);
    }

    public static String querySql(Class entityClass, String whereName1, String whereValue1,
                                  String whereName2, String whereValue2)
            throws FieldNotFoundException {
        ValueType whereValueType1;
        ValueType whereValueType2;
        try {
            Field field1 = entityClass.getDeclaredField(whereName1);
            whereValueType1 = TableValue.toValueType(field1.getAnnotation(TypeAnno.class).type());
            Field field2 = entityClass.getDeclaredField(whereName2);
            whereValueType2 = TableValue.toValueType(field2.getAnnotation(TypeAnno.class).type());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FieldNotFoundException(whereName1 + " or " + whereName2);
        }

        Where where = new Where().
                add(whereName1, changer.encode(whereValue1), whereValueType1).
                add(whereName2, changer.encode(whereValue2), whereValueType2);
        return SqlUtil.querySql(entityClass.getSimpleName(), where);
    }

}
