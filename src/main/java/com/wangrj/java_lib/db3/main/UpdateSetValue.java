package com.wangrj.java_lib.db3.main;

import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2017/8/21.
 */

public class UpdateSetValue {

    private List<Expression> expressionList;

    public UpdateSetValue() {
        expressionList = new ArrayList<>();
    }

    public static UpdateSetValue build(String setName, String setValue) {
        return new UpdateSetValue().add(setName, setValue);
    }

    public UpdateSetValue add(String setName, String setValue) {
        expressionList.add(new Expression(setName, setValue));
        return this;
    }

    @Override
    public String toString() {
        if (expressionList.size() == 0) {
            return null;
        }
        String s = "";
        for (Expression expression : expressionList) {
            s += expression.setName + "=" + expression.setValue + ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    private class Expression {
        String setName;
        String setValue;

        Expression(String setName, String setValue) {
            this.setName = setName;
            this.setValue = setValue;
        }
    }
}
