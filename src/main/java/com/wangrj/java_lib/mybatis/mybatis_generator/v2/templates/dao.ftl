package ${daoPackage};

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import ${pojoName};

<#if springAnno>
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

@MapperScan
@Repository
</#if>
public interface ${daoSimpleName} {

    @SelectProvider(type = Builder.class, method = "queryByIdSql")
    <#if haveFk>@ResultMap("singleMap")</#if><#if !haveFk>@ResultType(${pojoSimpleName}.class)</#if>
    ${pojoSimpleName} queryById(${pojoIdType} ${pojoIdName});

    @SelectProvider(type = Builder.class, method = "queryAllSql")
    <#if haveFk>@ResultMap("multiMap")</#if><#if !haveFk>@ResultType(${pojoSimpleName}.class)</#if>
    List<${pojoSimpleName}> queryAll(@Param("orderByList") String... orderByList);

    @SelectProvider(type = Builder.class, method = "querySql")
    <#if haveFk>@ResultMap("multiMap")</#if><#if !haveFk>@ResultType(${pojoSimpleName}.class)</#if>
    List<${pojoSimpleName}> query(@Param("${pojoVarName}") ${pojoSimpleName} ${pojoVarName}, @Param("orderByList") String... orderByList);

    @SelectProvider(type = Builder.class, method = "queryAllCountSql")
    int queryAllCount();

    @SelectProvider(type = Builder.class, method = "queryCountSql")
    int queryCount(${pojoSimpleName} ${pojoVarName});

    @InsertProvider(type = Builder.class, method = "insertSql")
    // MySQL自增主键的配置：@Options(useGeneratedKeys = true, keyProperty = "userId")
    // 如果Oracle不是使用触发器生成id，可以把currval改为nextval，同时设置before=true
    <#if insertReturnKeyAnno==0>
    @SelectKey(statement = "SELECT sequence_${tableName}.currval FROM dual", keyProperty = "${pojoIdName}",
            before = false, resultType = int.class)
    </#if>
    <#if insertReturnKeyAnno==1>@Options(useGeneratedKeys = true, keyProperty = "${pojoIdName}")</#if>
    int insert(${pojoSimpleName} ${pojoVarName});

    @DeleteProvider(type = Builder.class, method = "deleteSql")
    int deleteById(${pojoIdType} ${pojoIdName});

    /**
     * 更新时忽略空值
     */
    @UpdateProvider(type = Builder.class, method = "updateSql")
    int update(${pojoSimpleName} ${pojoVarName});

    /**
     * 空值也会更新
     * 注意：必须在全局配置中设置<setting username="jdbcTypeForNull" value="NULL"/>，否则报错
     */
    @UpdateProvider(type = Builder.class, method = "updateContainsNullSql")
    int updateContainsNull(${pojoSimpleName} ${pojoVarName});

    class Builder {
        public String queryByIdSql(Integer userId) {
            return new SQL() {{
                SELECT("*");
                FROM("${tableName}");
                WHERE("${pojoIdColumnName}=${r'#'}{id}");
            }}.toString();
        }

        public String queryAllSql(@Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("${tableName}");
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String querySql(@Param("${pojoVarName}") ${pojoSimpleName} ${pojoVarName}, @Param("orderByList") String... orderByList) {
            return new SQL() {{
                SELECT("*");
                FROM("${tableName}");
                <#list fields as field>
                if (${pojoVarName}.${field.getter}() != null) WHERE("${field.columnName}=${r'#'}{${pojoVarName}.${field.propertyName}<#if field.type==2>.${field.fkIdName}</#if>}");
                </#list>
                ORDER_BY(orderByList);
            }}.toString();
        }

        public String queryAllCountSql() {
            return new SQL() {{
                SELECT("count(1)");
                FROM("${tableName}");
            }}.toString();
        }

        public String queryCountSql(${pojoSimpleName} ${pojoVarName}) {
            return new SQL() {{
                SELECT("count(1)");
                FROM("${tableName}");
                <#list fields as field>
                if (${pojoVarName}.${field.getter}() != null) WHERE("${field.columnName}=${r'#'}{${field.propertyName}<#if field.type==2>.${field.fkIdName}</#if>}");
                </#list>
            }}.toString();
        }

        public String insertSql() {
            return new SQL() {{
                INSERT_INTO("${tableName}");
                INTO_COLUMNS(
                <#list fields as field>
                <#if field.type!=3 && (field.type!=1 || !autoIncrement)>"${field.columnName}"<#if field_has_next>, </#if></#if>
                </#list>
                );
                INTO_VALUES(
                <#list fields as field>
                <#if field.type!=3 && (field.type!=1 || !autoIncrement)>"${r'#'}{${field.propertyName}<#if field.type==2>.${field.fkIdName}</#if>}"<#if field_has_next>, </#if></#if>
                </#list>
                );
            }}.toString();
        }

        public String deleteSql() {
            return new SQL() {{
                DELETE_FROM("${tableName}");
                WHERE("${pojoIdColumnName} = ${r'#'}{id}");
            }}.toString();
        }

        public String updateSql(${pojoSimpleName} ${pojoVarName}) {
            return new SQL() {{
                UPDATE("${tableName}");
                <#list fields as field>
                    <#if field.type!=1 && field.type!=3>
                    if (${pojoVarName}.${field.getter}() != null) SET("${field.columnName}=${r'#'}{${field.propertyName}<#if field.type==2>.${field.fkIdName}</#if>}");
                    </#if>
                </#list>
                WHERE("${pojoIdColumnName}=${r'#'}{${pojoIdName}}");
            }}.toString();
        }

        public String updateContainsNullSql() {
            return new SQL() {{
                UPDATE("${tableName}");
                SET(
                <#list fields as field>
                    <#if field.type!=1 && field.type!=3>
                        "${field.columnName}=${r'#'}{${field.propertyName}<#if field.type==2>.${field.fkIdName}</#if>}"<#if field_has_next>, </#if>
                    </#if>
                </#list>
                );
                WHERE("${pojoIdColumnName}=${r'#'}{${pojoIdName}}");
            }}.toString();
        }
    }

}
