package com.wangrj.java_lib.web.web_socket;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * by wangrongjun on 2019/2/6.
 */
public class WebSocketTest {

    public static void main(String[] args) throws Exception {
        WebSocket webSocket = new WebSocket(8081);
        webSocket.start();
        webSocket.setOnDataReceivedListener(data -> {
            String s = new String(data);
            System.out.println(s);
            try {
                webSocket.sendData(("Hello: " + s).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        while (true) {
            Thread.sleep(2000);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            try {
                webSocket.sendData(time.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
