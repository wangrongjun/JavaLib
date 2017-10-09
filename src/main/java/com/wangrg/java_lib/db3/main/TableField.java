package com.wangrg.java_lib.db3.main;

import com.wangrg.java_lib.db2.Reference;

/**
 * by wangrongjun on 2017/8/21.
 */

public class TableField {

    private String name;
    private Type type;
    private int length;
    private int decimalLength;
    private boolean primaryKey = false;
    private boolean autoIncrement = true;// Mysql是否自增
    private boolean unique = false;
    private boolean nullable = true;
    private String defaultValue;

    private boolean foreignKey;
    private String referenceTable;
    private String referenceColumn;
    private Reference.Action onDeleteAction;
    private Reference.Action onUpdateAction;

    public enum Type {
        NUMBER_INT,
        NUMBER_LONG,
        NUMBER_FLOAT,
        NUMBER_DOUBLE,
        TEXT,
        DATE
    }

    public TableField(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDecimalLength() {
        return decimalLength;
    }

    public void setDecimalLength(int decimalLength) {
        this.decimalLength = decimalLength;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public void setReferenceTable(String referenceTable) {
        this.referenceTable = referenceTable;
    }

    public String getReferenceColumn() {
        return referenceColumn;
    }

    public void setReferenceColumn(String referenceColumn) {
        this.referenceColumn = referenceColumn;
    }

    public Reference.Action getOnDeleteAction() {
        return onDeleteAction;
    }

    public void setOnDeleteAction(Reference.Action onDeleteAction) {
        this.onDeleteAction = onDeleteAction;
    }

    public Reference.Action getOnUpdateAction() {
        return onUpdateAction;
    }

    public void setOnUpdateAction(Reference.Action onUpdateAction) {
        this.onUpdateAction = onUpdateAction;
    }
}
