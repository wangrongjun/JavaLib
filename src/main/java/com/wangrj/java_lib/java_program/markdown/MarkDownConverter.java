package com.wangrj.java_lib.java_program.markdown;

import com.wangrj.java_lib.java_util.FileUtil;
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
     * Ҫ�㣺1. \r\n\r\n��Ϊ�ָ����ָ��ɶ���ı���Ԫ
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
                        // �ı�Ҫ����ȫƥ��������ʽ��������ı�����ʼλ�ÿ�ʼƥ�䣬ֻƥ��һ�Σ�
                        if (matcher.matches()) {// ƥ��ɹ����ı���ȫ�滻
                            line = rule.getConverter().convert(groups, findIndex, findCount);
                        }
                        break;// �����Ƿ�ƥ��ɹ���ֻƥ��һ�Σ�����Ҫ�˳�
                    } else {
                        // �ı���Ҫ����ȫƥ��������ʽ�����ô��ı�����ʼλ�ÿ�ʼƥ�䣬��ƥ���Σ�
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
