package com.wangrj.java_lib.java_util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TextUtilTest {

    @Test
    public void isBlank() {
        assertTrue(TextUtil.isBlank(null));
        assertTrue(TextUtil.isBlank(""));
        assertTrue(TextUtil.isBlank("\t"));
        assertTrue(TextUtil.isBlank("\n"));
        assertTrue(TextUtil.isBlank("\f"));
        assertTrue(TextUtil.isBlank("\r"));
        assertTrue(TextUtil.isBlank("\r\n"));
        assertTrue(TextUtil.isBlank(" "));
        assertTrue(TextUtil.isBlank(" \n"));
        assertTrue(TextUtil.isBlank("\n \n"));
        assertTrue(TextUtil.isBlank("\r\n \r\n"));

        assertFalse(TextUtil.isBlank("a"));
        assertFalse(TextUtil.isBlank("a\n"));
        assertFalse(TextUtil.isBlank("\na\n"));
        assertFalse(TextUtil.isBlank("\r\na\r\n"));
    }

}