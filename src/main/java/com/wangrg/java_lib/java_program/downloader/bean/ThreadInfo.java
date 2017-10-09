package com.wangrg.java_lib.java_program.downloader.bean;

/**
 * Created by 王荣俊 on 2016/3/31.
 */
public class ThreadInfo {

    private int threadId;
    private int taskId;
    private int start;
    private int end;
    private int finished;

    public ThreadInfo(){
        
    }
    
    public ThreadInfo(int taskId, int threadId, int start, int end) {
        this.taskId = taskId;
        this.threadId = threadId;
        this.end = end;
        this.start = start;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
