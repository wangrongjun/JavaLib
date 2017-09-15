package com.wangrg.db2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2017/6/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    int length() default 0;

    /**
     * 小数位数（默认为0，即整数）
     */
    int decimalLength() default 0;

    boolean nullable() default true;

    boolean unique() default false;

    String defaultValue() default "";

}
