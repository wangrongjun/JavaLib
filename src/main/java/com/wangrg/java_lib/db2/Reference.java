package com.wangrg.java_lib.db2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2017/6/14.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Reference {
    enum Action {
        NO_ACTION,
        CASCADE,
        SET_NULL,
    }

    Action onDeleteAction() default Action.NO_ACTION;

    Action onUpdateAction() default Action.NO_ACTION;

}
