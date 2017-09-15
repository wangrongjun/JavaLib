package com.wangrg.java_program.markdown;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * by wangrongjun on 2017/8/7.
 */

public class MarkDownTest {

    private static void assertMarkDown(String markdown, String expectedHtml) {
        String actualHtml = MarkDownConverter.convert(markdown).replace("\r", "").replace("\n", "");
        assertEquals(expectedHtml, actualHtml);
    }

    @Test
    public void testTitle() {
        assertMarkDown("##abc", "<h2>abc</h2>");
        assertMarkDown("##abc ##", "<h2>abc ##</h2>");
        assertMarkDown("## abc ### ", "<h2>abc ###</h2>");

        assertMarkDown("a ## abc ###", "a ## abc ###");
    }

    @Test
    public void testDivider1() {
        assertMarkDown("---", "<hr>");
        assertMarkDown("------", "<hr>");

        assertMarkDown("--", "--");
        assertMarkDown("---a", "---a");
        assertMarkDown("a---", "a---");
    }

    @Test
    public void testDivider2() {
        assertMarkDown("***", "<hr>");
        assertMarkDown("******", "<hr>");

        assertMarkDown("**", "**");
        assertMarkDown("***a", "***a");
        assertMarkDown("a***", "a***");
    }

    @Test
    public void testItalics() {
        assertMarkDown("ab*cd*ef", "ab<i>cd</i>ef");
        assertMarkDown("ab*cd*ef*", "ab<i>cd</i>ef*");

        assertMarkDown("*ab", "*ab");
        assertMarkDown("ab*cd**ef", "ab<i>cd</i>*ef");
        assertMarkDown("*ab *", "*ab *");
    }

    @Test
    public void testBold() {
        assertMarkDown("ab**cd**ef", "ab<strong>cd</strong>ef");
        assertMarkDown("ab**cd", "ab**cd");
    }

    @Test
    public void testUnorderedList() {
        assertMarkDown("* a\r\n* b", "<ul><li>a</li><li>b</li></ul>");
    }

}
