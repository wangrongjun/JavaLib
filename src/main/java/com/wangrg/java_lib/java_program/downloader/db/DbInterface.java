package com.wangrg.java_lib.java_program.downloader.db;

import com.wangrg.java_lib.java_program.downloader.bean.Task;
import com.wangrg.java_lib.java_program.downloader.bean.ThreadInfo;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by 王荣俊 on 2016/4/6.
 */
public interface DbInterface {
    /**
     * @return 返回自动增长的id
     */
    int insert(Task task) throws SQLException;

    void insert(ThreadInfo threadInfo) throws SQLException;

    ArrayList<Task> queryTasks() throws SQLException;

    ArrayList<ThreadInfo> queryThreads(int taskId);

    void updateTaskProgress(int taskId, int finished, boolean success);

    void updateThreadProgress(int taskId, int threadId, int finished);

    void deleteTask(int taskId);

    void deleteThread(int taskId, int threadId);

    /**
     * 新建任务时需要先查看文件是否已存在。若存在，则重命名再创建任务
     */
    boolean exist(String fileName, String path) throws SQLException;

}
