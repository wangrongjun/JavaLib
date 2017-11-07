package com.wangrj.java_lib.db3.db.sql_creator;

import com.wangrj.java_lib.data_structure.Pair;
import com.wangrj.java_lib.db2.Query;
import com.wangrj.java_lib.db2.Where;
import com.wangrj.java_lib.db3.main.TableField;
import com.wangrj.java_lib.db3.main.UpdateSetValue;

import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public interface ISqlCreator {

    List<String> createTableSql(String tableName, List<TableField> fieldTypeList,
                                List<String> unionUniqueList);

    List<String> dropTableSql(String tableName);

    String querySql(String tableName, Query query);

    String queryCountSql(String tableName, Where where);

    String queryAutoIncrementCurrentValue(String tableName);

    String insertSql(String tableName, List<Pair<String, String>> nameValuePairList);

    String updateSql(String tableName, UpdateSetValue setValue, Where where);

    String deleteSql(String tableName, Where where);

    /**
     * 自定义了sql语句，又要取消不同数据库带来的不同分页实现方式的差异时，可以使用该方法
     */
    String wrapLimit(String sql, int offset, int rowCount);

}
