package com.wangrj.java_lib.java_program.video_download.website;


import com.wangrj.java_lib.java_program.video_download.bean.Course;
import com.wangrj.java_lib.java_program.video_download.bean.CourseDataFile;
import com.wangrj.java_lib.java_program.video_download.bean.Video;
import com.wangrj.java_lib.java_util.HttpRequest;
import com.wangrj.java_lib.java_util.TextUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JiKeXueYuan {

    public static final String url = "http://www.jikexueyuan.com";

    // 获取课程资料的url
    private static final String courseDataFileUrl = "http://www.jikexueyuan.com/course/downloadRes?course_id=";

    private String courseUrl;
    private String cookie;

    public JiKeXueYuan(String courseUrl, String cookie) {
        this.courseUrl = courseUrl;
        this.cookie = cookie;
    }

    public Course getCourse() throws Exception {
        String firefoxUserAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) " +
                "Gecko/20080404 Firefox/2.0.0.14";
        HttpRequest.Response response = new HttpRequest().
                setCookie(cookie).
                setUserAgent(firefoxUserAgent).
                request(courseUrl);
        return parseHtml(response.toResponseText());
    }

    private Course parseHtml(String html) throws Exception {
        // 获取课程名称
        String courseName = TextUtil.correctFileName(getCourseName(html), "_");

        // 获取该课程每一节视频的标题
        List<String> videoTitles = getVideoTitles(html);
        int len = videoTitles.size();

        for (int i = 0; i < len; i++) {
            String s = TextUtil.correctFileName(videoTitles.get(i), "_");
            videoTitles.add(s);
        }

        // 先获取课程页面的视频（也就是第一个视频）的Url
        List<String> videoPageUrl = new ArrayList<>();
        List<String> videoRealUrl = new ArrayList<>();
        videoPageUrl.add(courseUrl);
        videoRealUrl.add(getVideoUrl(html));

        // 获取第2，3，4...个视频的Url
        for (int i = 1; i < len; i++) {
            String nextUrl = courseUrl.replace(".html", "_" + (i + 1) + ".html");
            videoPageUrl.add(nextUrl);
            HttpRequest.Response response = null;
            try {
                response = new HttpRequest().setCookie(cookie).request(nextUrl);
                videoRealUrl.add(getVideoUrl(response.toResponseText()));
            } catch (Exception e) {
                throw new Exception("nextUrl的html获取失败。nextUrl : " + nextUrl, e);
            }
        }

        // 获取课程资料文件的名字和url
        String courseId;
        CourseDataFile courseDataFile = null;
        int i = courseUrl.indexOf("course/") + 7;
        int j = courseUrl.indexOf(".html");
        if (i != -1 && j != -1) {
            courseId = courseUrl.substring(i, j);
            courseDataFile = getCourseDataFile(courseId);
        }

        // 最后把所有信息汇总到course对象
        Course course = new Course();
        course.setCourseName(courseName);
        List<CourseDataFile> courseDataFiles = new ArrayList<>();
        courseDataFiles.add(courseDataFile);
        course.setCourseDataFiles(courseDataFiles);
        List<Video> videos = new ArrayList<>();
        for (i = 0; i < len; i++) {
            Video video = new Video();
            video.setTitle(videoTitles.get(i) + ".mp4");
            video.setPageUrl(videoPageUrl.get(i));
            video.setRealUrl(videoRealUrl.get(i));
            videos.add(video);
        }
        course.setVideos(videos);

        return course;
    }

    // 获取课程名称
    private String getCourseName(String html) {
        int i = html.indexOf("<title>");
        int j = html.indexOf("</title>");
        return html.substring(i + 7, j);
    }

    // 获取该课程每一节视频的标题
    private List<String> getVideoTitles(String html) {
        List<String> videoTitles = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements list = doc.getElementsByClass("lessonvideo-list");
        list = list.select("a[href]");

        for (int i = 0; i < list.size(); i++) {
            videoTitles.add(list.get(i).text());
        }

        return videoTitles;
    }

    // 获取该网页的视频的url
    private String getVideoUrl(String html) {
        int i = html.indexOf("source src");
        int j = html.indexOf("\" type=\"video");
        System.out.println("开始获取url");
        System.out.println("视频url： " + html.substring(i + 12, j));
        return html.substring(i + 12, j);
    }

    private CourseDataFile getCourseDataFile(String courseId) throws Exception {

        HttpRequest.Response response = new HttpRequest().setCookie(cookie).request(courseDataFileUrl + courseId);
        String json = response.toResponseText();
        String name;
        String url;
        // 先通过i,j获取资料文件的url
        int i = json.indexOf("http");
        int j = json.indexOf("\"}");
        if (i != -1 && j != -1) {
            url = json.substring(i, j);

        } else {
            return null;
        }

        // 再通过i,j获取资料文件名
        String postName = ".zip";
        i = json.indexOf("file/") + 5;
        j = json.indexOf(postName);
        if (j == -1) {
            postName = ".rar";
            j = json.indexOf(postName);
        }

        if (i != -1 && j != -1) {
            name = json.substring(i, j) + postName;

        } else {
            return null;
        }

        return new CourseDataFile(TextUtil.correctFileName(name, "_"), url);
    }

}
