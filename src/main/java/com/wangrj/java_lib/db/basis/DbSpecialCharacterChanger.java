package com.wangrj.java_lib.db.basis;

/**
 * by wangrongjun on 2017/2/2.
 */
public class DbSpecialCharacterChanger {

    public String encode(String word) {
        return word.replace("\\", "\\\\");
    }

    public String decode(String word) {
        return word;
    }

}
