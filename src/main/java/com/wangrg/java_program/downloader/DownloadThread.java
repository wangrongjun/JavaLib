package com.wangrg.java_program.downloader;

import com.wangrg.java_program.downloader.bean.Task;
import com.wangrg.java_program.downloader.bean.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 王荣俊 on 2016/4/1.
 */
public class DownloadThread extends Thread {

    private static final int CONNECT_TIME_OUT = 6000;
    private static final int READ_TIME_OUT = 6000;

    private ThreadInfo threadInfo;
    private Task task;
    private OnProgressListener progressListener;
    public boolean stop = false;

    public ThreadInfo getThreadInfo() {
        return threadInfo;
    }

    public DownloadThread(Task task, ThreadInfo threadInfo, OnProgressListener progressListener) {
        this.task = task;
        this.threadInfo = threadInfo;
        this.progressListener = progressListener;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        RandomAccessFile randomAccessFile = null;
        try {
            URL url = new URL(task.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECT_TIME_OUT);
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setRequestMethod("GET");

            int start = threadInfo.getStart();
            int end = threadInfo.getEnd();
            int finished = threadInfo.getFinished();
            conn.setRequestProperty("Range", "bytes=" + (start + finished) + "-" + end);

            File file = new File(task.getPath(), task.getFileName());
            randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(threadInfo.getStart() + threadInfo.getFinished());

            InputStream is = conn.getInputStream();
            int len;
            long delay = System.currentTimeMillis();
            byte[] buf = new byte[1024 * 4];
            int progress = 0;
            while ((len = is.read(buf)) > 0) {
                if (stop) {
                    if (stopListener != null) {
                        stopListener.onStop();
                    }
                    return;
                }
                randomAccessFile.write(buf, 0, len);
                progress += len;
                int preFinished = threadInfo.getFinished();
                threadInfo.setFinished(preFinished + len);
                long curr = System.currentTimeMillis();
                if (curr - delay > 1000) {
//                    TODO 通知界面进度的变化
                    if (progressListener != null) {
                        progressListener.onDownloading(threadInfo.getTaskId(),
                                threadInfo.getThreadId(), progress, threadInfo.getFinished(),
                                threadInfo.getEnd() - threadInfo.getStart());
                    }
                    delay = curr;
                    progress = 0;
                }
            }
//                    TODO 通知界面下载完成
            stop = true;
            if (progressListener != null) {
                progressListener.onDownloadFinished(threadInfo.getTaskId(),
                        threadInfo.getThreadId(), threadInfo.getFinished());
            }

        } catch (IOException e) {
//                    TODO 通知界面这一条线程下载出错
            stop = true;
            if (progressListener != null) {
                progressListener.onError(threadInfo.getTaskId(), threadInfo.getThreadId(),
                        threadInfo.getFinished(), e.toString());
            }
            e.printStackTrace();

        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    interface OnStopListener {
        void onStop();
    }

    private OnStopListener stopListener = null;

    public synchronized void stop(OnStopListener stopListener) {
        this.stopListener = stopListener;
        stop = true;
    }

}
