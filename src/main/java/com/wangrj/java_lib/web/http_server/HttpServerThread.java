package com.wangrj.java_lib.web.http_server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * by wangrongjun on 2019/2/6.
 */
public class HttpServerThread extends Thread {

    private Socket socket;

    public HttpServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            getRequestData(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getRequestData(InputStream is) throws IOException {
        byte[] buf = new byte[64];
        int length;
        StringBuilder requestHeader = new StringBuilder();
        while (!requestHeader.toString().endsWith("\r\n\r\n")) {
            length = is.read(buf);
            requestHeader.append(new String(buf, 0, length));
        }
        System.out.println("----------------------- request header from client\n" + requestHeader);
    }

    public static class HttpRequest {
        private String requestHeader;
        private byte[] requestBody;

        private HttpRequest(String requestHeader, byte[] requestBody) {
            this.requestHeader = requestHeader;
            this.requestBody = requestBody;
        }

        public String getRequestPath() {
            return null;
        }
    }

}
