package com.wangrj.java_lib.java_util;

import org.junit.Test;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2018/6/23.
 */
public class DbHelperTest {

    public static class User {
        @Id
        @GeneratedValue
        int userId;
        String name;
        int age;
        Date birthday;

        public User() {
        }

        public User(String name, int age, Date birthday) {
            this.name = name;
            this.age = age;
            this.birthday = birthday;
        }
    }

    @Test
    public void test() throws SQLException {
        DbHelper dbHelper = DbHelper.buildForMysql("root", "21436587", "test");
        dbHelper.dropTable(User.class);
        dbHelper.createTable(User.class);
        for (int i = 1; i < 10; i++) {
            dbHelper.insert(new User("name_" + i, 20 + i, DateUtil.toDate("2018-06-2" + i)));
        }

        int count = dbHelper.executeQueryCount("select count(1) from User");
        LogUtil.printEntity(count);

        List<User> userList = dbHelper.queryAll(User.class);
        assert userList.size() == 9;
        LogUtil.printEntity(userList);

        List<Map<String, Object>> mapList = dbHelper.queryMap(
                new String[]{"userId", "name"},
                "select userId,name from User"
        );
        LogUtil.printEntity(mapList);
    }

}
