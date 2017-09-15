package com.wangrg.db;

import com.wangrg.db.basis.Constraint;
import com.wangrg.db.basis.ConstraintAnno;
import com.wangrg.db.basis.FieldType;
import com.wangrg.db.basis.TypeAnno;
import com.wangrg.db.basis.ValueType;
import com.wangrg.db.basis.Where;
import com.wangrg.db.connection.DbHelper;
import com.wangrg.db.connection.MysqlDbHelper;
import com.wangrg.java_util.DebugUtil;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * by wangrongjun on 2017/3/26.
 */
public class TestDataBase {

    private static Connection connection;

    private void execute(String sql) throws SQLException {
        System.out.println(sql);
        System.out.println();
        connection.prepareStatement(sql).execute();
    }

    private int executeQueryCount(String sql) throws SQLException {
        System.out.println(sql);
        System.out.println();
        ResultSet rs = connection.prepareStatement(sql).executeQuery();
        int count = Dao.getCount(rs);
        rs.close();
        return count;
    }

    private <T> List<T> executeQuery(Class<T> entityClass, String sql) throws SQLException {
        System.out.println(sql);
        System.out.println();
        ResultSet rs = connection.prepareStatement(sql).executeQuery();
        List<T> result = Dao.getResult(entityClass, rs);
        rs.close();
        return result;
    }

    @BeforeClass
    public static void initDatabase() throws SQLException {
        DbHelper dbHelper = new MysqlDbHelper("root", "21436587", "test");
        connection = dbHelper.getConnection();
    }

    @AfterClass
    public static void closeDatabase() throws SQLException {
        connection.close();
    }

    @Test
    public void testAll() throws SQLException {
        testDropTable();
        testCreateTable();
        testInsert();
        testUpdate();
        testDelete();
        testQueryAll();
        testQueryFuzzy();
        testQueryLimit();
        testDeleteAll();
    }

    @Test
    public void testDropTable() throws SQLException {
        String sql = SqlUtil.dropTableSql(User.class.getSimpleName());
        execute(sql);
    }

    @Test
    public void testCreateTable() throws SQLException {
        String sql = SqlEntityUtil.createTableSql(User.class);
        execute(sql);
    }

    @Test
    public void testInsert() throws SQLException {
        for (int i = 1; i <= 10; i++) {
            User user = new User("phone" + i, "pass" + i, "nickname" + i, i % 2 + 1);
            String sql = SqlEntityUtil.insertSql(user);
            execute(sql);
        }

        String sql = SqlUtil.queryCountSql(User.class.getSimpleName(), null);
        int count = executeQueryCount(sql);
        assertEquals(10, count);
    }

    @Test
    public void testUpdate() throws SQLException {
        // 把userId=1的用户的手机号改为15521302230
        String sql = SqlEntityUtil.updateSql(User.class, "1", "phone", "15521302230");
        execute(sql);

        // 把userId=2的用户的所有信息进行修改
        User user = new User("13023796942", "123456", "mei_mei", 2);
        user.setUserId(2);
        sql = SqlEntityUtil.updateByIdSql(user);
        execute(sql);
    }

    @Test
    public void testDelete() throws SQLException {
        // 删除userId=8的记录
        String sql = SqlEntityUtil.deleteByIdSql(User.class, "8");
        execute(sql);

        // 删除nickname="nickname6"的记录
        sql = SqlEntityUtil.deleteSql(User.class, "nickname", "nickname6");
        execute(sql);

        sql = SqlUtil.queryCountSql(User.class.getSimpleName(), null);
        int count = executeQueryCount(sql);
        assertEquals(8, count);

    }

    @Test
    public void testDeleteAll() throws SQLException {
        String sql = SqlEntityUtil.deleteAllSql(User.class);
        execute(sql);

        sql = SqlUtil.queryCountSql(User.class.getSimpleName(), null);
        int count = executeQueryCount(sql);
        assertEquals(0, count);
    }

    @Test
    public void testQueryAll() throws SQLException {
        String sql = SqlEntityUtil.queryAllSql(User.class);
        List<User> userList = executeQuery(User.class, sql);
        DebugUtil.printlnEntity(userList);
    }

    @Test
    public void testQueryFuzzy() throws SQLException {
        String sql = SqlUtil.queryFuzzySql(User.class.getSimpleName(), "nickname", "nick",
                Collections.singletonList("-password"));
        List<User> userList = executeQuery(User.class, sql);
        DebugUtil.printlnEntity(userList);

        for (User user : userList) {
            assertEquals(true, user.getNickname().contains("nick"));
        }
    }

    @Test
    public void testQueryLimit() throws SQLException {
        // 查询nickname!="nickname4" 并且按照gender，password倒序排序，从第2项开始取5项
        List<String> orderByList = Arrays.asList("-gender", "-password");
        Where where = new Where().add("nickname", "nickname4", ValueType.TEXT, Where.QueryMode.NOT_EQUAL);
        String sql = SqlUtil.querySql(User.class.getSimpleName(), where, orderByList, 1, 5);
        List<User> userList = executeQuery(User.class, sql);
        DebugUtil.printlnEntity(userList);

        assertEquals(true, userList.size() == 5);
    }

    static class User {
        @TypeAnno(type = FieldType.INT)
        @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
        private int userId;

        @TypeAnno(type = FieldType.VARCHAR_20)
        @ConstraintAnno(constraint = Constraint.UNIQUE_NOT_NULL)
        private String phone;

        @TypeAnno(type = FieldType.VARCHAR_50)
        @ConstraintAnno(constraint = Constraint.NOT_NULL)
        private String password;

        @TypeAnno(type = FieldType.VARCHAR_50)
        private String nickname;

        @TypeAnno(type = FieldType.TINYINT)
        @ConstraintAnno(constraint = Constraint.DEFAULT, defaultValue = "-1")
        private int gender;

        public User() {
        }

        public User(String phone, String password, String nickname, int gender) {
            this.phone = phone;
            this.password = password;
            this.nickname = nickname;
            this.gender = gender;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }
    }

}
