package com.wangrg.java_lib.db.basis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * by wangrongjun on 2016/12/29.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintAnno {

    Constraint constraint() default Constraint.NULL;

    String defaultValue() default "0";

    String foreignTable() default "0";

    String foreignField() default "0";

    Action onDeleteAction() default Action.NO_ACTION;

    Action onUpdateAction() default Action.NO_ACTION;

}
