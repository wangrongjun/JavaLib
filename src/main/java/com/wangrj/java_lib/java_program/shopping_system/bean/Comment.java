package com.wangrj.java_lib.java_program.shopping_system.bean;

import com.wangrj.java_lib.db.basis.Constraint;
import com.wangrj.java_lib.db.basis.ConstraintAnno;
import com.wangrj.java_lib.db.basis.FieldType;
import com.wangrj.java_lib.db.basis.TypeAnno;
import com.wangrj.java_lib.db.basis.FieldType;

/**
 * by wangrongjun on 2016/12/15.
 * 评论
 */
public class Comment {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int commentId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "User", foreignField = "userId")
    private int userId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "Good", foreignField = "goodId")
    private int goodId;

    @TypeAnno(type = FieldType.VARCHAR_500)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String content;

    public Comment(int userId, int goodId, String content) {
        this.userId = userId;
        this.goodId = goodId;
        this.content = content;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
