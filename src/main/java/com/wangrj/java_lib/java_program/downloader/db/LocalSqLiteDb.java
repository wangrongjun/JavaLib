package com.wangrj.java_lib.java_program.downloader.db;

import com.wangrj.java_lib.java_program.downloader.bean.Task;
import com.wangrj.java_lib.java_util.DebugUtil;
import com.wangrj.java_lib.java_program.downloader.bean.ThreadInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * 关于数据库与Downloader的变量的同步问题：
 * 1.新建downloader时把所有task和thread读取到变量
 * 2.新建任务时，task插入到数据库和变量中，threadinfo插入到downloadThread和数据库
 * 3.开始任务时，变量与数据库所有关于该任务的信息应完全同步。
 * 4.任务暂停时，把变量中所有与该任务相关的信息更新到数据库中。
 * 5.关闭downloader时，把变量中所有信息更新到数据库。
 */
public class LocalSqLiteDb implements DbInterface {

    private Connection conn;
    private Statement stat;

    private static final String TASK_TABLE_NAME = "task";
    private static final String THREAD_TABLE_NAME = "thread";

    public LocalSqLiteDb(String databasePath) {

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            stat = conn.createStatement();
//            创建任务信息表
            String sql = "create table if not exists " + TASK_TABLE_NAME +
                    "(_id integer primary key autoincrement," +
                    "file_name varchar(100) not null," +
                    "path varchar(100) not null," +
                    "thread_number integer not null," +
                    "url varchar(200) not null," +
                    "length integer not null," +
                    "finished integer not null," +
                    "success integer not null);";
            stat.executeUpdate(sql);
//            创建线程信息表
            stat.executeUpdate("create table if not exists " + THREAD_TABLE_NAME +
                    "(_id integer primary key autoincrement," +
                    "thread_id integer not null," +
                    "task_id integer not null," +
                    "start integer not null," +
                    "finished integer not null," +
                    "end integer not null);");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 返回-1或0:出现异常 返回>0：正常
     */
    @Override
    public synchronized int insert(Task task) throws SQLException {
        String sql = "insert into " + TASK_TABLE_NAME + "(file_name,path,thread_number,url," +
                "length,finished,success) values(" +
                "'" + task.getFileName() + "'," +
                "'" + task.getPath() + "'," +
                task.getThreadNumber() + "," +
                "'" + task.getUrl() + "'," +
                task.getLength() + "," +
                task.getFinished() + "," +
                "'" + (task.isSuccess() ? 1 : 0) + "');";
        stat.executeUpdate(sql);
        sql = "select * from " + TASK_TABLE_NAME + " where file_name='" + task.getFileName() + "';";
        ResultSet rs = stat.executeQuery(sql);
        int taskId = -1;
        while (rs.next()) {
            taskId = rs.getInt("_id");
        }

        return taskId;
    }

    @Override
    public synchronized void insert(ThreadInfo threadInfo) throws SQLException {
        String sql = "insert into " + THREAD_TABLE_NAME + "(thread_id,task_id," +
                "start,finished,end) values(" +
                threadInfo.getThreadId() + "," +
                threadInfo.getTaskId() + "," +
                threadInfo.getStart() + "," +
                threadInfo.getFinished() + "," +
                threadInfo.getEnd() + ")";
        stat.executeUpdate(sql);
    }

    @Override
    public ArrayList<Task> queryTasks() {
        ArrayList<Task> tasks = new ArrayList();
        String sql = "select * from " + TASK_TABLE_NAME + ";";
        try {
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("_id"));
                task.setFileName(rs.getString("file_name"));
                task.setPath(rs.getString("path"));
                task.setUrl(rs.getString("url"));
                task.setThreadNumber(rs.getInt("thread_number"));
                task.setLength(rs.getInt("length"));
                task.setFinished(rs.getInt("finished"));
                task.setSuccess(rs.getInt("success") == 1 ? true : false);
                tasks.add(task);
            }
        } catch (SQLException e) {
            DebugUtil.println("ErrorLine: " + DebugUtil.getErrorLine(e) + "\n" + e.toString());
            e.printStackTrace();
        }

        return tasks;
    }

    @Override
    public ArrayList<ThreadInfo> queryThreads(int taskId) {
        ArrayList<ThreadInfo> threadInfos = new ArrayList();
        String sql = "select * from " + THREAD_TABLE_NAME + " where task_id=" + taskId + ";";
        try {
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                ThreadInfo threadInfo = new ThreadInfo();
                threadInfo.setThreadId(rs.getInt("thread_id"));
                threadInfo.setTaskId(rs.getInt("task_id"));
                threadInfo.setStart(rs.getInt("start"));
                threadInfo.setFinished(rs.getInt("finished"));
                threadInfo.setEnd(rs.getInt("end"));
                threadInfos.add(threadInfo);
            }
        } catch (SQLException e) {
            DebugUtil.println("ErrorLine: " + DebugUtil.getErrorLine(e) + "\n" + e.toString());
            e.printStackTrace();
        }

        return threadInfos;
    }

    @Override
    public synchronized void updateTaskProgress(int taskId, int finished, boolean success) {
        String sql = "update " + TASK_TABLE_NAME + " set finished=" + finished +
                ",success='" + (success ? 1 : 0) + "' where _id=" + taskId + ";";
        try {
            stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void updateThreadProgress(int taskId, int threadId, int finished) {
        String sql = "update " + THREAD_TABLE_NAME + " set finished=" + finished +
                " where task_id=" + taskId + " and thread_id=" + threadId + ";";
        try {
            stat.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteTask(int taskId) {
        try {
            stat.execute("delete from " + TASK_TABLE_NAME + " where _id=" + taskId + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void deleteThread(int taskId, int threadId) {
        try {
            stat.execute("delete from " + THREAD_TABLE_NAME + " where task_id=" + taskId +
                    " and thread_id=" + threadId + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exist(String fileName, String path) throws SQLException {
        String sql = "select * from " + TASK_TABLE_NAME + " where file_name='" + fileName +
                "' and path='" + path + "';";
        ResultSet rs = stat.executeQuery(sql);
        return rs.next();
    }

    public void close() {
        try {
            stat.close();
            conn.close();
            stat = null;
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

