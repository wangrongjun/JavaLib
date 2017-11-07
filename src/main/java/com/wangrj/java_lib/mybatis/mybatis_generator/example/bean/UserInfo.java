package com.wangrj.java_lib.mybatis.mybatis_generator.example.bean;

import com.wangrj.java_lib.db3.main.UnionUniqueKey;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * by wangrongjun on 2017/11/4.
 */
public class UserInfo {

    @Id
    @GeneratedValue
    private Integer userId;
    private String username;
    @UnionUniqueKey
    private Integer sex;
    @UnionUniqueKey
    private Date regDate;
    @ManyToOne
    private Job job;

    public UserInfo() {
    }

    public UserInfo(Integer userId) {
        this.userId = userId;
    }

    public UserInfo(String username, Integer sex, Date regDate, Job job) {
        this.username = username;
        this.sex = sex;
        this.regDate = regDate;
        this.job = job;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
