package com.wangrj.java_lib.java_util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * by wangrongjun on 2019/2/5.
 */
public class BinaryUtilTest {

    @Test
    public void testGetBitValue() {
        byte date = (byte) 0b10101010;
        assertEquals(BinaryUtil.getBitValue(date, 0, 0), 0);// 0b0
        assertEquals(BinaryUtil.getBitValue(date, 0, 1), 2);// 0b10
        assertEquals(BinaryUtil.getBitValue(date, 0, 2), 2);// 0b010
        assertEquals(BinaryUtil.getBitValue(date, 0, 3), 10);// 0b1010
        assertEquals(BinaryUtil.getBitValue(date, 1, 1), 1);// 0b1
        assertEquals(BinaryUtil.getBitValue(date, 1, 2), 1);// 0b01
        assertEquals(BinaryUtil.getBitValue(date, 1, 3), 5);// 0b101
        assertEquals(BinaryUtil.getBitValue(date, 1, 4), 5);// 0b0101
        assertEquals(BinaryUtil.getBitValue(date, 1, 5), 21);// 0b10101
    }

}