package com.wangrj.java_lib.mybatis.mybatis_generator.example;

import com.wangrj.java_lib.constant.JavaLibConstant;
import com.wangrj.java_lib.db3.DbUtil;
import com.wangrj.java_lib.java_util.DateUtil;
import com.wangrj.java_lib.java_util.ListUtil;
import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.dao.JobDao;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.dao.UserInfoDao;
import com.wangrj.java_lib.mybatis.mybatis_generator.v2.MybatisDaoCreator;
import freemarker.template.TemplateException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * by wangrongjun on 2017/11/4.
 */
public class TestClass {

    private SqlSession session;
    private JobDao jobDao;
    private UserInfoDao userInfoDao;

    public static void main(String[] args) throws IOException, TemplateException {
        String daoPackageName = TestClass.class.getPackage().getName() + ".dao";
        String daoPackageDir = JavaLibConstant.classDir(TestClass.class) + "dao\\";

        /*
        String jobDaoName = daoPackageName + ".JobDao";
        new MybatisCreator().createMapper(Job.class, jobDaoName, new FileWriter(daoPackageDir + "JobDao.xml"));
        new MybatisCreator().createDao(Job.class, jobDaoName, new FileWriter(daoPackageDir + "JobDao.java"));

        String userInfoDaoName = daoPackageName + ".UserInfoDao";
        new MybatisCreator().createMapper(UserInfo.class, userInfoDaoName, new FileWriter(daoPackageDir + "UserInfoDao.xml"));
        new MybatisCreator().createDao(UserInfo.class, userInfoDaoName, new FileWriter(daoPackageDir + "UserInfoDao.java"));
        */

        MybatisDaoCreator creator = new MybatisDaoCreator();
        creator.createDao(Job.class, JobDao.class.getName(), new FileWriter(daoPackageDir + "JobDao.java"));
        creator.createDao(UserInfo.class, UserInfoDao.class.getName(), new FileWriter(daoPackageDir + "UserInfoDao.java"));
    }

    @Test
    public void testAll() {
        testInsert();
        testDelete();
        testUpdate();
        testUpdateContainsNull();
        testQueryAll();
        testQuery();
    }

    @Test
    public void testInsert() {
        DbUtil.dropAndCreateTables(DbUtil.DbType.Oracle, "orcl", "wang", "123",
                ListUtil.build(Job.class, UserInfo.class));

        Job 程序员 = new Job("程序员");
        Job 销售员 = new Job("销售员");
        jobDao.insert(程序员);
        jobDao.insert(销售员);

        userInfoDao.insert(new UserInfo("英俊", 1, d("2016-1-2"), 程序员));
        userInfoDao.insert(new UserInfo("沫宝儿", 0, d("2016-1-3"), 销售员));
        for (int i = 1; i <= 20; i++) {
            userInfoDao.insert(new UserInfo(
                    "username_" + i,
                    i % 2,
                    d("2016-1-" + i),
                    i % 3 == 0 ? 程序员 : 销售员
            ));
        }
    }

    @Test
    public void testUpdate() {
        UserInfo userInfo = new UserInfo(1);
        userInfo.setJob(new Job(2));
        userInfoDao.update(userInfo);
        testQueryAll();
    }

    @Test
    public void testUpdateContainsNull() {
        UserInfo userInfo = new UserInfo(2);
        userInfoDao.updateContainsNull(userInfo);
        userInfo.setUsername("hello");
        userInfoDao.update(userInfo);
        testQueryAll();
    }

    @Test
    public void testDelete() {
        userInfoDao.deleteById(1);
        testQueryAll();
    }

    @Test
    public void testQueryAll() {
        userInfoDao.queryAll("sex", "userId");
    }

    @Test
    public void testQuery() {
        userInfoDao.queryAllCount();
        userInfoDao.queryCount(new UserInfo(5));

        userInfoDao.queryAll("sex desc", "username desc");

        LogUtil.printEntity(userInfoDao.queryById(2));

//        LogUtil.printEntity(userInfoDao.queryAllLimit(1, 3));
//        LogUtil.printEntity(userInfoDao.queryAllLimit(4, 4));
        LogUtil.printEntity(userInfoDao.queryAllCount());

        UserInfo userInfo = new UserInfo();
        userInfo.setRegDate(d("2016-1-2"));
        LogUtil.printEntity(userInfoDao.query(userInfo));

        userInfo = new UserInfo();
        userInfo.setJob(new Job(1));
        LogUtil.printEntity(userInfoDao.queryCount(userInfo));
//        LogUtil.printEntity(userInfoDao.queryLimit(userInfo, 2, 6));
    }

    @Before
    public void init() throws IOException {
//        InputStream is = Resources.getResourceAsStream("SqlMapConfig.xml");
        String path = "C:/IDE/ideaIU-project/JavaLib/src/main/java/" +
                "com/wangrj/java_lib/mybatis/mybatis_generator/example/SqlMapConfig.xml";
        FileInputStream fis = new FileInputStream(path);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(fis);
        session = factory.openSession();
        jobDao = session.getMapper(JobDao.class);
        userInfoDao = session.getMapper(UserInfoDao.class);
    }

    @After
    public void close() {
        session.commit();
        session.close();
    }

    private Date d(String date) {
        return DateUtil.toDate(date);
    }

}
