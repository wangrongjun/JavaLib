package com.wangrg.java_lib.java_program.video_download.bean;

public class Video {

    private String title;// 视频标题
    private String pageUrl;// 视频页面的Url
    private String realUrl;// 视频真实的Url
    private String id;// 从视频页面Url截取的视频id,在Imooc中用到

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
