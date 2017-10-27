package com.wangrj.java_lib.java_util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * by wangrongjun on 2016/11/22.
 */
public class Downloader {

    public interface OnDownloadListener {
        /**
         * @param speed 单位：Byte/ms(即KB/s)
         * @return 是否继续下载
         */
        boolean onDownloadProgressUpdate(int status, String fileUrl, String savePath,
                                         double currentBytes, double totalBytes, double speed);
    }

    public interface OnExceptionListener {
        void onException(String fileUrl, String savePath, Exception e);
    }

    // 网络连接，读取的限时
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    public static final int READ_TIMEOUT = 10 * 1000;

    // 帮助计算时间间隔
    private static long preTime;
    // 帮助计算下载速度
    private static int preBytes;

    //    public static final int OK = 1;
    public static final int DOWNLOADING = 2;
    public static final int DOWNLOAD_FINISH = 3;
    public static final int EXISTS = 4;
    //    public static final int DOWNLOAD_FAILURE = -1;
    public static final int DOWNLOAD_AGAIN = -2;

    private String fileUrl;
    private String savePath;
    private String cookie;
    private OnDownloadListener listener;
    private int duration;//回调通知进度的时间间隔(毫秒)，0则只有下载完成时回调

    public Downloader(String fileUrl, String savePath) {
        this.fileUrl = fileUrl;
        this.savePath = savePath;
        duration = 1000;
    }

    public Downloader setCookie(String cookie) {
        this.cookie = cookie;
        return this;
    }

    public Downloader setListener(OnDownloadListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 回调通知进度的时间间隔(毫秒)，0则只有下载完成时回调
     */
    public Downloader setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public void startInNewThread(final OnExceptionListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (IOException e) {
                    if (listener != null) {
                        listener.onException(fileUrl, savePath, e);
                    }
                }
            }
        }).start();
    }

    public void start() throws IOException {

        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            URL url = new URL(fileUrl);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            if (cookie != null && cookie.length() > 0) {
                conn.setRequestProperty("Cookie", cookie);
            }

            file = new File(savePath);

            if (file.exists()) {
                listener.onDownloadProgressUpdate(EXISTS, fileUrl, savePath, 0, file.getTotalSpace(), 0);
                return;
            }

            int fileLength = conn.getContentLength();

            // 有时可以得到conn，但长度为空，这样会下载到一个大小为0的文件
            if (fileLength == 0) {
                file.delete();
                throw new IOException(DebugUtil.println("长度为空,下载失败"));
            }

            is = conn.getInputStream();

            file.createNewFile();

            fos = new FileOutputStream(file);

            preBytes = 0;
            preTime = System.currentTimeMillis();
            int bytes = 0;
            int len;
            byte[] buf = new byte[1024];

            if (listener != null) {
                listener.onDownloadProgressUpdate(DOWNLOADING, fileUrl, savePath, 0, fileLength, 0);
            }

            while ((len = is.read(buf)) >= 0) {
                bytes += len;
                fos.write(buf, 0, len);

                if (listener != null) {
                    long delay = System.currentTimeMillis() - preTime;
                    if (delay > duration && duration != 0) {

                        // speed的单位：Byte/ms(即KB/s)
                        double speed = 1.0 * (bytes - preBytes) / delay;

                        boolean continute = listener.onDownloadProgressUpdate(
                                DOWNLOADING, fileUrl, savePath, bytes, fileLength, speed);
                        if (!continute) {
                            return;
                        }
                        preTime = System.currentTimeMillis();
                        preBytes = bytes;
                    }
                }
            }

            fos.flush();
            is.close();
            fos.close();

            // 有时没有异常，但下载会提前结束，文件有大小，却损坏
            if (bytes / fileLength < 0.95) {
                is.close();
                fos.close();
                file.delete();
                throw new IOException(DebugUtil.println(" 下载失败，下载提前结束  "
                        + fileUrl));
            }

        } catch (IOException e) {
            if (file != null & is != null && fos != null) {
                is.close();
                fos.close();
                file.delete();
            }
            throw new IOException(" 下载失败，网络连接不稳定， " + fileUrl
                    + DebugUtil.println(e.toString()));

        }

        if (listener != null) {
            listener.onDownloadProgressUpdate(DOWNLOAD_FINISH, fileUrl, savePath, 0, 0, 0);
        }

    }

}
