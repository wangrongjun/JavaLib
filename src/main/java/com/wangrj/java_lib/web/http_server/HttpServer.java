package com.wangrj.java_lib.web.http_server;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * by wangrongjun on 2019/2/6.
 */
public class HttpServer implements Closeable {

    private int port;
    private boolean running = true;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newFixedThreadPool(5);// 支持最大并发数为5

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        while (running) {
            System.out.println("Waiting for client...");
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (SocketException e) {
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
            threadPool.execute(new HttpServerThread(socket));
        }
    }

    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
        running = false;
        System.out.println("HttpServer closed");
    }

}
