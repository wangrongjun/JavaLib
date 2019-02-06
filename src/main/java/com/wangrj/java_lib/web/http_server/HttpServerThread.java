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
//        while ((length = is.read(buf)) != -1) {
//            requestHeader.append(new String(buf, 0, length));
//        }
        while (!requestHeader.toString().endsWith("\r\n\r\n")) {
            length = is.read(buf);
            requestHeader.append(new String(buf, 0, length));
        }
        System.out.println("----------------------- request header from client\n" + requestHeader.toString().replace("\r\n", "\\r\\n\r\n"));
    }

    public static class HttpRequest {
        private String requestMethod;
        private String requestPath;
        private byte[] requestBody;

        private HttpRequest(String requestHeader, byte[] requestBody) {
            this.requestBody = requestBody;
            String[] split = requestHeader.substring(0, requestHeader.indexOf("/n")).split(" ");
            String requestMethod = split[0];
            String requestPath = split[1];
        }

        public String getRequestMethod() {
            return requestMethod;
        }

        public String getRequestPath() {
            return requestPath;
        }

        public byte[] getRequestBody() {
            return requestBody;
        }
    }

}
