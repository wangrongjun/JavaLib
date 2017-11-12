package com.wangrj.java_lib.mybatis.mybatis_generator.v1;

import org.apache.ibatis.annotations.Param;

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

    List<T> queryAllLimit(@Param("offset") int offset, @Param("rowCount") int rowCount);

    int queryAllCount();

    List<T> query(T entity);

    List<T> queryLimit(@Param("entity") T entity, @Param("offset") int offset, @Param("rowCount") int rowCount);

    int queryCount(T entity);

}
