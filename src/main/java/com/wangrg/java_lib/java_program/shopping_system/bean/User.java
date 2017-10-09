package com.wangrg.java_lib.java_program.shopping_system.bean;

import com.wangrg.java_lib.db.basis.Constraint;
import com.wangrg.java_lib.db.basis.ConstraintAnno;
import com.wangrg.java_lib.db.basis.FieldType;
import com.wangrg.java_lib.db.basis.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 用户
 */
public class User {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int userId;

    @TypeAnno(type = FieldType.VARCHAR_20)
    @ConstraintAnno(constraint = Constraint.UNIQUE_NOT_NULL)
    private String phone;//手机号

    @TypeAnno(type = FieldType.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String password;

    @TypeAnno(type = FieldType.VARCHAR_20)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String realName;//真实姓名

    @TypeAnno(type = FieldType.VARCHAR_50)
    private String nickname;//昵称

    @TypeAnno(type = FieldType.TINYINT)
    @ConstraintAnno(constraint = Constraint.DEFAULT, defaultValue = "-1")
    private int gender;//-1为未设置，0为女，1为男，默认为-1

    public User() {
    }

    public User(String phone, String password, String realName, String nickname, int gender) {
        this.phone = phone;
        this.password = password;
        this.realName = realName;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
