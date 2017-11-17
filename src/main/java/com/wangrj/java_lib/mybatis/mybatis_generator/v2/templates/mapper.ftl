<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${daoName}">

    <#-- 由于resultMap的子标签必须按照id,result,association的顺序，所以只能分成两个循环 -->
    <resultMap id="singleMap" type="${pojoName}">
    <#list fields as field>
        <#if field.type==0>
        <result column="${field.columnName}" property="${field.propertyName}"/>
        </#if>
        <#if field.type==1>
        <id column="${field.columnName}" property="${field.propertyName}"/>
        </#if>
    </#list>
    <#list fields as field>
        <#if field.type==2>
        <association property="${field.propertyName}" javaType="${field.fkClassName}">
            <id column="${field.columnName}" property="${field.fkIdName}"/>
        </association>
        </#if>
    </#list>
    </resultMap>

    <resultMap id="multiMap" type="${pojoName}" extends="singleMap"/>

</mapper>