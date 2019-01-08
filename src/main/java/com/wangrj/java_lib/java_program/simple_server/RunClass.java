package com.wangrj.java_lib.java_program.simple_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * by wangrongjun on 2017/10/19.
 */
public class RunClass {

    public static int port = 8080;
    public static String resourceRoot = "E:/resource";
    public static boolean shouldClose = false;

    /*
    http://localhost:/8080/a.png（显示图片）
    http://localhost:/8080/a.png1（404）
    http://localhost:/8080/close（关闭服务器）
     */

    public static void main(String[] args) throws IOException {
        if (args != null && args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (args != null && args.length > 1) {
            resourceRoot = args[1];
        }

        ServerSocket server = new ServerSocket(port);
        while (!shouldClose) {
            Socket socket = server.accept();
            new RequestHandler(socket, resourceRoot).start();
        }
        server.close();
    }

}
