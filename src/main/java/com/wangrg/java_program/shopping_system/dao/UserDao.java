package com.wangrg.java_program.shopping_system.dao;

import com.wangrg.java_program.shopping_system.bean.User;

import java.sql.SQLException;
import java.util.List;

/**
 * by wangrongjun on 2016/12/15.
 */
public class UserDao extends CustomDao<User> {

    /**
     * 根据手机号，密码查询用户，用于登录时验证用户
     */
    public User queryLogin(String phone, String password) throws SQLException {
        List<User> userList = super.query("phone", phone);
        if (userList == null || userList.size() == 0) {
            return null;
        }
        User user = userList.get(0);
        if (!user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    /**
     * 根据手机号查询用户，用于注册时判断用户是否已存在
     */
    public User query(String phone) throws SQLException {
        List<User> userList = super.query("phone", phone);
        if (userList == null || userList.size() == 0) {
            return null;
        }
        return userList.get(0);
    }

    /**
     * 根据用户id更新用户的手机号
     */
    public void updatePhone(int userId, String phone) throws SQLException {
        super.update(userId + "", "phone", phone);
    }

    /**
     * 根据用户id更新用户的密码
     */
    public void updatePassword(int userId, String password) throws SQLException {
        super.update(userId + "", "password", password);
    }

    /**
     * 根据用户id更新用户真实姓名
     */
    public void updateRealName(int userId, String realName) throws SQLException {
        super.update(userId + "", "realName", realName);
    }

    /**
     * 根据用户id更新用户昵称
     */
    public void updateNickname(int userId, String nickname) throws SQLException {
        super.update(userId + "", "nickname", nickname);
    }

    /**
     * 根据用户id更新用户性别
     */
    public void updateGender(int userId, int gender) throws SQLException {
        super.update(userId + "", "gender", gender + "");
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
