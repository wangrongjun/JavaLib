package com.wangrj.java_lib.java_program.shopping_system.bean;

import com.wangrj.java_lib.db.basis.Action;
import com.wangrj.java_lib.db.basis.Constraint;
import com.wangrj.java_lib.db.basis.ConstraintAnno;
import com.wangrj.java_lib.db.basis.FieldType;
import com.wangrj.java_lib.db.basis.TypeAnno;

/**
 * by wangrongjun on 2016/12/15.
 * 商品
 */
public class Good {

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.PRIMARY_KEY)
    private int goodId;

    @TypeAnno(type = FieldType.INT)
    @ConstraintAnno(constraint = Constraint.FOREIGN_KEY,
            foreignTable = "Shop", foreignField = "shopId",
            onDeleteAction = Action.CASCADE, onUpdateAction = Action.CASCADE)
    private int shopId;

    @TypeAnno(type = FieldType.VARCHAR_50)
    @ConstraintAnno(constraint = Constraint.UNIQUE_NOT_NULL)
    private String goodName;

    @TypeAnno(type = FieldType.VARCHAR_500)
    private String description;

    @TypeAnno(type = FieldType.DOUBLE)
    @ConstraintAnno(constraint = Constraint.NOT_NULL)
    private double price;

    public Good() {
    }

    public Good(int shopId, String goodName, String description, int price) {
        this.shopId = shopId;
        this.goodName = goodName;
        this.description = description;
        this.price = price;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
