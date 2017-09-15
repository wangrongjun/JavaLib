package com.wangrg.java_program.shopping_system.bean;

import com.wangrg.db.basis.Action;
import com.wangrg.db.basis.Constraint;
import com.wangrg.db.basis.ConstraintAnno;
import com.wangrg.db.basis.FieldType;
import com.wangrg.db.basis.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 商铺
 */
public class Shop {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int shopId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "User", foreignField = "userId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int ownerId;

    @TypeAnno(type = FieldType.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.UNIQUE_NOT_NULL)
    private String shopName;

    public Shop() {
    }

    public Shop(int ownerId, String shopName) {
        this.ownerId = ownerId;
        this.shopName = shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
