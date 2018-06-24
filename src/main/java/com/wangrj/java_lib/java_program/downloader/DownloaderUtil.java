package com.wangrj.java_lib.java_program.downloader;

import com.wangrj.java_lib.java_program.downloader.bean.Task;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import java.util.ArrayList;

/**
 * Created by 王荣俊 on 2016/4/6.
 */
public class DownloaderUtil {
    /**
     * @return 若同一目录下有重名，返回fileName+(1)，否则返回fileName
     */
    public static String renameIfNecessary(String fileName, String path, ArrayList<Task> tasks) {
        int i = 1;
        while (fileNameExist(fileName, path, tasks)) {
            String preName = TextUtil.parseFileName(fileName, false);
            String postName = TextUtil.parseFileName(fileName, true);
            if (preName.endsWith("(" + i + ")")) {
                i++;
                continue;
            } else {
                fileName = preName.replace("(" + (i - 1) + ")", "") + "(" + i + ")." + postName;
            }
        }
        return fileName;
    }

    private static boolean fileNameExist(String fileName, String path, ArrayList<Task> tasks) {
        for (Task task : tasks) {
            if (task.getPath().equals(path)) {
                if (task.getFileName().equals(fileName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Task getTask(int taskId, ArrayList<Task> tasks) {
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }

        return null;
    }

    public static boolean isAllThreadFinished(ArrayList<DownloadThread> downloadThreads) {
        boolean b = true;
        for (DownloadThread downloadThread : downloadThreads) {
            if (!downloadThread.stop) {
                b = false;
            }
        }

        return b;
    }

    public static boolean isAllThreadFinished(int taskId, ArrayList<DownloadThread> downloadThreads) {
        boolean b = true;
        for (DownloadThread downloadThread : downloadThreads) {
            if (downloadThread.getThreadInfo().getTaskId() == taskId &&
                    !downloadThread.stop) {
                b = false;
            }
        }

        return b;
    }

}
