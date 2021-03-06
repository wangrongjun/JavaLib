package com.wangrj.java_lib.java_util.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * by robin.wang on 2018/8/26.
 * <p>
 * 实体数组与Object数组之间的相互转换
 */
public class EntityConverter {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface EntityConverterIgnore {
    }

    /**
     * 设置valueList每一列对应的Class Field。如果不设置，则按照 cls.getDeclaredFields() 的顺序赋值
     */
    private List<String> fieldNameList;
    /**
     * 设置日期类型数据在Excel的显示格式，默认是 yyyy-MM-dd HH:mm:ss
     */
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * 把实体数组转换为二维值数组。二维值数组的一行代表一个实体，一列代表实体的一个属性。
     *
     * @return 值类型有且仅有以下类型：String, boolean, double, Date
     */
    public List<List<Object>> entityListToValueLists(List entityList) {
        if (entityList == null || entityList.size() == 0) {
            return new ArrayList<>();
        }

        List<List<Object>> valueLists = new ArrayList<>();
        Class entityClass = entityList.get(0).getClass();
        List<Field> fieldList = getFieldList(entityClass);

        for (Object entity : entityList) {
            List<Object> valueList = new ArrayList<>();
            for (Field field : fieldList) {
                if (field == null) {
                    valueList.add(null);
                    continue;
                } else if (field.getAnnotation(EntityConverterIgnore.class) != null) {
                    continue;
                }
                field.setAccessible(true);
                Object value = getValueFromEntity(entity, field);
                valueList.add(value);
            }
            valueLists.add(valueList);
        }

        return valueLists;
    }

    /**
     * 把二维值数组转换为实体数组。二维值数组的一行代表一个实体，一列代表实体的一个属性。
     *
     * @return 值类型仅支持以下类型：String, boolean, double, Date
     */
    public <T> List<T> valueListsToEntityList(List<List<Object>> valueLists, Class<T> entityClass) {
        List<T> entityList = new ArrayList<>();
        List<Field> fieldList = getFieldList(entityClass);

        for (List<Object> valueList : valueLists) {
            T entity;
            try {
                entity = entityClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Default Constructor not found at class: " + entityClass.getName(), e);
            }

            for (int i = 0; i < valueList.size(); i++) {
                // 如果entityClass的fields不够list的列多，list多出来的列就忽略。
                // 比如每个list都有a,b,c三列，而entity只有a,b两个属性。那list的c列对应的值就会被忽略。
                if (i >= fieldList.size()) {
                    break;
                }
                Object value = valueList.get(i);
                if (value == null) {
                    continue;
                }
                Field field = fieldList.get(i);
                if (field == null || field.getAnnotation(EntityConverterIgnore.class) != null) {
                    continue;
                }
                field.setAccessible(true);
                setValueToEntity(entity, field, value);
            }
            entityList.add(entity);
        }

        return entityList;
    }

    private List<Field> getFieldList(Class entityClass) {
        List<Field> fieldList;
        if (fieldNameList != null) {// 按照 fieldNameList 的顺序赋值
            fieldList = new ArrayList<>();
            for (String fieldName : fieldNameList) {
                if (fieldName == null) {
                    fieldList.add(null);// 兼容 valueList 的某一列没有 Class Field 与之对应的情况。
                } else {
                    try {
                        fieldList.add(entityClass.getDeclaredField(fieldName));
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else {// 按照 cls.getDeclaredFields() 的顺序赋值
            fieldList = Arrays.asList(entityClass.getDeclaredFields());
        }
        return fieldList;
    }

    private void setValueToEntity(Object entity, Field field, Object value) {
        try {
            switch (field.getType().getSimpleName()) {
                case "int":
                case "Integer":
                    // 假如 value="1.0"，那么Integer.parseInt("1.0")就会出错，所以使用Double来解析，从而避免错误
                    field.set(entity, (int) Double.parseDouble(String.valueOf(value)));
                    break;
                case "long":
                case "Long":
                    field.set(entity, (long) Double.parseDouble(String.valueOf(value)));
                    break;
                case "float":
                case "Float":
                    field.set(entity, (float) Double.parseDouble(String.valueOf(value)));
                    break;
                case "double":
                case "Double":
                    field.set(entity, Double.parseDouble(String.valueOf(value)));
                    break;
                case "boolean":
                case "Boolean":
                    field.set(entity, Boolean.parseBoolean(String.valueOf(value)));
                    break;
                case "String":
                    field.set(entity, String.valueOf(value));// String.valueOf : 避免value不是字符串而报错
                    break;
                case "Date":
                    switch (value.getClass().getSimpleName()) {
                        case "Date":
                            field.set(entity, value);
                            break;
                        case "String":
                            field.set(entity, new SimpleDateFormat(dateFormat).parse(String.valueOf(value)));
                            break;
                        case "double":
                            field.set(entity, new Date((long) Double.parseDouble(String.valueOf(value))));
                            break;
                    }
                    break;
                case "LocalDate":
                    switch (value.getClass().getSimpleName()) {
                        case "Date":
                            LocalDate localDate = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            field.set(entity, localDate);
                            break;
                        case "String":
                            field.set(entity, LocalDate.parse(String.valueOf(value), DateTimeFormatter.ofPattern(dateFormat)));
                            break;
                        case "double":
                            Date date = new Date((long) Double.parseDouble(String.valueOf(value)));
                            field.set(entity, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                            break;
                    }
                    break;
                case "LocalDateTime":
                    switch (value.getClass().getSimpleName()) {
                        case "Date":
                            LocalDateTime localDateTime = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            field.set(entity, localDateTime);
                            break;
                        case "String":
                            field.set(entity, LocalDateTime.parse(String.valueOf(value), DateTimeFormatter.ofPattern(dateFormat)));
                            break;
                        case "double":
                            Date date = new Date((long) Double.parseDouble(String.valueOf(value)));
                            field.set(entity, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                            break;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("can not resolve type: " +
                            field.getType().getName() + " - " + field.getName());
            }
        } catch (IllegalArgumentException | IllegalAccessException | ParseException e) {
            throw new RuntimeException("Failed to set value to field: " + field.getName(), e);
        }
    }

    /**
     * @return 如果 field 是 double,boolean 类型，就返回对应的类型。
     * 如果是 Date,LocalDate,LocalDateTime，统一返回Date类型。
     * 其余情况返回String类型（toString得到的值）。
     */
    private Object getValueFromEntity(Object entity, Field field) {
        try {
            Object value = field.get(entity);
            if (value == null) {
                return null;
            }
            switch (field.getType().getSimpleName()) {
                case "double":
                case "Double":
                case "boolean":
                case "Boolean":
                case "Date":
                    return value;
                case "Timestamp":
                    return new Date(((Timestamp) value).getTime());
                case "LocalDate":
                    Instant instant = ((LocalDate) value).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                    return Date.from(instant);
                case "LocalDateTime":
                    instant = ((LocalDateTime) value).atZone(ZoneId.systemDefault()).toInstant();
                    return Date.from(instant);
                default:
                    return String.valueOf(value);
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get value from field: " + field.getName(), e);
        }
    }

    public void setFieldNameList(List<String> fieldNameList) {
        this.fieldNameList = fieldNameList;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
