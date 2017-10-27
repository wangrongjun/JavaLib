package com.wangrj.java_lib.web;

/**
 * Created by Administrator on 2016/3/9.
 */
public class JsonResult {
    //    state: 0：操作成功  -1：操作失败  -2：服务器捕获异常
    public static final int OK = 0;
    public static final int ERROR = -1;
    public static final int EXCEPTION = -2;

    private int state = OK;
    private String result;

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}