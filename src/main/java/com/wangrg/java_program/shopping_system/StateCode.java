package com.wangrg.java_program.shopping_system;

/**
 * by wangrongjun on 2016/12/6.
 */
public enum StateCode {

    OK,

    /**
     * 参数错误（空，超出范围，格式错误等，一般情况下由客户端在网络访问之前即可判断出来）
     */
    PARAM_ERROR,
    /**
     * 网络或服务器访问失败
     */
    INTERNET_UNABLE,
    /**
     * 程序出错
     */
    APP_ERROR,

    /**
     * 服务器：权限错误
     */
    ERROR_ILLEGAL,
    /**
     * 服务器：一般业务错误
     */
    ERROR_NORMAL,
    /**
     * 服务器：存储错误
     */
    ERROR_STORAGE,
    /**
     * 服务器：未知错误
     */
    ERROR_UNKNOWN
}
