package com.wangrg.db.basis;

/**
 * 建表的字段
 */
public class TableField {

    public String name;
    public FieldType type;
    public boolean primaryKey = false;
    public boolean unsigned = false;
    public boolean notNull = false;
    public boolean unique = false;
    public TableValue defaultValue;

    public TableField(String name, FieldType type) {
        this.name = name;
        this.type = type;
    }

    public TableField primaryKey() {
        primaryKey = true;
        return this;
    }

    public TableField unsigned() {
        unsigned = true;
        return this;
    }

    public TableField notNull() {
        notNull = true;
        return this;
    }

    public TableField unique() {
        unique = true;
        return this;
    }

    /**
     * 该方法会先判断value的类型，若为字符串，则自动添加单引号
     */
    public TableField defaultValue(TableValue defaultValue) {
        switch (defaultValue.type) {
            case TEXT:
                defaultValue.value = "'" + defaultValue.value + "'";
                break;
            case INT:
            case DOUBLE:
                break;
        }
        this.defaultValue = defaultValue;
        return this;
    }

}
