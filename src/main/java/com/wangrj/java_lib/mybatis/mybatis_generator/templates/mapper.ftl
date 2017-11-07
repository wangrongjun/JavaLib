<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${daoName}">


    <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <!-- +++++++++++++ 以下是MybatisCreator生成的默认方法 +++++++++++++ -->
    <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->

    <#-- 由于resultMap的子标签必须按照id,result,association的顺序，所以只能分成两个循环 -->
    <#if haveFk>
    <resultMap id="defaultMap" type="${pojoName}">
    <#list fields as field>
        <#if field.type==0>
        <result column="${field.name}" property="${field.name}"/>
        </#if>
        <#if field.type==1>
        <id column="${field.name}" property="${field.name}"/>
        </#if>
    </#list>
    <#list fields as field>
        <#if field.type==2>
        <association property="${field.name}" javaType="${field.fkClassName}">
            <id column="${field.name}" property="${field.fkIdName}"/>
        </association>
        </#if>
    </#list>
    </resultMap>
    </#if>

    <sql id="whereSql">
        <where>
        <#list fields as field>
            <#if field.type!=2>
            <if test="${field.name}!=null">
                AND ${field.name}=${r'#'}{${field.name}}
            </if>
            </#if>
            <#if field.type==2>
            <if test="${field.name}!=null">
                AND ${field.name}=${r'#'}{${field.name}.${field.fkIdName}}
            </if>
            </#if>
        </#list>
        </where>
    </sql>

    <insert id="insert" parameterType="${pojoName}">
        INSERT INTO ${pojoSimpleName}(
        <#list fields as field>${field.name}<#if field_has_next>,</#if></#list>
        ) VALUES (
        <#list fields as field>${r'#'}{${field.name}<#if field.type==2>.${field.fkIdName}</#if>}<#if field_has_next>,</#if></#list>
        )
        <selectKey keyProperty="${pojoIdName}" resultType="int">
            SELECT sequence_${pojoSimpleName}.currval FROM dual
        </selectKey>
    </insert>

    <delete id="deleteById" parameterType="long">
        DELETE FROM ${pojoSimpleName} WHERE ${pojoIdName}=${r'#'}{id}
    </delete>

    <update id="update" parameterType="${pojoName}">
        UPDATE ${pojoSimpleName}
        <set>
        <#list fields as field>
            <if test="${field.name}!=null">
            ${field.name}=${r'#'}{${field.name}<#if field.type==2>.${field.fkIdName}</#if>},
            </if>
        </#list>
        </set>
        WHERE ${pojoIdName}=${r'#'}{${pojoIdName}}
    </update>

    <!--必须在全局配置中设置<setting username="jdbcTypeForNull" value="NULL"/>，否则报错-->
    <update id="updateContainsNull">
        UPDATE ${pojoSimpleName} SET
        <#list fields as field>
            ${field.name}=${r'#'}{${field.name}<#if field.type==2>.${field.fkIdName}</#if>}<#if field_has_next>,</#if>
        </#list>
        WHERE ${pojoIdName}=${r'#'}{${pojoIdName}}
    </update>

    <select id="queryById" parameterType="long" <#if haveFk>resultMap="defaultMap"<#else>resultType="${pojoName}"</#if>>
        SELECT * FROM ${pojoSimpleName} WHERE ${pojoIdName}=${r'#'}{id}
    </select>

    <select id="queryAll" <#if haveFk>resultMap="defaultMap"<#else>resultType="${pojoName}"</#if>>
        SELECT * FROM ${pojoSimpleName}
    </select>

    <select id="queryAllLimit" <#if haveFk>resultMap="defaultMap"<#else>resultType="${pojoName}"</#if>>
        SELECT * FROM (SELECT full_result_set.*,rownum rn FROM (

        SELECT * FROM ${pojoSimpleName}

        ) full_result_set WHERE rownum<![CDATA[<=]]>${r'$'}{offset + rowCount}
        ) WHERE rn>${r'$'}{offset}
    </select>

    <select id="queryAllCount" resultType="int">
        SELECT count(1) FROM ${pojoSimpleName}
    </select>

    <select id="query" <#if haveFk>resultMap="defaultMap"<#else>resultType="${pojoName}"</#if>>
        SELECT * FROM ${pojoSimpleName}
        <include refid="whereSql"/>
    </select>

    <select id="queryLimit" parameterType="${pojoName}" <#if haveFk>resultMap="defaultMap"<#else>resultType="${pojoName}"</#if>>
        SELECT * FROM (SELECT full_result_set.*,rownum rn FROM (

        SELECT * FROM ${pojoSimpleName}
        <where>
        <#list fields as field>
            <if test="entity.${field.name}!=null">
                AND ${field.name}=${r'#'}{entity.${field.name}<#if field.type==2>.${field.fkIdName}</#if>}
            </if>
        </#list>
        </where>

        ) full_result_set WHERE rownum<![CDATA[<=]]>${r'$'}{offset + rowCount}
        ) WHERE rn>${r'$'}{offset}
    </select>

    <select id="queryCount" resultType="int">
        SELECT count(1) FROM ${pojoSimpleName}
        <include refid="whereSql"/>
    </select>

</mapper>