package com.wangrj.java_lib.db3.db;

import com.wangrj.java_lib.data_structure.Pair;
import com.wangrj.java_lib.db.basis.Action;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.db3.anno.DefaultValue;
import com.wangrj.java_lib.db3.main.NameConverter;
import com.wangrj.java_lib.db3.main.TableField;
import com.wangrj.java_lib.db3.anno.UnionUniqueKey;
import com.wangrj.java_lib.db3.main.UpdateSetValue;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.JavaCodeParser;
import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import javax.persistence.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2017/8/23.
 */

public abstract class DefaultDatabase implements IDataBase {

    private boolean defineComment = true;// 是否获取源码的代码注释并定义到数据库中
    private NameConverter converter = new NameConverter.DefaultConverter();// 实体类属性名 -> 字段名 转换器

    @Override
    public List<String> createTableSql(Class entityClass) {
        String tableName = converter.toTableName(entityClass);

        Map<String, String> nameCommentMap = new HashMap<>();
        if (defineComment) {
            nameCommentMap = getComment(entityClass);
        }

        String tableComment = nameCommentMap.get(entityClass.getSimpleName());

        List<TableField> tableFieldList = new ArrayList<>();
        List<String> unionUniqueList = new ArrayList<>();

        for (Field field : entityClass.getDeclaredFields()) {
            if (shouldIgnoreField(field)) {
                continue;
            }

            String columnName = converter.toColumnName(field);

            TableField tableField = new TableField(columnName, getType(field));

            if (field.getAnnotation(Id.class) != null) {
                tableField.setPrimaryKey(true);
            }
            if (field.getAnnotation(GeneratedValue.class) != null) {
                tableField.setAutoIncrement(true);
            }

            Column columnAnno = field.getAnnotation(Column.class);
            if (columnAnno != null) {
                if (columnAnno.length() > 0) {
                    if (columnAnno.length() == 255) {// 255为@Column length()的默认值
                        tableField.setLength(0);
                    } else {
                        tableField.setLength(columnAnno.length());
                    }
                }
                tableField.setDecimalLength(columnAnno.precision());
                tableField.setNullable(columnAnno.nullable());
                tableField.setUnique(columnAnno.unique());
            }

            // 定义默认值
            DefaultValue defaultValueAnno = field.getAnnotation(DefaultValue.class);
            if (defaultValueAnno != null) {
                tableField.setDefaultValue(defaultValueAnno.value());
            }

            // 定义联合唯一键
            if (field.getAnnotation(UnionUniqueKey.class) != null) {
                unionUniqueList.add(columnName);
            }

            // 定义注释
            String comment = nameCommentMap.get(field.getName());
            if (comment != null) {
                tableField.setComment(comment);
            }

            if (isForeignKeyObject(field)) {

                tableField.setForeignKey(true);

                String referenceTableName = converter.toTableName(field.getType());
                tableField.setReferenceTable(referenceTableName);
                Field fkIdField = ReflectUtil.findByAnno(field.getType(), Id.class);
                if (fkIdField == null) {
                    throw new RuntimeException("reference object doesn't has id");
                }
                tableField.setType(getType(fkIdField));// 根据外键类的id类型重新设置数据类型
                tableField.setReferenceColumn(converter.toColumnName(fkIdField));
                CascadeType[] cascade;
                if (field.getAnnotation(OneToOne.class) != null) {
                    cascade = field.getAnnotation(OneToOne.class).cascade();
                    tableField.setUnique(true);// 由于是一对一，所以要唯一
                } else {
                    cascade = field.getAnnotation(ManyToOne.class).cascade();
                }
                if (cascade.length > 0) {
                    switch (cascade[0]) {
                        case ALL:
                            tableField.setOnDeleteAction(Action.CASCADE);
                            tableField.setOnUpdateAction(Action.CASCADE);
                            break;
                        case DETACH:
                        case REMOVE:
                            tableField.setOnDeleteAction(Action.SET_NULL);
                            tableField.setOnUpdateAction(Action.SET_NULL);
                            break;
                        default:
                            tableField.setOnDeleteAction(Action.NO_ACTION);
                            tableField.setOnUpdateAction(Action.NO_ACTION);
                    }
                }
            }

            tableFieldList.add(tableField);
        }

        return getSqlCreator().createTableSql(tableName, tableComment, tableFieldList, unionUniqueList);
    }

    @Override
    public List<String> dropTableSql(Class entityClass) {
        String tableName = converter.toTableName(entityClass);
        return getSqlCreator().dropTableSql(tableName);
    }

    @Override
    public String insertSql(Object entity) {
        List<Pair<String, String>> nameValuePairList = new ArrayList<>();

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (shouldIgnoreField(field)) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                long idValue = getIdValue(entity);
                if (idValue > 0) {
                    nameValuePairList.add(new Pair<>(field.getName(), idValue + ""));
                }
                continue;
            }

            field.setAccessible(true);
            try {
                if (isForeignKeyObject(field)) {
                    Object innerEntity = field.get(entity);
                    long idValue = getIdValue(innerEntity);
                    if (idValue > 0) {// 外键id值大于0才把外键id设置到sql语句中
                        nameValuePairList.add(new Pair<>(field.getName(), idValue + ""));
                    }
                } else {
                    String value = convertInsertOrUpdateValue(field, entity);
                    if (!TextUtil.isEmpty(value)) {
                        nameValuePairList.add(new Pair<>(field.getName(), value));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return getSqlCreator().insertSql(entity.getClass().getSimpleName(), nameValuePairList);
    }

    @Override
    public String deleteSql(Object entity) {
        Where where = new Where();
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (shouldIgnoreField(field)) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {
                field.setAccessible(true);
                where.equal(field.getName(), getIdValue(entity) + "");
            }
        }
        return getSqlCreator().deleteSql(entity.getClass().getSimpleName(), where);
    }

    @Override
    public String updateSql(Object entity) {
        UpdateSetValue setValue = new UpdateSetValue();
        Where where = new Where();

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (shouldIgnoreField(field)) {
                continue;
            }
            Id idAnno = field.getAnnotation(Id.class);
            if (idAnno != null) {// don't need: && idAnno.autoIncrement()
                where.equal(field.getName(), getIdValue(entity) + "");
                continue;
            }
            field.setAccessible(true);
            try {
                if (isForeignKeyObject(field)) {
                    Object innerEntity = field.get(entity);
                    long fkIdValue = getIdValue(innerEntity);
                    if (fkIdValue == 0) {// 表示需要把外键设置为空
                        setValue.add(field.getName(), null);
                    } else {
                        setValue.add(field.getName(), fkIdValue + "");
                    }
                } else {
                    String value = convertInsertOrUpdateValue(field, entity);
                    setValue.add(field.getName(), value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return getSqlCreator().updateSql(entity.getClass().getSimpleName(), setValue, where);
    }

    private boolean shouldIgnoreField(Field field) {
        return field.getAnnotation(Transient.class) != null ||
                field.getAnnotation(OneToMany.class) != null ||
                field.getAnnotation(ManyToMany.class) != null;
    }

    private boolean isForeignKeyObject(Field field) {
        return field.getAnnotation(ManyToOne.class) != null ||
                field.getAnnotation(OneToOne.class) != null;
    }

    protected TableField.Type getType(Field field) {
        if (isForeignKeyObject(field)) {
            return null;
        }

        switch (field.getType().getSimpleName()) {
            case "int":
            case "Integer":
                return TableField.Type.NUMBER_INT;
            case "long":
            case "Long":
                return TableField.Type.NUMBER_LONG;
            case "float":
            case "Float":
                return TableField.Type.NUMBER_FLOAT;
            case "double":
            case "Double":
                return TableField.Type.NUMBER_DOUBLE;
            case "boolean":
            case "Boolean":
                return TableField.Type.BOOLEAN;
            case "String":
                return TableField.Type.TEXT;
            case "Date":
                return TableField.Type.DATE;
            default:
                throw new RuntimeException("field '" + field.getName() + "' has error type: " +
                        field.getType().getSimpleName());
        }
    }

    /**
     * @return entity对象或主键对象为空，返回0。主键对象不为空且不存在@Id变量，返回-1。
     * Id变量不为空，返回主键对象值
     */
    protected long getIdValue(Object entity) {
        if (entity == null) {
            return 0;
        }
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null) {
                field.setAccessible(true);
                try {
                    if (field.get(entity) == null) {
                        return 0;
                    }
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

    /**
     * 尝试从各种途径获取代码注释
     *
     * @return 键是实体类名和属性名，值是注释
     */
    protected Map<String, String> getComment(Class entityClass) {
        String classPath = entityClass.getName().replace(".", "/");
        String[] possiblePaths = new String[]{// 所有可能的路径
                "src/" + classPath + ".java",
                "src/main/java/" + classPath + ".java"
        };
        for (String path : possiblePaths) {
            File javaFile = new File(path);
            if (javaFile.exists()) {
                String javaCode = FileUtil.read(javaFile);
                return JavaCodeParser.getDocument(entityClass, javaCode);
            }
        }
        return new HashMap<>();
    }

    /**
     * 防止输入的值中含有非法字符如单引号，斜杠等。防止sql注入攻击。
     * 原理：单引号中间的内容有可能引起歧义的只有内容里的单引号和斜杠。
     * 只要对内容里的单引号和斜杠进行转义，理论上可以防止任意方式的sql注入攻击。
     */
    protected String formatInputValue(String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }

    public DefaultDatabase setNameConverter(NameConverter converter) {
        this.converter = converter;
        return this;
    }

    public void setDefineComment(boolean defineComment) {
        this.defineComment = defineComment;
    }
}
