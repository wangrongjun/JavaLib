package com.wangrj.java_lib.mybatis.mybatis_generator.v2;

import com.wangrj.java_lib.constant.JavaLibConstant;
import com.wangrj.java_lib.db3.main.NameConverter;
import com.wangrj.java_lib.java_util.FreeMakerUtil;
import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.dao.JobDao;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.dao.UserInfoDao;
import freemarker.template.TemplateException;
import org.junit.Test;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2017/11/4.
 */
public class MybatisCreator {

    private static final String daoFltFilePath = JavaLibConstant.srcDir() +
            "com/wangrj/java_lib/mybatis/mybatis_generator/v2/templates/dao.ftl";
    private static final String mapperFltFilePath = JavaLibConstant.srcDir() +
            "com/wangrj/java_lib/mybatis/mybatis_generator/v2/templates/mapper.ftl";

    /**
     * dao是否生成 @Repository 和 @MapperScan 注解
     */
    private boolean springAnno = false;
    /**
     * 数据库种类，如Oracle，MySQL
     */
    private String dbType = "Oracle";
    /**
     * POJO类属性名 - 数据表字段名转换器
     */
    private NameConverter converter = new NameConverter.DefaultConverter();

    @Test
    public void testCreate() throws IOException, TemplateException {
        MybatisCreator creator = new MybatisCreator().
                setNameConverter(new NameConverter.HumpToUnderlineConverter());

        creator.createDao(Job.class, JobDao.class.getName(), new OutputStreamWriter(System.out));
        System.out.println("\n\n-----------------------------------------------------------------\n\n");
        creator.createMapper(Job.class, JobDao.class.getName(), new OutputStreamWriter(System.out));
        System.out.println("\n\n-----------------------------------------------------------------\n\n");
        creator.createDao(UserInfo.class, UserInfoDao.class.getName(), new OutputStreamWriter(System.out));
        System.out.println("\n\n-----------------------------------------------------------------\n\n");
        creator.createMapper(UserInfo.class, UserInfoDao.class.getName(), new OutputStreamWriter(System.out));
    }

    public void createDao(Class pojoClass, String daoName, Writer writer) throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();

        String simpleName = pojoClass.getSimpleName();
        dataModel.put("daoPackage", TextUtil.getTextExceptLastPoint(daoName));// 例如：com.dao
        dataModel.put("daoSimpleName", TextUtil.getTextAfterLastPoint(daoName));// 例如：UserInfoDao
        dataModel.put("pojoName", pojoClass.getName());// 例如：com.bean.UserInfo
        dataModel.put("pojoSimpleName", simpleName);// 例如：UserInfo
        dataModel.put("tableName", converter.toTableName(pojoClass));// 例如：UserInfo 或 user_info
        String varName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1, simpleName.length());
        dataModel.put("pojoVarName", varName);// 例如：userInfo
        dataModel.put("springAnno", springAnno);

        Field idField = ReflectUtil.findByAnno(pojoClass, Id.class);
        assert idField != null;
        dataModel.put("pojoIdType", idField.getType().getSimpleName());// 例如：Integer
        dataModel.put("pojoIdName", idField.getName());// 例如：userId
        dataModel.put("pojoIdColumnName", converter.toColumnName(idField));// 例如：userId 或 user_id

        // 插入数据操作返回id的注解。0：@SelectKey。1：@Options。
        dataModel.put("insertReturnKeyAnno", dbType.equalsIgnoreCase("Oracle") ? 0 : 1);

        boolean haveFk = false;// 是否有外键，这决定了查询结果映射注解是@ResultType还是@ResultMap

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (Field field : pojoClass.getDeclaredFields()) {

            if (field.getAnnotation(Transient.class) != null) {// 如果需要忽略
                continue;
            }

            String fieldName = field.getName();
            String getter = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());

            String columnName = fieldName;
            if (converter != null) {
                columnName = converter.toColumnName(field);
            }
            FieldInfo fieldInfo = new FieldInfo(fieldName, getter, columnName);

            if (field.getAnnotation(Id.class) != null) {// 如果是主键
                // 主键是否自增，决定插入语句是否需要主键赋值的set子句
                dataModel.put("autoIncrement", field.getAnnotation(GeneratedValue.class) != null);
                fieldInfo.setType(1);

            } else if (field.getAnnotation(ManyToOne.class) != null ||
                    field.getAnnotation(OneToOne.class) != null) {// 如果是外键对象
                haveFk = true;
                fieldInfo.setType(2);
                Field fkIdField = ReflectUtil.findByAnno(field.getType(), Id.class);
                assert fkIdField != null;
                fieldInfo.setFkClassName(field.getType().getName());
                fieldInfo.setFkTableName(converter.toTableName(field.getType()));
                fieldInfo.setFkIdName(fkIdField.getName());// 注意：外键id名这里不用转换

            } else if (field.getAnnotation(OneToMany.class) != null ||
                    field.getAnnotation(ManyToMany.class) != null) {// 如果是外键对象列表
                fieldInfo.setType(3);

            } else {// 如果是基本数据对象（数值，字符串，日期）
                fieldInfo.setType(0);
            }

            fieldInfoList.add(fieldInfo);
        }

        dataModel.put("haveFk", haveFk);
        dataModel.put("fields", fieldInfoList);
        LogUtil.printEntity(dataModel);

        FreeMakerUtil.create(new File(daoFltFilePath), dataModel, writer);
    }

    public void createMapper(Class pojoClass, String daoName, Writer writer) throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("daoName", daoName);// 例如：com.bean.UserInfoDao
        dataModel.put("pojoName", pojoClass.getName());// 例如：com.bean.UserInfo

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (Field field : pojoClass.getDeclaredFields()) {
            if (field.getAnnotation(Transient.class) != null) {// 如果需要忽略
                continue;
            }

            String fieldName = field.getName();
            String columnName = fieldName;
            if (converter != null) {
                columnName = converter.toColumnName(field);
            }
            FieldInfo fieldInfo = new FieldInfo(fieldName, columnName);

            if (field.getAnnotation(Id.class) != null) {// 如果是主键
                fieldInfo.setType(1);

            } else if (field.getAnnotation(ManyToOne.class) != null ||
                    field.getAnnotation(OneToOne.class) != null) {// 如果是外键对象
                fieldInfo.setType(2);
                Field fkIdField = ReflectUtil.findByAnno(field.getType(), Id.class);
                assert fkIdField != null;
                fieldInfo.setFkClassName(field.getType().getName());
                fieldInfo.setFkTableName(converter.toTableName(field.getType()));
                fieldInfo.setFkIdName(fkIdField.getName());// 注意：外键id名这里不用转换

            } else if (field.getAnnotation(OneToMany.class) != null ||
                    field.getAnnotation(ManyToMany.class) != null) {// 如果是外键对象列表
                fieldInfo.setType(3);

            } else {// 如果是基本数据对象（数值，字符串，日期）
                fieldInfo.setType(0);
            }

            fieldInfoList.add(fieldInfo);
        }

        dataModel.put("fields", fieldInfoList);
        LogUtil.printEntity(dataModel);

        FreeMakerUtil.create(new File(mapperFltFilePath), dataModel, writer);
    }

    public MybatisCreator setSpringAnno(boolean springAnno) {
        this.springAnno = springAnno;
        return this;
    }

    public MybatisCreator setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }

    public MybatisCreator setNameConverter(NameConverter converter) {
        this.converter = converter;
        return this;
    }
}
