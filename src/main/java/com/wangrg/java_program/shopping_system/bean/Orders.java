package com.wangrg.java_program.shopping_system.bean;

import com.wangrg.db.basis.Action;
import com.wangrg.db.basis.Constraint;
import com.wangrg.db.basis.ConstraintAnno;
import com.wangrg.db.basis.FieldType;
import com.wangrg.db.basis.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 订单
 */
public class Orders {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int orderId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "User", foreignField = "userId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int userId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "Good", foreignField = "goodId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int goodId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.DEFAULT, defaultValue = "1")
    private int count;//购买数量

    @TypeAnno(type = FieldType.VARCHAR_20)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private String createDate;

    @TypeAnno(type = FieldType.VARCHAR_20)
    private String finishDate;//订单生成时为空，客户收货后设置。可以判断订单是否结束

    public Orders() {
    }

    public Orders(int userId, int goodId, int count, String createDate) {
        this.userId = userId;
        this.goodId = goodId;
        this.count = count;
        this.createDate = createDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
