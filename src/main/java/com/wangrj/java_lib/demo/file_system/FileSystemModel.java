package com.wangrj.java_lib.demo.file_system;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * by wangrongjun on 2016/12/8.
 */
public class FileSystemModel {

    private FileNode rootNode;
    private FileNode selectedFileNode;
    private String savePath;

    public FileSystemModel(String savePath) {
        this.savePath = savePath;
        rootNode = read();
    }

    public FileNode read() {
        String json = FileUtil.read(savePath);
        if (TextUtil.isEmpty(json)) {
            return null;
        }
        try {
            return new Gson().fromJson(json.replace("\r\n", ""), FileNode.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save() {
        String json = GsonUtil.toPrettyJson(rootNode);
        try {
            FileUtil.write(json.replace("\n", "\r\n"), savePath);
            System.out.println("�ѱ���");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSelectedFileNode(FileNode selectedFileNode) {
        this.selectedFileNode = selectedFileNode;
        System.out.println("��ѡ���ˣ�" + selectedFileNode);
    }

    public FileNode getRootNode() {
        return rootNode;
    }

    private static FileNode getParent(FileNode rootNode, FileNode fileNode) {

        List<FileNode> children = rootNode.getChildren();
        if (children != null && children.size() > 0) {
            boolean contains = children.contains(fileNode);
            if (!contains) {
                for (FileNode child : children) {
                    FileNode parent = getParent(child, fileNode);
                    if (parent != null) {
                        return parent;
                    }
                }
                return null;
            } else {
                return rootNode;
            }

        } else {
            return null;
        }

    }

    /**
     * @return �������ɹ������ؿ��ַ��������򷵻ش�����Ϣ
     */
    public String createNewFileUnderSelectedDir(String newFileName, boolean createDir) {

        if (selectedFileNode == null) {
            if (rootNode == null) {
                rootNode = new FileNode(new FileContent(newFileName, ""), null, createDir);
                return "";
            } else {
                return "��ѡ���ļ���";
            }
        }
        if (!selectedFileNode.isDirectory()) {
            return "ֻ�����ļ����´���";
        }

        if (selectedFileNode.getChildren() == null) {
            selectedFileNode.setChildren(new ArrayList<FileNode>());
        }

        List<FileNode> children = selectedFileNode.getChildren();
        for (FileNode child : children) {
            if (child.getFileContent().getFileName().equals(newFileName)) {
                return "��λ���Ѱ���ͬ���ļ�";
            }
        }

        children.add(new FileNode(new FileContent(newFileName, ""), null, createDir));
        System.out.println("�㴴���ˣ�" + newFileName);
        return "";
    }

    public String updateSelectedFileName(String newFileName) {

        if (selectedFileNode == null) {
            return "��ѡ���ļ���";
        }

        FileNode parent = getParent(rootNode, selectedFileNode);
        assert parent != null;
        List<FileNode> brothers = parent.getChildren();
        for (FileNode brother : brothers) {
            if (!brother.equals(selectedFileNode) &&
                    brother.getFileContent().getFileName().equals(newFileName)) {
                return "��λ���Ѱ���ͬ���ļ�";
            }
        }

        selectedFileNode.getFileContent().setFileName(newFileName);
        System.out.println("���޸��ˣ�" + newFileName);
        return "";
    }

    public void deleteSelectedFileNode() {
        if (selectedFileNode == null) {
            return;
        }
        if (rootNode.equals(selectedFileNode)) {
            rootNode = null;
        } else {
            delete(rootNode, selectedFileNode);
        }
        selectedFileNode = null;
    }

    private static void delete(FileNode rootNode, FileNode deleteNode) {

        List<FileNode> children = rootNode.getChildren();
        if (children != null && children.size() > 0) {
            boolean contains = children.remove(deleteNode);
            if (!contains) {
                for (FileNode child : children) {
                    delete(child, deleteNode);
                }
            } else {
                System.out.println("��ɾ���ˣ�" + deleteNode);
            }
        }

    }

}
