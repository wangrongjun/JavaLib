package com.wangrj.java_lib.demo.calculator.bean;

import java.io.Serializable;

public class Sign implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String sign;
    private int operateNum;
    private int prior;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getOperateNum() {
        return operateNum;
    }

    public void setOperateNum(int operateNum) {
        this.operateNum = operateNum;
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
