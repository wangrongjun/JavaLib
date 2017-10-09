package com.wangrg.java_lib.db2;

/**
 * by wangrongjun on 2017/6/17.
 */

public interface InputValueFormatter {

    String format(String inputValue);

    /**
     * 防止输入的值中含有非法字符如单引号，斜杠等。防止sql注入攻击。
     * 原理：单引号中间的内容有可能引起歧义的只有内容里的单引号和斜杠。
     * 只要对内容里的单引号和斜杠进行转义，理论上可以防止任意方式的sql注入攻击。
     */
    class InputValueFormatterImpl implements InputValueFormatter {
        @Override
        public String format(String value) {
            return value.replace("\\", "\\\\").replace("'", "\\'");
//            return value;
        }
    }

}
