package com.wangrj.java_lib.java_program.downloader;

import com.wangrj.java_lib.java_util.DebugUtil;
import com.wangrj.java_lib.java_util.HttpUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_program.downloader.bean.Task;
import com.wangrj.java_lib.java_program.downloader.bean.ThreadInfo;
import com.wangrj.java_lib.java_program.downloader.db.LocalSqLiteDb;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 作用：对下载任务进行创建，开始，暂停，删除等控制。
 * 数据库的使用：任务开始时读取，暂停和关闭时保存，
 */
public class Downloader implements OnProgressListener {

    public String downloadPath;
    public int threadNumber;
    private ArrayList<Task> tasks;
    private LocalSqLiteDb db;
    /**
     * 每个downloadThread持有一个threadInfo和task
     */
    private ArrayList<DownloadThread> downloadThreads;

    public Downloader(String dbDir, String downloadPath, int threadNumber) {
        this.downloadPath = downloadPath;
        this.threadNumber = threadNumber;
        tasks = new ArrayList<>();
        downloadThreads = new ArrayList<>();
        db = new LocalSqLiteDb(dbDir);
//        TODO 查询数据库并给tasks赋值
        tasks = db.queryTasks();

    }

    /**
     * 作用：初始化任务
     * 过程：获取文件长度，配置每条线程的信息，并添加到tasks和downloadThreads
     *
     * @return 这个任务对应的id。进度提示信息和删除，开始，暂停就是根据这个id对应不同的下载任务。
     * 若返回-1,网络不可用或文件不存在
     */
    public int createTask(String url, String fileName) throws SQLException {
        Task task = new Task(fileName, downloadPath, threadNumber, url, 0, 0, false);

//        TODO 根据文件名在tasks查询任务是否已存在，若存在，fileName+（1）
        task.setFileName(Util.renameIfNecessary(task.getFileName(), task.getPath(), tasks));

//        TODO 根据url获取文件长度
        int length = HttpUtil.getDownloadFileLength(url);
        if (length <= 0) {
//            通知网络不可用或文件不存在
            System.out.println("no Internet or file not exist");
            return -1;
        } else {
            task.setLength(length);
//            创建文件
            try {
                File file = new File(task.getPath(), task.getFileName());
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(length);
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        TODO 添加task到数据库(id项为自动增长)，并把数据库返回的id作为该任务的id,添加task到tasks
        int taskId = db.insert(task);
        task.setTaskId(taskId);
        tasks.add(task);

//        TODO 创建downloadThread并根据文件长度分配每条线程的下载指标，threadId从0到threadNumber-1
        for (int i = 0; i < threadNumber; i++) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setThreadId(i);
            threadInfo.setTaskId(task.getTaskId());
            length = task.getLength() / threadNumber;
            threadInfo.setStart(i * length);
            threadInfo.setEnd((i + 1) * length - 1);
            if (i == threadNumber - 1) {
                threadInfo.setEnd(task.getLength());
            }
            threadInfo.setFinished(0);

//        TODO threadInfo插入到数据库
            db.insert(threadInfo);

        }

        return taskId;

    }

    /**
     * 根据taskId从tasks读取任务信息和线程信息，循环启动多个线程下载
     */
    public void startTask(int taskId) {
//        TODO 根据taskId从tasks读取任务信息，从数据库读取线程信息，创建线程对象,添加到downloadThreads,最后启动
        Task task = Util.getTask(taskId, tasks);
        ArrayList<ThreadInfo> threadInfos = db.queryThreads(taskId);
        for (ThreadInfo info : threadInfos) {
            DownloadThread downloadThread = new DownloadThread(task, info, this);
            downloadThreads.add(downloadThread);
            downloadThread.start();
        }

    }

    /**
     * 风险：设置stop为true后，可能会线程还没完全停止就已经保存线程信息到数据库，使数据库进度不准确
     *
     * @param taskId
     */
    public void stopTask(final int taskId) {
//        TODO 根据taskId找到所有线程并结束。注意要使用监听器，等线程跳出下载循环再保存信息到数据库

        for (final DownloadThread downloadThread : downloadThreads) {
            final ThreadInfo threadInfo = downloadThread.getThreadInfo();
            if (threadInfo.getTaskId() == taskId) {
                downloadThread.stop(new DownloadThread.OnStopListener() {
                    @Override
                    public void onStop() {
//                        保存信息到数据库
                        db.updateThreadProgress(taskId, threadInfo.getThreadId(), threadInfo.getFinished());
//                        删除该线程对象
                        downloadThreads.remove(downloadThread);
                    }
                });

            }
        }
    }

    public void close() {
//        TODO 暂停所有任务
//        TODO 保存所有线程信息到数据库
        for (final DownloadThread downloadThread : downloadThreads) {
            downloadThread.stop(new DownloadThread.OnStopListener() {
                @Override
                public void onStop() {
                    ThreadInfo info = downloadThread.getThreadInfo();
                    db.updateThreadProgress(info.getTaskId(), info.getThreadId(), info.getFinished());
                }
            });
        }
//        TODO 保存所有任务信息到数据库
        for (Task task : tasks) {
            db.updateTaskProgress(task.getTaskId(), task.getFinished(), task.isSuccess());
        }

//        TODO 关闭置空所有变量
        tasks = null;
        downloadThreads = null;
        db.close();
        db = null;
    }

    /**
     * 后几个参数的单位全为Byte
     */
    @Override
    public synchronized void onDownloading(int taskId, int threadId, int speedPreSecond, int finished, int length) {
        Task task = Util.getTask(taskId, tasks);
        task.setFinished(task.getFinished() + speedPreSecond);
        task.setSpeedPreSecond(task.getSpeedPreSecond() + speedPreSecond);

        long preTime = task.getDelay();
        long currTime = System.currentTimeMillis();
        if (currTime - preTime >= 1000) {
            task.setDelay(currTime);

            String strSpeed, strLength, strFinished;
            int taskSpeed = task.getSpeedPreSecond();
            if (taskSpeed < 1024 * 1024) {
                strSpeed = (int) ((double) taskSpeed / 1024) + " KB/s";
            } else {
                strSpeed = TextUtil.formatDouble((double) taskSpeed / 1024 / 1024, 1) + " MB/s";
            }
            strFinished = TextUtil.formatDouble((double) task.getFinished() / 1024 / 1024, 2) + " MB";
            strLength = TextUtil.formatDouble((double) task.getLength() / 1024 / 1024, 2) + " MB";
            System.out.println(task.getFileName() + "  " + strSpeed + "   " + strFinished + " / " + strLength + "   " +
                    (int) (((double) task.getFinished() / task.getLength()) * 100) + "%");

            task.setSpeedPreSecond(0);
        }

    }

    @Override
    public void onError(int taskId, int threadId, int finished, String exception) {
        db.updateThreadProgress(taskId, threadId, finished);
        DebugUtil.println(Util.getTask(taskId, tasks).getFileName() +
                " thread-" + threadId + "  " + exception);
    }

    private int reStartNumber = 0;

    @Override
    public synchronized void onDownloadFinished(int taskId, int threadId, int finished) {
        db.deleteThread(taskId, threadId);
        System.out.println("------- thread " + threadId + " finish----------");

        if (Util.isAllThreadFinished(taskId, downloadThreads)) {//单个文件下载结束
            Task task = Util.getTask(taskId, tasks);
            if (db.queryThreads(taskId).size() == 0) {//若数据库所有该任务的线程都被删除
                task.setSuccess(true);
                System.out.println("-----" + Util.getTask(taskId, tasks).getFileName() + " success-----");
            } else {//否则就是有线程出错，需要从出错的位置接着下载,默认重新下载5次
                if (reStartNumber <= 5) {
                    startTask(taskId);
                    reStartNumber++;
                    System.out.println("-----" + Util.getTask(taskId, tasks).getFileName() + " reStart-----");
                } else {
                    System.out.println("-----" + Util.getTask(taskId, tasks).getFileName() + " fail-----");
                }
            }

            if (Util.isAllThreadFinished(downloadThreads)) {//所有文件下载结束
                System.out.println("----------all finish--------");
                close();

            }
        }

    }
}
