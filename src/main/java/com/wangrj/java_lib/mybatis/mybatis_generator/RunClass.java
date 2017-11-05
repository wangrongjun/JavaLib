package com.wangrj.java_lib.mybatis.mybatis_generator;

import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.dao.JobDao;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.dao.UserInfoDao;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.persistence.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2017/11/4.
 */
public class RunClass {

    public static void main(String[] args) throws IOException, TemplateException {
        Class daoClass = UserInfoDao.class;
        Class pojoClass = UserInfo.class;
//        Class daoClass = JobDao.class;
//        Class pojoClass = Job.class;
        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("daoName", daoClass.getName());
        dataModel.put("pojoName", pojoClass.getName());
        dataModel.put("pojoSimpleName", pojoClass.getSimpleName());
        dataModel.put("pojoIdName", ReflectUtil.findByAnno(pojoClass, Id.class).getName());

        List<FieldInfo> fieldInfoList = new ArrayList<>();
        for (Field field : pojoClass.getDeclaredFields()) {
            if (field.getAnnotation(Transient.class) != null ||
                    field.getAnnotation(OneToMany.class) != null) {// 如果需要忽略
                continue;
            }
            if (field.getAnnotation(Id.class) != null) {// 如果是主键
                fieldInfoList.add(new FieldInfo(1, field.getName()));

            } else if (field.getAnnotation(ManyToOne.class) != null ||
                    field.getAnnotation(OneToOne.class) != null) {// 如果是外键对象
                String fkClassName = field.getType().getName();
                String fkIdName = ReflectUtil.findByAnno(field.getType(), Id.class).getName();
                fieldInfoList.add(new FieldInfo(2, field.getName(), fkClassName, fkIdName));

            } else {// 如果是基本数据对象（数值，字符串，日期）
                fieldInfoList.add(new FieldInfo(0, field.getName()));

            }
        }
        dataModel.put("fields", fieldInfoList);
        LogUtil.printEntity(fieldInfoList);

        create(dataModel);
    }

    public static void create(Map<String, Object> dataModel) throws IOException, TemplateException {
        String fltFilePath = "C:/IDE/ideaIU-project/JavaLib/src/main/java/" +
                "com/wangrj/java_lib/mybatis/mybatis_generator/templates/mapper.flt";
        Template template = new Template(null, new FileReader(fltFilePath), new Configuration());
//        Map<String, String> map = new HashMap<>();
//        map.put("pojoClasspath", "com.handsome.shop.bean.UserInfo");
        template.process(dataModel, new OutputStreamWriter(System.out));
    }

}
