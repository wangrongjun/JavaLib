package com.wangrj.java_lib.java_util.excel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestUser {

    private Integer userId;
    private String username;
    @EntityConverter.EntityConverterIgnore
    private String password;
    private Boolean gender;
    private Long age;
    private Date birthday;
    private Double salary;
    private LocalDate localDate;
    private LocalDateTime localDateTime;

    public static List<TestUser> example(int size) throws ParseException {
        List<TestUser> userList = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("1996-01-" + (i < 10 ? "0" + i : i));
            TestUser user = new TestUser(i, "name_" + i, "pass_" + i, true, (long) i, date, i + 0.5, LocalDate.now(), LocalDate.now().atStartOfDay());
            userList.add(user);
        }
        return userList;
    }

    public TestUser() {
    }

    public TestUser(Integer userId, String username, String password, Boolean gender, Long age, Date birthday, Double salary, LocalDate localDate, LocalDateTime localDateTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.salary = salary;
        this.localDate = localDate;
        this.localDateTime = localDateTime;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
