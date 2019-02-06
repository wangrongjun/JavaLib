package com.wangrj.java_lib.web.http_server;

import java.io.IOException;

/**
 * by wangrongjun on 2019/2/6.
 */
public class HttpServerTest {

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.start();
    }

}
