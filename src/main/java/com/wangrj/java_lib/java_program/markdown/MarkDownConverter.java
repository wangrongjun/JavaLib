package com.wangrj.java_lib.java_program.markdown;

import com.wangrj.java_lib.java_util.FileUtil;

import java.util.List;
import java.util.regex.Matcher;

/**
 * by wangrongjun on 2017/8/6.
 */

public class MarkDownConverter {

    public static void main(String[] args) throws Exception {
        String markdown = FileUtil.read("E:/a.txt", "gbk");
        System.out.println(markdown);
        System.out.println(convert(markdown));
    }

    /**
     * 要点：1. \r\n\r\n作为分隔符分隔成多个文本单元
     */
    public static String convert(String markdown) {
        String html = "";
        List<MarkDownRule> ruleList = MarkDownRule.getMarkDownRuleList();
        assert markdown != null;
        String[] lines = markdown.split("\r\n\r\n");
        for (String line : lines) {
//            line = line.replace("\r", "").replace("\n", "");
            for (MarkDownRule rule : ruleList) {
                Matcher matcher = rule.getPattern().matcher(line);

                int findCount = 0;
                int findIndex = 0;
                while (matcher.find()) {
                    findCount++;
                }
                matcher.reset();

                while (matcher.find()) {
                    String[] groups = new String[matcher.groupCount() + 1];
                    for (int i = 0; i <= matcher.groupCount(); i++) {
                        groups[i] = matcher.group(i);
                    }
                    if (rule.getContext().isMatchCompletely()) {
                        // 文本要求完全匹配正则表达式（必须从文本的起始位置开始匹配，只匹配一次）
                        if (matcher.matches()) {// 匹配成功，文本完全替换
                            line = rule.getConverter().convert(groups, findIndex, findCount);
                        }
                        break;// 无论是否匹配成功，只匹配一次，所以要退出
                    } else {
                        // 文本不要求完全匹配正则表达式（不用从文本的起始位置开始匹配，可匹配多次）
                        line = line.replace(groups[0],
                                rule.getConverter().convert(groups, findIndex++, findCount));
                    }
                }
            }
            html += line + "\r\n\r\n";
        }
        return html;
    }

}
