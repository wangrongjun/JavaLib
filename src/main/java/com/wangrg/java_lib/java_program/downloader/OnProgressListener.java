package com.wangrg.java_lib.java_program.downloader;

/**
 * Created by 王荣俊 on 2016/4/6.
 */
public interface OnProgressListener {
    void onDownloading(int taskId, int threadId, int speedPreSecond, int finished, int length);

    void onError(int taskId, int threadId, int finished, String exception);

    void onDownloadFinished(int taskId, int threadId, int finished);
}
