package com.wangrg.demo.file_system;

import java.util.List;

/**
 * by wangrongjun on 2016/12/8.
 */
public class FileNode {

    private FileContent fileContent;
    private List<FileNode> children;
    private boolean isDirectory;

    public FileNode(FileContent fileContent, List<FileNode> children, boolean isDirectory) {
        this.fileContent = fileContent;
        this.children = children;
        this.isDirectory = isDirectory;
    }

    @Override
    public String toString() {
        return fileContent.getFileName();
    }

    public FileContent getFileContent() {
        return fileContent;
    }

    public void setFileContent(FileContent fileContent) {
        this.fileContent = fileContent;
    }

    public List<FileNode> getChildren() {
        return children;
    }

    public void setChildren(List<FileNode> children) {
        this.children = children;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

}
