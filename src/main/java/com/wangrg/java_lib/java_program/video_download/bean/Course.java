package com.wangrg.java_lib.java_program.video_download.bean;

import com.wangrg.java_lib.java_util.ListUtil;

import java.util.ArrayList;
import java.util.List;

public class Course {

    private String courseName;// 课程名称
    private List<String> chapterNames;// 章节名称
    private List<Video> videos;
    private List<CourseDataFile> courseDataFiles;// 课程资料
    private String courseHint;// 课程学习提示

    public static Course getCourseListExample() {
        Course course = new Course();
        course.setCourseName("courseName");
        course.setChapterNames(ListUtil.build("chapter1", "chapter2"));
        course.setCourseHint("courseHint");

        List<Video> videoList = new ArrayList<>();
        Video video = new Video();
        video.setTitle("video1");
        video.setRealUrl("http://www.baidu.com/");
        videoList.add(video);
        video = new Video();
        video.setTitle("video2");
        video.setRealUrl("http://www.baidu.com/");
        videoList.add(video);

        List<CourseDataFile> dataFileList = new ArrayList<>();
        dataFileList.add(new CourseDataFile("dataFile1", "http://www.baidu.com/"));
        dataFileList.add(new CourseDataFile("dataFile2", "http://www.baidu.com/"));

        course.setVideos(videoList);
        course.setCourseDataFiles(dataFileList);
        return course;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<String> getChapterNames() {
        return chapterNames;
    }

    public void setChapterNames(List<String> chapterNames) {
        this.chapterNames = chapterNames;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<CourseDataFile> getCourseDataFiles() {
        return courseDataFiles;
    }

    public void setCourseDataFiles(List<CourseDataFile> courseDataFiles) {
        this.courseDataFiles = courseDataFiles;
    }

    public String getCourseHint() {
        return courseHint;
    }

    public void setCourseHint(String courseHint) {
        this.courseHint = courseHint;
    }
}
