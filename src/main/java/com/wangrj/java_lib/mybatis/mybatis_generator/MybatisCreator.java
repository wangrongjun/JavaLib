package com.wangrj.java_lib.mybatis.mybatis_generator;

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
public class MybatisCreator {

    private static final String mapperFltFilePath = JavaLibConstant.srcDir() +
            "com/wangrj/java_lib/mybatis/mybatis_generator/templates/mapper.ftl";

    private static final String daoFltFilePath = JavaLibConstant.srcDir() +
            "com/wangrj/java_lib/mybatis/mybatis_generator/templates/dao.ftl";

    private boolean createAnno = false;// dao是否生成 @Repository和 @MapperScan 注解

    @Test
    public void testCreateMapper() throws IOException, TemplateException {
        createMapper(UserInfo.class, UserInfoDao.class.getName(), new OutputStreamWriter(System.out));
        createMapper(Job.class, JobDao.class.getName(), new OutputStreamWriter(System.out));
    }

    @Test
    public void testCreateDao() throws IOException, TemplateException {
        createDao(UserInfo.class, UserInfoDao.class.getName(), new OutputStreamWriter(System.out));
        createDao(Job.class, JobDao.class.getName(), new OutputStreamWriter(System.out));
    }

    public void createMapper(Class pojoClass, String daoName, Writer writer)
            throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("daoName", daoName);// 例如：com.dao.UserInfoDao
        dataModel.put("pojoName", pojoClass.getName());// 例如：com.bean.UserInfo
        dataModel.put("pojoSimpleName", pojoClass.getSimpleName());// 例如：UserInfo
        dataModel.put("pojoIdName", ReflectUtil.findByAnno(pojoClass, Id.class).getName());// 例如：userId

        boolean haveFk = false;// 是否有外键，这决定了映射文件的查询方法是使用resultType还是resultMap

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (Field field : pojoClass.getDeclaredFields()) {
            if (field.getAnnotation(Transient.class) != null) {// 如果需要忽略
                continue;
            }
            if (field.getAnnotation(Id.class) != null) {// 如果是主键
                fieldInfoList.add(new FieldInfo(1, field.getName()));

            } else if (field.getAnnotation(ManyToOne.class) != null ||
                    field.getAnnotation(OneToOne.class) != null) {// 如果是外键对象
                haveFk = true;
                String fkClassName = field.getType().getName();
                String fkIdName = ReflectUtil.findByAnno(field.getType(), Id.class).getName();
                fieldInfoList.add(new FieldInfo(2, field.getName(), fkClassName, fkIdName));

            } else if (field.getAnnotation(OneToMany.class) != null) {// 如果是外键对象列表


            } else {// 如果是基本数据对象（数值，字符串，日期）
                fieldInfoList.add(new FieldInfo(0, field.getName()));

            }
        }
        dataModel.put("haveFk", haveFk);
        dataModel.put("fields", fieldInfoList);
        LogUtil.printEntity(dataModel);

        FreeMakerUtil.create(new File(mapperFltFilePath), dataModel, writer);
    }

    public void createDao(Class pojoClass, String daoName, Writer writer)
            throws IOException, TemplateException {
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("daoPackage", TextUtil.getTextExceptLastPoint(daoName));
        dataModel.put("daoSimpleName", TextUtil.getTextAfterLastPoint(daoName));
        dataModel.put("pojoName", pojoClass.getName());
        dataModel.put("pojoSimpleName", pojoClass.getSimpleName());
        dataModel.put("createAnno", createAnno);

        FreeMakerUtil.create(new File(daoFltFilePath), dataModel, writer);
    }

    public MybatisCreator setCreateAnno(boolean createAnno) {
        this.createAnno = createAnno;
        return this;
    }

}
