package com.wangrj.java_lib.java_program.simple_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * by wangrongjun on 2017/10/19.
 */
public class RunClass {

    public static final int port = 8080;
    public static final String resourceRoot = "E:/resource";
    public static boolean shouldClose = false;

    /*
    http://localhost:/8080/a.png（显示图片）
    http://localhost:/8080/a.png1（404）
    http://localhost:/8080/close（关闭服务器）
     */

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        while (!shouldClose) {
            Socket socket = server.accept();
            new RequestHandler(socket, resourceRoot).start();
        }
        server.close();
    }

}
