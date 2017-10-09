package com.wangrg.java_lib.java_util;

import com.wangrg.java_lib.data_structure.Pair;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static final int OK = 0;
    public static final int NO_INTERNET = -1;//,UnknownHostException
    public static final int TIME_OUT = -2;//SocketTimeOutException
    public static final int SERVER_ERROR = -3;//ConnectException,FileNotFoundException
    public static final int UNKNOWN_ERROR = -4;

    // 网络连接，读取的限时
    private static int CONNECT_TIMEOUT = 7 * 1000;
    private static int READ_TIMEOUT = 5 * 1000;

    public static Result request(String url) {
        return new HttpRequest().request(url);
    }

    /**
     * GET,POST均可
     * 注意：url不能带参数，否则2Mb以下的文件会上传失败（服务器会把字节流当作url的参数列表来处理，并提示乱码）
     * 在doGet/Post中，可以通过request.getHeader("fileName")获取文件名
     */
    public static Result upload(String url, String requestMethod, String fileName, InputStream isFromClient, String charsetName) {
        Result r = new Result();
        OutputStream osToServer;

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setUseCaches(false);// 设置不使用缓存（容易出现问题）

            conn.setRequestProperty("fileName", fileName);

            if (isFromClient != null) {
                conn.setDoOutput(true);
                osToServer = conn.getOutputStream();
                int len;
                byte[] buf = new byte[1024];
                while ((len = isFromClient.read(buf)) != -1) {
                    osToServer.write(buf, 0, len);
                }
                osToServer.flush();
                osToServer.close();
                isFromClient.close();
            }

            r.size = TextUtil.transferFileLength(conn.getContentLength(), 2);
            r.responseCode = conn.getResponseCode();
            r.result = StreamUtil.readInputStream(conn.getInputStream(), charsetName);
            r.state = OK;

        } catch (UnknownHostException e) {
            r.state = NO_INTERNET;
            r.result = e.toString();

        } catch (SocketTimeoutException e) {
            r.state = TIME_OUT;
            r.result = e.toString();

        } catch (ConnectException | FileNotFoundException e) {
            r.state = SERVER_ERROR;
            r.result = e.toString();

        } catch (Exception e) {
            r.state = UNKNOWN_ERROR;
            r.result = e.toString();

        }

        return r;

    }

    /**
     * @return 返回-1:网络不可用。0：文件不存在。成功则返回大于0的数字
     */
    public static int getDownloadFileLength(String url) {
        int length;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            length = conn.getContentLength();
            if (length <= 0) {
                return -1;
            }
            return length;

        } catch (Exception e) {
            DebugUtil.println("ErrorLine: " + DebugUtil.getErrorLine(e) + "\n" + e.toString());
            return -1;
        }
    }

    public interface OnGetLengthFinishedListener {
        void onGetLengthFinished(int length);
    }

    /**
     * 该方法会启动新线程
     * 返回-1:网络不可用。0：文件不存在。成功则返回大于0的数字
     */
    public static void getDownloadFileLength(final String url, final OnGetLengthFinishedListener listener) {
        new Thread() {
            @Override
            public void run() {
                int length = getDownloadFileLength(url);
                listener.onGetLengthFinished(length);
            }
        }.start();
    }

    public static class HttpRequest {

        private String requestMethod = "GET";
        private String output;
        private String charsetName = "utf-8";
        private int connectTimeOut = CONNECT_TIMEOUT;
        private int readTimeOut = READ_TIMEOUT;
        private List<Pair<String, String>> requestPropertyList;

        public HttpRequest() {
            requestPropertyList = new ArrayList<>();
        }

        public HttpRequest setRequestMethod(String requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public HttpRequest setOutput(String output) {
            this.output = output;
            return this;
        }

        public HttpRequest setCookie(String cookie) {
            requestPropertyList.add(new Pair<>("Set-Cookie", cookie));
            return this;
        }

        public HttpRequest setUserAgent(String userAgent) {
            requestPropertyList.add(new Pair<>("User-Agent", userAgent));
            return this;
        }

        public HttpRequest setFirefoxUserAgent() {
            String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) " +
                    "Gecko/20080404 Firefox/2.0.0.14";
            requestPropertyList.add(new Pair<>("User-Agent", userAgent));
            return this;
        }

        public HttpRequest setCharsetName(String charsetName) {
            this.charsetName = charsetName;
            return this;
        }

        public HttpRequest addRequestProperty(String key, String value) {
            requestPropertyList.add(new Pair<>(key, value));
            return this;
        }

        public HttpRequest setConnectTimeOut(int time) {
            connectTimeOut = time;
            return this;
        }

        public HttpRequest setReadTimeOut(int time) {
            readTimeOut = time;
            return this;
        }

        public Result request(String url) {
            Result r = new Result();

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod(requestMethod);
                conn.setDoInput(true);
                conn.setDoOutput(!TextUtil.isEmpty(output));
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);

                for (Pair<String, String> requestProperty : requestPropertyList) {
                    conn.setRequestProperty(requestProperty.first, requestProperty.second);
                }

                if (output != null && output.length() > 0) {
                    OutputStream os = conn.getOutputStream();
                    os.write(output.getBytes(charsetName));
                    os.flush();
                    os.close();
                }

//                r.headerFields = conn.getHeaderFields();
                r.setCookie = conn.getHeaderField("Set-Cookie");
                r.cookie = conn.getHeaderField("Cookie");
                r.size = TextUtil.transferFileLength(conn.getContentLength(), 2);
                r.responseCode = conn.getResponseCode();

                r.result = StreamUtil.readInputStream(conn.getInputStream(), charsetName);
                r.state = OK;

            } catch (UnknownHostException | ConnectException e) {
                r.state = NO_INTERNET;
                r.result = e.toString();

            } catch (SocketTimeoutException e) {
                r.state = TIME_OUT;
                r.result = e.toString();

            } catch (Exception e) {
                r.state = UNKNOWN_ERROR;
                r.result = e.toString();
            }

            return r;
        }

    }

    public static class Result {
        public int state;
        public int responseCode;
        public String cookie;
        public String setCookie;
        public String result;
        public String size;
        Map<String, List<String>> headerFields;
    }

}
