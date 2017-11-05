package com.wangrj.java_lib.test.crawl_site;

import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.HttpUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/10/30.
 */
public class CrawlSiteRunClass {

    public static void main(String[] args) {
//        HttpUtil.Result r = get("http://www.runoob.com/kotlin/kotlin-tutorial.html");
//        if (r.responseCode != HttpUtil.OK) {
//            throw new RuntimeException(r.responseCode + " : " + r.result);
//        }
//        String html = r.result;
        String html = FileUtil.read("E:/a.html");
//        String html = "<script src=\"//cdn.bootcss.com/jquery/2.0.3/jquery.min.js\"></script>  " +
//                "<script src=\"//cdn.bootcss.com/jquery/2.0.3/jquery.min.js\"/>  " +
//                "<script src=\"//cdn.bootcss.com/jquery/2.0.3/jquery.min.js\">";
//         匹配link标签
        Pattern linkPattern = Pattern.compile("<link.+?href=\"([^\"]+)\".*?>");
        Matcher matcher = linkPattern.matcher(html);
        while (matcher.find()) {
            System.out.println(html.substring(matcher.start(), matcher.end()));
            if (html.substring(matcher.end(), matcher.end() + 7).equals("</link>")) {
                System.out.println("</link>");
            }
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            System.out.println("--------------------------------");
        }

        // 匹配script标签
        Pattern scriptPattern = Pattern.compile("<script.+?src=\"([^\"]+)\".*?>");
        matcher = scriptPattern.matcher(html);
        while (matcher.find()) {
            System.out.println(html.substring(matcher.start(), matcher.end()));
            if (html.substring(matcher.end(), matcher.end() + 9).equals("</script>")) {
                System.out.println("</script>");
            }
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            System.out.println("--------------------------------");
        }
    }

    private static HttpUtil.Result get(String url) {
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest();
        request.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        return request.request(url);
    }

}
