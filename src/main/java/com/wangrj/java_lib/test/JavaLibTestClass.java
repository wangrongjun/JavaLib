package com.wangrj.java_lib.test;

import com.wangrj.java_lib.java_util.DbHelper;
import com.wangrj.java_lib.java_util.GsonUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JavaLibTestClass {

    public static void main(String[] args) throws SQLException {
        DbHelper dbHelper = DbHelper.buildForMysql("root", "21436587", "c_utf8");
        dbHelper.executeUpdate("drop table if exists emoji");
        dbHelper.executeUpdate("create table emoji(signId int primary key auto_increment,emoji varchar(20)) charset=utf8mb4");
        dbHelper.executeUpdate("insert into emoji(emoji) values('☀')");
        dbHelper.executeUpdate("insert into emoji(emoji) values('☔')");
        dbHelper.executeUpdate("insert into emoji(emoji) values('☠')");
        dbHelper.executeUpdate("insert into emoji(emoji) values('♠')");
        dbHelper.executeUpdate("insert into emoji(emoji) values('♣')");
        dbHelper.executeUpdate("insert into emoji(emoji) values('♥')");
        dbHelper.executeUpdate("insert into emoji(emoji) values('♦')");
        List<Map<String, Object>> maps = dbHelper.queryMap(new String[]{"signId", "emoji"}, "select signId,emoji from emoji");
        GsonUtil.printPrettyJson(maps);
    }

}
