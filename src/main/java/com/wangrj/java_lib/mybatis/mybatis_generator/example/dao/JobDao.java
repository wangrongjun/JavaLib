package com.wangrj.java_lib.mybatis.mybatis_generator.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job;

public interface JobDao {

/*
<resultMap id="singleMap" type="com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job">
    <id column="jobId" property="jobId"/>
    <result column="jobName" property="jobName"/>
</resultMap>

<resultMap id="multiMap" type="com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job" extends="singleMap"/>

*/

    @SelectProvider(type = Builder.class, method = "queryByIdSql")
    @ResultType(Job.class)
    Job queryById(Integer jobId);

    @SelectProvider(type = Builder.class, method = "queryAllSql")
    @ResultType(Job.class)
    List<Job> queryAll(@Param("orderByList") String... orderByList);

    @SelectProvider(type = Builder.class, method = "querySql")
    @ResultType(Job.class)
    List<Job> query(@Param("job") Job job, @Param("orderByList") String... orderByList);

    @SelectProvider(type = Builder.class, method = "queryAllCountSql")
    int queryAllCount();

    @SelectProvider(type = Builder.class, method = "queryCountSql")
    int queryCount(Job job);

    @InsertProvider(type = Builder.class, method = "insertSql")
    // MySQL自增主键的配置：@Options(useGeneratedKeys = true, keyProperty = "userId")
    // 如果Oracle不是使用触发器生成id，可以把currval改为nextval，同时设置before=true
    @SelectKey(statement = "SELECT sequence_Job.currval FROM dual", keyProperty = "jobId",
            before = false, resultType = int.class)
    
    int insert(Job job);

    @DeleteProvider(type = Builder.class, method = "deleteSql")
    int deleteById(Integer jobId);

    /**
     * 更新时忽略空值
     */
    @UpdateProvider(type = Builder.class, method = "updateSql")
    int update(Job job);

    /**
     * 空值也会更新
     * 注意：必须在全局配置中设置<setting username="jdbcTypeForNull" value="NULL"/>，否则报错
     */
    @UpdateProvider(type = Builder.class, method = "updateContainsNullSql")
    int updateContainsNull(Job job);

    class Builder {
        public String queryByIdSql(Integer userId) {
            return new SQL() {{
                SELECT("*");
                FROM("Job");
                WHERE("jobId=#{id}");
            }}.toString();
        }

        public String queryAllSql(@Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("Job");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String querySql(@Param("job") Job job, @Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("Job");
                if (job.getJobId() != null) WHERE("jobId=#{job.jobId}");
                if (job.getJobName() != null) WHERE("jobName=#{job.jobName}");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String queryAllCountSql() {
            return new SQL() {{
                SELECT("count(1)");
                FROM("Job");
            }}.toString();
        }

        public String queryCountSql(Job job) {
            return new SQL() {{
                SELECT("count(1)");
                FROM("Job");
                if (job.getJobId() != null) WHERE("jobId=#{jobId}");
                if (job.getJobName() != null) WHERE("jobName=#{jobName}");
            }}.toString();
        }

        public String insertSql() {
            return new SQL() {{
                INSERT_INTO("Job");
                INTO_COLUMNS(
                
                "jobName"
                );
                INTO_VALUES(
                
                "#{jobName}"
                );
            }}.toString();
        }

        public String deleteSql() {
            return new SQL() {{
                DELETE_FROM("Job");
                WHERE("jobId = #{id}");
            }}.toString();
        }

        public String updateSql(Job job) {
            return new SQL() {{
                UPDATE("Job");
                    if (job.getJobName() != null) SET("jobName=#{jobName}");
                WHERE("jobId=#{jobId}");
            }}.toString();
        }

        public String updateContainsNullSql() {
            return new SQL() {{
                UPDATE("Job");
                SET(
                        "jobName=#{jobName}"
                );
                WHERE("jobId=#{jobId}");
            }}.toString();
        }
    }

}
