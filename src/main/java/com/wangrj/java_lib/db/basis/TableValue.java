package com.wangrj.java_lib.db.basis;

/**
 * by 王荣俊 on 2016/5/12.
 */
public class TableValue {

    public String name;
    public ValueType type;
    public String value;

    /**
     * 作为defaultValue时才用到
     */
    public TableValue(ValueType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * 作为defaultValue时才用到
     */
    public TableValue(FieldType type, String value) {
        this.type = toValueType(type);
        this.value = value;
    }

    public TableValue(String name, ValueType type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public TableValue(String name, FieldType type, String value) {
        this.name = name;
        this.type = toValueType(type);
        this.value = value;
    }

    public static ValueType toValueType(FieldType fieldType) {
        switch (fieldType) {
            case TINYINT:
            case INT:
                return ValueType.INT;
            case DOUBLE:
                return ValueType.DOUBLE;
            case VARCHAR_10:
            case VARCHAR_20:
            case VARCHAR_50:
            case VARCHAR_100:
            case VARCHAR_500:
            case TEXT:
                return ValueType.TEXT;
        }
        return null;
    }

}
