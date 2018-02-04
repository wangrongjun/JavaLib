package com.wangrj.java_lib.java_program.carton_title_parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * by 王荣俊 on 2016/7/26.
 * http://www.jb51.net/article/43485.htm  Jsoup解析HTML实例及文档方法详解
 */
public class BaiKeVideoTitleParser {

    public interface TitleHandler {
        String updateTitle(String title);
    }

    /**
     * 解析各话制作
     *
     * @param column 表格中所需列的下标，0，1，2，3，4等
     */
    public static String parseTd(Document document, int column, TitleHandler handler) throws Exception {
        StringBuilder builder = new StringBuilder();
        Elements elements = document.select("tr");
        for (Element element : elements) {
            Elements tds = element.select("td");
            try {
                Element title = tds.get(column);
                String text = title.text();
                if (handler != null) {
                    text = handler.updateTitle(text);
                }
                builder.append(text).append("\n");

            } catch (Exception ignored) {
            }
        }
        return builder.toString();
    }

    /**
     * 解析分集剧情
     */
    public static String parseSpan(Document document, TitleHandler handler) throws Exception {
        StringBuilder builder = new StringBuilder();
        Elements spans = document.select("span");
        for (Element span : spans) {
            try {
                String text = span.text();
                if (handler != null) {
                    text = handler.updateTitle(text);
                }
                builder.append(text).append("\n");

            } catch (Exception ignored) {
            }
        }
        return builder.toString();
    }

}
