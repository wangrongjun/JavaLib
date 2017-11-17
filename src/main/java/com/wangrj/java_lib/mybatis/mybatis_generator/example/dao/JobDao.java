package com.wangrj.java_lib.mybatis.mybatis_generator.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job;

public interface JobDao {

/*
<resultMap id="singleMap" type="com.wangrj.java_lib.mybatis.mybatis_generator.example.bean.Job">
    <id column="job_id" property="jobId"/>
    <result column="job_name" property="jobName"/>
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
    @SelectKey(statement = "SELECT sequence_job.currval FROM dual", keyProperty = "jobId",
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
                FROM("job");
                WHERE("job_id=#{id}");
            }}.toString();
        }

        public String queryAllSql(@Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("job");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String querySql(@Param("job") Job job, @Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("job");
                if (job.getJobId() != null) WHERE("job_id=#{job.jobId}");
                if (job.getJobName() != null) WHERE("job_name=#{job.jobName}");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String queryAllCountSql() {
            return new SQL() {{
                SELECT("count(1)");
                FROM("job");
            }}.toString();
        }

        public String queryCountSql(Job job) {
            return new SQL() {{
                SELECT("count(1)");
                FROM("job");
                if (job.getJobId() != null) WHERE("job_id=#{jobId}");
                if (job.getJobName() != null) WHERE("job_name=#{jobName}");
            }}.toString();
        }

        public String insertSql() {
            return new SQL() {{
                INSERT_INTO("job");
                INTO_COLUMNS(
                
                "job_name"
                );
                INTO_VALUES(
                
                "#{jobName}"
                );
            }}.toString();
        }

        public String deleteSql() {
            return new SQL() {{
                DELETE_FROM("job");
                WHERE("job_id = #{id}");
            }}.toString();
        }

        public String updateSql(Job job) {
            return new SQL() {{
                UPDATE("job");
                    if (job.getJobName() != null) SET("job_name=#{jobName}");
                WHERE("job_id=#{jobId}");
            }}.toString();
        }

        public String updateContainsNullSql() {
            return new SQL() {{
                UPDATE("job");
                SET(
                        "job_name=#{jobName}"
                );
                WHERE("job_id=#{jobId}");
            }}.toString();
        }
    }

}
