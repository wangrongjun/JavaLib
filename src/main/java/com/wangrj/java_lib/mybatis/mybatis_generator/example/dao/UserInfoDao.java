package com.wangrj.java_lib.mybatis.mybatis_generator.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo;

public interface UserInfoDao {

/*
<resultMap id="singleMap" type="com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo">
    <id column="user_id" property="userId"/>
    <result column="username" property="username"/>
    <result column="sex" property="sex"/>
    <result column="reg_date" property="regDate"/>
    <association property="job" javaType="job">
        <id column="job" property="jobId"/>
    </association>
</resultMap>

<resultMap id="multiMap" type="com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.UserInfo" extends="singleMap"/>

*/

    @SelectProvider(type = Builder.class, method = "queryByIdSql")
    @ResultMap("singleMap")
    UserInfo queryById(Integer userId);

    @SelectProvider(type = Builder.class, method = "queryAllSql")
    @ResultMap("multiMap")
    List<UserInfo> queryAll(@Param("orderByList") String... orderByList);

    @SelectProvider(type = Builder.class, method = "querySql")
    @ResultMap("multiMap")
    List<UserInfo> query(@Param("userInfo") UserInfo userInfo, @Param("orderByList") String... orderByList);

    @SelectProvider(type = Builder.class, method = "queryAllCountSql")
    int queryAllCount();

    @SelectProvider(type = Builder.class, method = "queryCountSql")
    int queryCount(UserInfo userInfo);

    @InsertProvider(type = Builder.class, method = "insertSql")
    // MySQL自增主键的配置：@Options(useGeneratedKeys = true, keyProperty = "userId")
    // 如果Oracle不是使用触发器生成id，可以把currval改为nextval，同时设置before=true
    @SelectKey(statement = "SELECT sequence_user_info.currval FROM dual", keyProperty = "userId",
            before = false, resultType = int.class)
    
    int insert(UserInfo userInfo);

    @DeleteProvider(type = Builder.class, method = "deleteSql")
    int deleteById(Integer userId);

    /**
     * 更新时忽略空值
     */
    @UpdateProvider(type = Builder.class, method = "updateSql")
    int update(UserInfo userInfo);

    /**
     * 空值也会更新
     * 注意：必须在全局配置中设置<setting username="jdbcTypeForNull" value="NULL"/>，否则报错
     */
    @UpdateProvider(type = Builder.class, method = "updateContainsNullSql")
    int updateContainsNull(UserInfo userInfo);

    class Builder {
        public String queryByIdSql(Integer userId) {
            return new SQL() {{
                SELECT("*");
                FROM("user_info");
                WHERE("user_id=#{id}");
            }}.toString();
        }

        public String queryAllSql(@Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("user_info");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String querySql(@Param("userInfo") UserInfo userInfo, @Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("user_info");
                if (userInfo.getUserId() != null) WHERE("user_id=#{userInfo.userId}");
                if (userInfo.getUsername() != null) WHERE("username=#{userInfo.username}");
                if (userInfo.getSex() != null) WHERE("sex=#{userInfo.sex}");
                if (userInfo.getRegDate() != null) WHERE("reg_date=#{userInfo.regDate}");
                if (userInfo.getJob() != null) WHERE("job=#{userInfo.job.jobId}");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String queryAllCountSql() {
            return new SQL() {{
                SELECT("count(1)");
                FROM("user_info");
            }}.toString();
        }

        public String queryCountSql(UserInfo userInfo) {
            return new SQL() {{
                SELECT("count(1)");
                FROM("user_info");
                if (userInfo.getUserId() != null) WHERE("user_id=#{userId}");
                if (userInfo.getUsername() != null) WHERE("username=#{username}");
                if (userInfo.getSex() != null) WHERE("sex=#{sex}");
                if (userInfo.getRegDate() != null) WHERE("reg_date=#{regDate}");
                if (userInfo.getJob() != null) WHERE("job=#{job.jobId}");
            }}.toString();
        }

        public String insertSql() {
            return new SQL() {{
                INSERT_INTO("user_info");
                INTO_COLUMNS(
                
                "username", 
                "sex", 
                "reg_date", 
                "job"
                );
                INTO_VALUES(
                
                "#{username}", 
                "#{sex}", 
                "#{regDate}", 
                "#{job.jobId}"
                );
            }}.toString();
        }

        public String deleteSql() {
            return new SQL() {{
                DELETE_FROM("user_info");
                WHERE("user_id = #{id}");
            }}.toString();
        }

        public String updateSql(UserInfo userInfo) {
            return new SQL() {{
                UPDATE("user_info");
                    if (userInfo.getUsername() != null) SET("username=#{username}");
                    if (userInfo.getSex() != null) SET("sex=#{sex}");
                    if (userInfo.getRegDate() != null) SET("reg_date=#{regDate}");
                    if (userInfo.getJob() != null) SET("job=#{job.jobId}");
                WHERE("user_id=#{userId}");
            }}.toString();
        }

        public String updateContainsNullSql() {
            return new SQL() {{
                UPDATE("user_info");
                SET(
                        "username=#{username}", 
                        "sex=#{sex}", 
                        "reg_date=#{regDate}", 
                        "job=#{job.jobId}"
                );
                WHERE("user_id=#{userId}");
            }}.toString();
        }
    }

}
