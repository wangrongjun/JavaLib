package com.wangrj.java_lib.java_program.shopping_system.dao;

import com.wangrj.java_lib.java_program.shopping_system.bean.Comment;

/**
 * by wangrongjun on 2016/12/16.
 */
public class CommentDao extends CustomDao<Comment> {
    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

    @Override
    protected boolean isPrintSql() {
        return false;
    }
}
