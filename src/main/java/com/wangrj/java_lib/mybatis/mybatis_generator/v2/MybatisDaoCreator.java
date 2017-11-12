package com.wangrj.java_lib.mybatis.mybatis_generator.v2;

import com.wangrj.java_lib.constant.JavaLibConstant;
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
public class MybatisDaoCreator {

    private static final String daoFltFilePath = JavaLibConstant.srcDir() +
            "com/wangrj/java_lib/mybatis/mybatis_generator/v2/templates/dao.ftl";

    /**
     * dao是否生成 @Repository 和 @MapperScan 注解
     */
    private boolean springAnno = false;
    /**
     * 数据库种类，如Oracle，MySQL
     */
    private String dbType = "Oracle";

    @Test
    public void testCreateDao() throws IOException, TemplateException {
        createDao(UserInfo.class, UserInfoDao.class.getName(), new OutputStreamWriter(System.out));
        createDao(Job.class, JobDao.class.getName(), new OutputStreamWriter(System.out));
    }

    public void createDao(Class pojoClass, String daoName, Writer writer)
            throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();

        String simpleName = pojoClass.getSimpleName();
        dataModel.put("daoPackage", TextUtil.getTextExceptLastPoint(daoName));// 例如：com.dao
        dataModel.put("daoSimpleName", TextUtil.getTextAfterLastPoint(daoName));// 例如：UserInfoDao
        dataModel.put("pojoName", pojoClass.getName());// 例如：com.bean.UserInfo
        dataModel.put("pojoSimpleName", simpleName);// 例如：UserInfo
        String varName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1, simpleName.length());
        dataModel.put("pojoVarName", varName);// 例如：userInfo
        dataModel.put("springAnno", springAnno);

        Field idField = ReflectUtil.findByAnno(pojoClass, Id.class);
        assert idField != null;
        dataModel.put("pojoIdType", idField.getType().getSimpleName());// 例如：Integer
        dataModel.put("pojoIdName", idField.getName());// 例如：userId

        // 插入数据操作返回id的注解。0：@SelectKey。1：@Options。
        dataModel.put("insertReturnKeyAnno", dbType.equalsIgnoreCase("Oracle") ? 0 : 1);

        boolean haveFk = false;// 是否有外键，这决定了查询结果映射注解是@ResultType还是@ResultMap

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (Field field : pojoClass.getDeclaredFields()) {

            if (field.getAnnotation(Transient.class) != null) {// 如果需要忽略
                continue;
            }

            String name = field.getName();
            String getter = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());

            FieldInfo fieldInfo = new FieldInfo(name, getter);

            if (field.getAnnotation(Id.class) != null) {// 如果是主键
                // 主键是否自增，决定插入语句是否需要主键赋值的set子句
                dataModel.put("autoIncrement", field.getAnnotation(GeneratedValue.class) != null);
                fieldInfo.setType(1);

            } else if (field.getAnnotation(ManyToOne.class) != null ||
                    field.getAnnotation(OneToOne.class) != null) {// 如果是外键对象
                haveFk = true;
                String fkClassName = field.getType().getName();
                String fkIdName = ReflectUtil.findByAnno(field.getType(), Id.class).getName();
                fieldInfo.setType(2);
                fieldInfo.setFkClassName(fkClassName);
                fieldInfo.setFkIdName(fkIdName);
                // TODO singleMap 和 multiMap

            } else if (field.getAnnotation(OneToMany.class) != null ||
                    field.getAnnotation(ManyToMany.class) != null) {// 如果是外键对象列表
                fieldInfo.setType(3);
                // TODO singleMap 和 multiMap

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

    public MybatisDaoCreator setSpringAnno(boolean springAnno) {
        this.springAnno = springAnno;
        return this;
    }

    public MybatisDaoCreator setDbType(String dbType) {
        this.dbType = dbType;
        return this;
    }
}
