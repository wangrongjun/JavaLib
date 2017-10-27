package com.wangrj.java_lib.java_program.downloader.bean;

/**
 * Created by 王荣俊 on 2016/3/31.
 */
public class Task {

    private int taskId;
    private String fileName;
    private String path;
    private int threadNumber;
    private String url;
    private int length;
    private int speedPreSecond;//为了汇总该任务每条线程的下载速度,不必存进数据库
    private long delay;//为了计算时间，分配好每个任务的下载进度和速度，不必存进数据库
    private int finished;
    private boolean success;

    public Task() {

    }

    public Task(String fileName, String path, int threadNumber, String url, int length, int finished, boolean success) {
        this.fileName = fileName;
        this.path = path;
        this.threadNumber = threadNumber;
        this.url = url;
        this.length = length;
        this.finished = finished;
        this.success = success;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public int getSpeedPreSecond() {
        return speedPreSecond;
    }

    public void setSpeedPreSecond(int speedPreSecond) {
        this.speedPreSecond = speedPreSecond;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
