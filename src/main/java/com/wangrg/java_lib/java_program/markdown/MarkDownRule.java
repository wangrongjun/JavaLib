package com.wangrg.java_lib.java_program.markdown;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/8/7.
 */

class MarkDownRule {

    private Pattern pattern;
    private Context context;
    private Converter converter;

    public MarkDownRule(Pattern pattern, Converter converter, Context context) {
        this.pattern = pattern;
        this.converter = converter;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public interface Converter {
        String convert(String[] groups, int findIndex, int findCount);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Converter getConverter() {
        return converter;
    }

    public static class Context {
        // 文本是否要求完全匹配正则表达式（必须从文本的起始位置开始匹配，只匹配一次），比如匹配标题
        private boolean matchCompletely = true;

        public Context setMatchCompletely(boolean matchCompletely) {
            this.matchCompletely = matchCompletely;
            return this;
        }

        public boolean isMatchCompletely() {
            return matchCompletely;
        }
    }

    public static List<MarkDownRule> getMarkDownRuleList() {
        List<MarkDownRule> ruleList = new ArrayList<>();

        // 标题
        ruleList.add(new MarkDownRule(Pattern.compile("([#]+)([^#]+[\\d\\D]*)"), new Converter() {
            @Override
            public String convert(String[] groups, int findIndex, int findCount) {
                if (!groups[0].startsWith("#")) {
                    return groups[0];
                }
                int titleLevel = groups[1].length();
                return "<h" + titleLevel + ">" + groups[2].trim() + "</h" + titleLevel + ">";
            }
        }, new Context()));

        // 分隔线-
        ruleList.add(new MarkDownRule(Pattern.compile("-{3,}"), new Converter() {
            @Override
            public String convert(String[] groups, int findIndex, int findCount) {
                return "<hr>";
            }
        }, new Context()));

        // 分隔线*
        ruleList.add(new MarkDownRule(Pattern.compile("\\*{3,}"), new Converter() {
            @Override
            public String convert(String[] groups, int findIndex, int findCount) {
                return "<hr>";
            }
        }, new Context()));

        // 加粗
        ruleList.add(new MarkDownRule(Pattern.compile("\\*\\*([^*\n]+)\\*\\*"), new Converter() {
            @Override
            public String convert(String[] groups, int findIndex, int findCount) {
                return "<strong>" + groups[1] + "</strong>";
            }
        }, new Context().setMatchCompletely(false)));//一行里可能有多个，所有要匹配多次

        // 斜体
        ruleList.add(new MarkDownRule(Pattern.compile("\\*([^ ][^*]+)\\*"), new Converter() {
            @Override
            public String convert(String[] groups, int findIndex, int findCount) {
                if (groups[1].endsWith(" ")) {// groups[1]="ab "
                    return groups[0];// groups[0]="*ab *"
                }
                return "<i>" + groups[1] + "</i>";
            }
        }, new Context().setMatchCompletely(false)));//一行里可能有多个，所有要匹配多次

        // 无序列表
        ruleList.add(new MarkDownRule(Pattern.compile("\\* ([^*]+)[\r\n]?"), new Converter() {
            @Override
            public String convert(String[] groups, int findIndex, int findCount) {
                return (findIndex == 0 ? "<ul>" : "") +
                        "<li>" + groups[1] + "</li>" +
                        (findIndex == findCount - 1 ? "</ul>" : "");
            }
        }, new Context().setMatchCompletely(false)));//一行里可能有多个，所有要匹配多次

        return ruleList;
    }

}
