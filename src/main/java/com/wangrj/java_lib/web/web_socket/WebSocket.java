package com.wangrj.java_lib.web.web_socket;

import com.wangrj.java_lib.java_util.BinaryUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSocket implements Closeable {

    public void setOnDataReceivedListener(OnDataReceivedListener onDataReceivedListener) {
        this.onDataReceivedListener = onDataReceivedListener;
    }

    public interface OnDataReceivedListener {
        void onDataReceived(byte[] data);
    }

    private int port;
    private boolean running = true;
    private ServerSocket serverSocket;
    private Socket socket;
    private OnDataReceivedListener onDataReceivedListener;

    public WebSocket(int port) {
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

        Thread receiveWebSocketDataThread = new Thread(() -> {
            while (running) {
                System.out.println("Waiting for client...");
                try {
                    socket = serverSocket.accept();
                } catch (SocketException e) {
                    continue;
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                try {
                    httpConnect(socket);
                    System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
                    System.out.println("Start communicate with client...");
                    while (true) {
                        ByteArrayOutputStream receivedData = new ByteArrayOutputStream();
                        receiveWebSocketData(socket.getInputStream(), receivedData);
                        if (onDataReceivedListener != null) {
                            onDataReceivedListener.onDataReceived(receivedData.toByteArray());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        socket = null;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        receiveWebSocketDataThread.start();
    }

    public void sendData(byte[] data) throws IOException {
        if (socket == null) {
            throw new IllegalStateException("Unable to send data without a client connected.");
        }
        ByteArrayOutputStream sendData = new ByteArrayOutputStream();
        sendData.write(0b10000001);
        if (data.length < 126) {
            sendData.write(data.length);
        } else if (data.length < 65536) {
            // TODO 使用16位存储长度
        } else {
            // TODO 使用64位存储长度
        }
        sendData.write(data);
        OutputStream os = socket.getOutputStream();
        os.write(sendData.toByteArray());
        os.flush();
    }

    @Override
    public void close() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
            serverSocket = null;
        }
        running = false;
        System.out.println("WebSocket closed");
    }

    /**
     * 连接认证阶段
     */
    private static void httpConnect(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        byte[] buf = new byte[64];
        int length;
        StringBuilder requestHeader = new StringBuilder();
        while (!requestHeader.toString().endsWith("\r\n\r\n")) {
            length = is.read(buf);
            requestHeader.append(new String(buf, 0, length));
        }
//        System.out.println("----------------------- request header from client\n" + requestHeader);
        String responseHeader = "";
        boolean error = false;
        try {
            responseHeader = response(requestHeader.toString());
        } catch (IllegalArgumentException e) {
            error = true;
            responseHeader += "HTTP/1.1 400 Bad Request\n";
            responseHeader += "\n";
            responseHeader += e.getMessage();
            throw new IllegalStateException(responseHeader);
        } finally {
//            System.out.println("+++++++++++++++++++++++ response header from server\n" + responseHeader);
            os.write(responseHeader.getBytes());
            os.flush();
            if (error) {
                os.close();
                socket.close();
            }
        }
    }

    /**
     * @param payloadData 负载数据
     */
    private static void receiveWebSocketData(InputStream is, ByteArrayOutputStream payloadData) throws IOException {
        // 解析前16位（包括，前八位的操作符，第九位，十位后的负载长度）
        byte[] headerControl = new byte[2];
        is.read(headerControl);

//            System.out.println(BinaryUtil.toBinaryString(headerControl, ","));

        // 0 bit: 结束标识位，如果为1，代表该帧为结束帧
        boolean fin = BinaryUtil.getBitValue(headerControl[0], 7, 7) == 1;

        // 4-7 bit: 操作码（用于标识该帧负载的类型）
        int opcode = BinaryUtil.getBitValue(headerControl[0], 0, 3);
        if (opcode != 0x01) {// 0x01 文本帧，最常用到的数据帧类别之一，表示该帧的负载是一段文本(UTF-8字符流)
            System.out.println("Warning: payload is binary data, but handle with text data");
        }

        // 8 bit: 掩码标识位（用来表明负载是否经过掩码处理，浏览器发送的帧必为1，服务器发送的帧必为0，否则应断开WebSocket连接）
        if (BinaryUtil.getBitValue(headerControl[1], 7, 7) != 1) {
            throw new RuntimeException("Client must use mask code");
        }

        // 9-15 bit: 负载数据长度
        //payload length 负载长度，单位字节如果负载长度0~125字节，则此处就是负载长度的字节数，如果负载长度在126~65535之间，
        // 则此处的值为126，16~32Bit表示负载的真实长度。如果负载长度在65536~2的64次方-1时，16~80Bit表示负载的真实长度。
        int payloadLength = BinaryUtil.getBitValue(headerControl[1], 0, 6);
        if (payloadLength >= 126) {
            // 如果等于126，那么就是接下来的 16-31 bit 表示真实的负载数据长度
            // 如果等于127，那么就是接下来的 16-80 bit 表示真实的负载数据长度
            // TODO 当接负载数据的长度大于65536时，会出问题，表现为is.read完负载数据的长度，还能read出数据，而不是预期的堵塞，导致把这些未知的数据当作下一个数据包来解析，格式错误
            byte[] payloadLengthBytes = new byte[payloadLength == 126 ? 2 : 8];
            is.read(payloadLengthBytes);
            payloadLength = Math.toIntExact(BinaryUtil.toNumber(BinaryUtil.toBinaryString(payloadLengthBytes, "")));
        }
//        System.out.println("=== fin:" + fin + ", payloadLength: " + payloadLength);

        // 获取掩码
        byte[] maskCode = new byte[4];
        is.read(maskCode);

        // 获取负载数据
        byte[] payload = new byte[payloadLength];
        is.read(payload);

        // 使用掩码解码负载数据
        for (int i = 0; i < payloadLength; i++) {
            payload[i] = (byte) (payload[i] ^ maskCode[i % 4]);
        }

        // 保存解码后的负载数据
        payloadData.write(payload);

        // 如果上面步骤解析的这个数据包不是最后一个数据包，就还要以同样的方式处理接下来的数据包
        if (!fin) {
            receiveWebSocketData(is, payloadData);
        }
    }

    private static String response(String requestHeader) {
        String response = "";
        Matcher matcher = Pattern.compile("Sec-WebSocket-Key: (.+)").matcher(requestHeader);
        byte[] encodedSecWebSocketKey;
        if (matcher.find()) {
            String secWebSocketKey = matcher.group(1);
            secWebSocketKey += "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            encodedSecWebSocketKey = encodeBySHA1(secWebSocketKey.getBytes());
            response += "HTTP/1.1 101 Switching Protocols\n";
            response += "Connection: Upgrade\n";
            response += "Upgrade: websocket\n";
            response += "Sec-WebSocket-Accept: " + Base64.getEncoder().encodeToString(encodedSecWebSocketKey) + "\n";
            response += "\n";
        } else {
            throw new IllegalArgumentException("Sec-WebSocket-Key not found, please connect with websocket protocol");
        }
        return response;
    }

    private static byte[] encodeBySHA1(byte[] b) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messageDigest.update(b);
        return messageDigest.digest();
    }

}
