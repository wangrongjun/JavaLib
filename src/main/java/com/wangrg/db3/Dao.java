package com.wangrg.db3;

import com.wangrg.db2.Query;
import com.wangrg.db2.Where;

import java.util.List;

public interface Dao<T> {

    boolean createTable();

    boolean dropTable();

    boolean insert(T entity);

    boolean delete(Where where);

    boolean deleteById(long id);

    boolean deleteAll();

    boolean update(T entity);

    T queryById(long id);

    List<T> queryAll();

    List<T> query(Where where);

    /**
     * 参数Query相比于Where有更多的功能，比如分页，按字段排序，限制递归查询外键对象的深度
     */
    List<T> query(Query query);

    /**
     * 查询当前数据表的符合where条件的记录的数量
     */
    int queryCount(Where where);

}
