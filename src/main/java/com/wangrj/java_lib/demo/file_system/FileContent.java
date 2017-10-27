package com.wangrj.java_lib.demo.file_system;

/**
 * by wangrongjun on 2016/12/8.
 */
public class FileContent {

    private String fileName;
    private String content;

    public FileContent(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
