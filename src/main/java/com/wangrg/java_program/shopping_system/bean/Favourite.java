package com.wangrg.java_program.shopping_system.bean;

import com.wangrg.db.basis.Action;
import com.wangrg.db.basis.Constraint;
import com.wangrg.db.basis.ConstraintAnno;
import com.wangrg.db.basis.FieldType;
import com.wangrg.db.basis.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 收藏
 */
public class Favourite {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "Emp", foreignField = "userId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int userId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY, foreignTable = "Good", foreignField = "goodId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int goodId;

    public Favourite() {
    }

    public Favourite(int userId, int goodId) {
        this.userId = userId;
        this.goodId = goodId;
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
}
