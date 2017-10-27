package com.wangrj.java_lib.db2;

import java.util.List;

public interface Dao<T> {

    default boolean createTable() {
        return false;
    }

    default boolean dropTable() {
        return false;
    }

    default boolean createForeignKey() {
        return false;
    }

    boolean insert(T entity);

    boolean delete(Where where);

    boolean deleteById(long id);

    default boolean deleteAll() {
        return false;
    }

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
