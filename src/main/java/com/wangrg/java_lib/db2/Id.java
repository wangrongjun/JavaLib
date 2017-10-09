package com.wangrg.java_lib.db2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2017/6/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    boolean autoIncrement() default true;
}
