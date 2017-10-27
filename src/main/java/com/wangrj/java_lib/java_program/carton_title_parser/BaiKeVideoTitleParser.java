package com.wangrj.java_lib.java_program.carton_title_parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * by ���ٿ� on 2016/7/26.
 * http://www.jb51.net/article/43485.htm  Jsoup����HTMLʵ�����ĵ��������
 */
public class BaiKeVideoTitleParser {

    public interface TitleHandler {
        String updateTitle(String title);
    }

    /**
     * ������������
     *
     * @param column ����������е��±꣬0��1��2��3��4��
     */
    public static String parse1(Document document, int column, TitleHandler handler) throws Exception {
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
     * �����ּ�����
     */
    public static String parse2(Document document, TitleHandler handler) throws Exception {
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
