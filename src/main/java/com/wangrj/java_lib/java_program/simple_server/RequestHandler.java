package com.wangrj.java_lib.java_program.simple_server;

import com.wangrj.java_lib.java_util.StreamUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_util.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * by wangrongjun on 2017/10/19.
 */
public class RequestHandler extends Thread {

    private Socket socket;
    private String resourceRoot;

    public RequestHandler(Socket socket, String resourceRoot) {
        this.socket = socket;
        this.resourceRoot = resourceRoot;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[2048];
        try {
            // 这里是关键！千万不要使用循环！否则浏览器会卡死！
            int len = socket.getInputStream().read(buffer);
            String request = new String(buffer, 0, len);
            System.out.println("\n-------------------------------------------------------------");
            System.out.println(request);
            response(socket.getOutputStream(), request);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void response(OutputStream os, String request) throws IOException {
        // 解析请求行，获取method和path
        String method;
        Pattern pattern;
        if (request.startsWith("GET")) {
            method = "GET";
            pattern = Pattern.compile("GET ([^ ]+) HTTP/1");
        } else if (request.startsWith("POST")) {
            method = "POST";
            pattern = Pattern.compile("POST ([^ ]+) HTTP/1");
        } else {
            throw new IOException("request line method not found");
        }
        Matcher matcher = pattern.matcher(request);
        if (!matcher.find()) {
            throw new IOException("request line is error");
        }
        String path = matcher.group(1);
        System.out.println("path: " + path);

        // 关闭服务器
        if (path.equals("/close")) {
            RunClass.shouldClose = true;
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<h1>Server is closed.</h1>";
            os.write(response.getBytes());
            os.flush();
            return;
        }

        File file = new File(resourceRoot + path);
        if (file.exists() && !file.isDirectory()) {
            // 如果请求的文件存在，响应报文=响应行+响应头+空行+文件二进制数据
            String suffix = TextUtil.getTextAfterLastPoint(file.getName());
            String contentType = ContentType.getType("." + suffix);
            if (contentType == null) {
                contentType = ".*";// 二进制流，不知道下载文件类型
            }
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "\r\n";
            FileInputStream fis = new FileInputStream(file);
            os.write(response.getBytes());
            os.write(StreamUtil.toBytes(fis));
            fis.close();
        } else {
            // 如果请求的文件不存在，返回404页面
            String response = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "\r\n" +
                    "<h1>404 - File Not Found</h1>" +
                    "<hr>" +
                    path;
            os.write(response.getBytes());
        }
        os.flush();
    }

}
