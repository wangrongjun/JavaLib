package com.wangrj.java_lib.java_program.video_download.bean;

public class CourseDataFile {

    private String name;// 资料文件名
    private String url;// 资料下载网址

    public CourseDataFile(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
