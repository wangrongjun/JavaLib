package com.wangrj.java_lib.mybatis;

import java.util.List;

/**
 * by wangrongjun on 2017/11/4.
 */
public interface MybatisDao<T> {

    boolean insert(T entity);

    boolean deleteById(long id);

    /**
     * 更新时忽略空值
     */
    boolean update(T entity);

    /**
     * 空值也会更新
     */
    boolean updateContainsNull(T entity);

    T queryById(long id);

    List<T> queryAll();

    List<T> queryAllLimit(int begin, int end);

    int queryAllCount();

    List<T> query(T entity);

    List<T> queryLimit(T entity, int begin, int end);

    int queryCount(T entity);

}
