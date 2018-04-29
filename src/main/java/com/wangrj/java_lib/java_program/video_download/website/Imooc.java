package com.wangrj.java_lib.java_program.video_download.website;

import com.wangrj.java_lib.java_program.video_download.bean.Course;
import com.wangrj.java_lib.java_program.video_download.bean.CourseDataFile;
import com.wangrj.java_lib.java_program.video_download.bean.ImoocBugInfo;
import com.wangrj.java_lib.java_program.video_download.bean.Video;
import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_util.HttpRequest;
import com.wangrj.java_lib.java_util.TextUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

//String strUrl = "http://www.imooc.com/learn/536";// 无资料下载
// URL url2 = new URL("http://www.imooc.com/learn/510");// 有资料下载

public class Imooc {

    public static final String URL = "http://www.imooc.com";

    /**
     * http://www.wooyun.org/bugs/wooyun-2010-0141770
     * 漏洞标题： 慕课网某接口授权不当导致全部课程视频可被下载
     * "id="+视频Id
     */
    private static final String BUG_URL = "http://www.imooc.com/course/ajaxmediainfo?mid=";

    // 视频的清晰度，有L（标清），M（高清），H（超清）三种
    public static final int VIDEO_QUALITY_L = 0;
    public static final int VIDEO_QUALITY_M = 1;
    public static final int VIDEO_QUALITY_H = 2;

    private String courseUrl;
    private int videoQuality;
    /**
     * 获取方式：360浏览器登陆后 --> F12 --> Console --> document.cookie
     */
    private String cookie;

    public Imooc(String courseUrl, int videoQuality, String cookie) {
        this.courseUrl = courseUrl;
        this.videoQuality = videoQuality;
        this.cookie = cookie;
    }

    public Course getCourse() throws Exception {
        String firefoxUserAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) " +
                "Gecko/20080404 Firefox/2.0.0.14";
        HttpRequest.Response response = new HttpRequest().
                setCookie(cookie).
                setUserAgent(firefoxUserAgent).
                request(courseUrl);
        if (response.getStatus() == HttpRequest.Status.SUCCESS) {
            return parseHtml(response.getResponseData());
        } else {
            throw new Exception(GsonUtil.printFormatJson(response));
        }
    }

    private Course parseHtml(String html) throws Exception {

        //TODO
        System.out.println("------------------------------------------------------------");
        System.out.println(html);
        System.out.println("------------------------------------------------------------");

        Document document = Jsoup.parse(html);
        if (document != null) {

            Course course = new Course();
            Elements videoRootElem = document
                    .getElementsByClass("mod-chapters");

//            1.解析课程标题------------------------------------------------------------------------
            try {
                String quality = videoQuality == VIDEO_QUALITY_L ? "L" :
                        (videoQuality == VIDEO_QUALITY_M ? "M" : "H");
                course.setCourseName(document.getElementsByTag("title").text() + "(" + quality + ")");
            } catch (Exception e) {
                System.out.println("课程标题获取失败！");
                throw e;
            }

//            2.解析每章的章节名--------------------------------------------------------------------
            try {
                Elements chaptersElem = videoRootElem.select("strong");
                List<String> chaptersNames = new ArrayList<>();
                for (int i = 0; i < chaptersElem.size(); i++) {
                    chaptersNames.add(chaptersElem.get(i).text());
                }
                course.setChapterNames(chaptersNames);
            } catch (Exception e) {
                System.out.println("章节名获取失败！");
                e.printStackTrace();
            }

//            3.解析html中每个视频的名称和pageUrl---------------------------------------------------
            try {
                Elements videosElem = videoRootElem.select("a[href]");
                List<Video> videos = new ArrayList<>();
                for (int i = 0; i < videosElem.size(); i++) {
                    String title = videosElem.get(i).text();
                    // 注意：这里的name的格式一定要保证可以作为文件名
//                    title = title.replace(":", "").replace(" ", ".")
//                            .replace(")", ")mp4").replaceAll("\\(.*\\)", "");
                    title = TextUtil.correctFileName(title, "_") + ".mp4";
                    if (title.contains("练习题")) {
                        continue;
                    }
                    Video video = new Video();
                    video.setTitle(title);
                    video.setPageUrl(URL + videosElem.get(i).attr("href"));
                    String[] s = videosElem.get(i).attr("href").split("/");
                    video.setId(s[s.length - 1]);
                    videos.add(video);
                }
                course.setVideos(videos);
            } catch (Exception e) {
                System.out.println("视频获取失败！");
                e.printStackTrace();
            }

//            4.解析资料下载列表的资料文件名和url---------------------------------------------------
            try {
                Elements elements = document.getElementsByClass("downlist");
                if (elements.size() > 0) {// 若存在课程资料
                    List<CourseDataFile> courseDataFiles = new ArrayList<>();
                    Element dataRoot = elements.get(0);
                    Elements datasName = dataRoot.getElementsByTag("span");
                    Elements datasUrl = dataRoot.getElementsByTag("a");
                    for (int i = 0; i < datasName.size() && i < datasUrl.size(); i++) {
                        String name = datasName.get(i).text();
                        String url = datasUrl.get(i).attr("href");
                        CourseDataFile dataFile = new CourseDataFile(
                                TextUtil.correctFileName(name, "_") + ".zip",
                                url
                        );
                        courseDataFiles.add(dataFile);
                    }
                    course.setCourseDataFiles(courseDataFiles);
                }
            } catch (Exception e) {
                System.out.println("课程资料获取失败！");
                e.printStackTrace();
            }

//            5.解析课程学习提示--------------------------------------------------------------------
            try {
                Elements hintRoot = document.getElementsByClass("course-info-tip");
                course.setCourseHint(hintRoot.text());
            } catch (Exception e) {
                System.out.println("课程学习提示获取失败！");
                e.printStackTrace();
            }

//            6.从视频页面url获取视频真实的下载url--------------------------------------------------
            try {
                getRealUrl(course.getVideos());
            } catch (Exception e) {
                System.out.println("视频真实的下载url获取失败！");
                e.printStackTrace();
            }

            return course;

        } else {
            throw new Exception();
        }
    }

    private void getRealUrl(List<Video> videos) throws Exception {

        for (int i = 0; i < videos.size(); i++) {
            String id = videos.get(i).getId();
            HttpRequest.Response response = new HttpRequest().request(BUG_URL + id);

            ImoocBugInfo imoocBugInfo = GsonUtil.fromJson(response.getResponseData(), ImoocBugInfo.class);
            int index = 0;
            switch (videoQuality) {
                case VIDEO_QUALITY_L:
                    index = 0;
                    break;
                case VIDEO_QUALITY_M:
                    index = 1;
                    break;
                case VIDEO_QUALITY_H:
                    index = 2;
                    break;
            }
            String realUrl = imoocBugInfo.getData().getResult().getMpath().get(index);
//            realUrl = realUrl.replace("\\u003d", "=");//转化为等号
            videos.get(i).setRealUrl(realUrl);
/*
            int indexBegin = r.result.indexOf("http:");
            int indexEndH = r.result.indexOf("H.mp4");
            if (indexBegin == -1 || indexEndH == -1) {
                continue;
            }
            String realUrl = r.result.substring(indexBegin, indexEndH);
            realUrl = realUrl.replace("\\", "");
            switch (videoQuality) {
                case VIDEO_QUALITY_L:
                    realUrl = realUrl + "L.mp4";
                    break;
                case VIDEO_QUALITY_M:
                    realUrl = realUrl + "M.mp4";
                    break;
                case VIDEO_QUALITY_H:
                    realUrl = realUrl + "H.mp4";
                    break;
            }
            videos.get(i).setRealUrl(realUrl);
*/
        }
    }

}
