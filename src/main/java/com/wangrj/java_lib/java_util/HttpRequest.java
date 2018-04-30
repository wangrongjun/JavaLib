package com.wangrj.java_lib.java_util;

import java.io.OutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2018/4/29.
 */
public class HttpRequest {

    private int connectTimeOut = 0;// 如果小于等于0，就不限时
    private int readTimeOut = 0;// 如果小于等于0，就不限时
    private String charsetName = "UTF-8";
    private String requestMethod = "GET";
    private Map<String, String> requestHeaderMap = new HashMap<>();
    private byte[] requestBody;
    private boolean returnResponseHeader = false;

    public HttpRequest setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    public HttpRequest setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public HttpRequest setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        return this;
    }

    public HttpRequest setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    /**
     * For example, "text/html" , "application/json" , "application/x-www-form-urlencoded"
     */
    public HttpRequest setContentType(String contentType) {
        requestHeaderMap.put("Content-Type", contentType);
        return this;
    }

    public HttpRequest setCookie(String cookie) {
        requestHeaderMap.put("Set-Cookie", cookie);
        return this;
    }

    public HttpRequest setUserAgent(String userAgent) {
        requestHeaderMap.put("User-Agent", userAgent);
        return this;
    }

    public HttpRequest setRequestHeader(String key, String value) {
        requestHeaderMap.put(key, value);
        return this;
    }

    public HttpRequest setRequestHeaders(Map<String, String> requestHeaders) {
        requestHeaderMap.putAll(requestHeaders);
        return this;
    }

    public HttpRequest setRequestBody(byte[] requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public HttpRequest returnResponseHeader(boolean returnResponseHeader) {
        this.returnResponseHeader = returnResponseHeader;
        return this;
    }

    /**
     * 注意：在上传文件时，url不能带参数，否则2Mb以下的文件会上传失败（服务器会把字节流当作url的参数列表来处理，并提示乱码）
     */
    public Response request(String url) {
        Response response = new Response();
        try {
            // 请求初始化
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.setDoOutput(requestBody != null && requestBody.length > 0);
            if (connectTimeOut > 0) {
                conn.setConnectTimeout(connectTimeOut);
            }
            if (readTimeOut > 0) {
                conn.setReadTimeout(readTimeOut);
            }
            conn.setUseCaches(false);// 设置不使用缓存（否则上传文件时容易出现问题）

            // 设置请求头
            for (Map.Entry<String, String> entry : requestHeaderMap.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // 设置请求体
            if (requestBody != null && requestBody.length > 0) {
                OutputStream os = conn.getOutputStream();
                os.write(requestBody);
                os.flush();
                os.close();
            }

            // 获取请求的响应信息
            response.responseCode = conn.getResponseCode();
            if (returnResponseHeader) {
                response.responseHeader = new ResponseHeader(conn);
            }
            response.responseData = StreamUtil.readInputStream(conn.getInputStream(), charsetName);
            response.status = Status.SUCCESS;
        } catch (UnknownHostException e) {
            response.status = Status.NO_INTERNET;
            response.exception = e;
        } catch (ConnectException e) {
            response.status = Status.SERVER_ERROR;
            response.exception = e;
        } catch (SocketTimeoutException e) {
            response.status = Status.TIME_OUT;
            response.exception = e;
        } catch (Exception e) {
            response.status = Status.UNKNOWN_ERROR;
            response.exception = e;
        }
        return response;
    }

    public enum Status {
        SUCCESS(0),
        NO_INTERNET(-1),
        TIME_OUT(-2),
        SERVER_ERROR(-3),
        UNKNOWN_ERROR(-4);

        private int code;

        Status(int code) {
            this.code = code;
        }
    }

    public static class ResponseHeader {

        private int contentLength;
        private String contentType;
        private String cookie;
        private String setCookie;
        private Map<String, List<String>> headerFields;

        private ResponseHeader(HttpURLConnection conn) {
            this.headerFields = conn.getHeaderFields();
            this.setCookie = conn.getHeaderField("Set-Cookie");
            this.cookie = conn.getHeaderField("Cookie");
            this.contentLength = conn.getContentLength();
        }

        public int getContentLength() {
            return contentLength;
        }

        public void setContentLength(int contentLength) {
            this.contentLength = contentLength;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getSetCookie() {
            return setCookie;
        }

        public void setSetCookie(String setCookie) {
            this.setCookie = setCookie;
        }

        public Map<String, List<String>> getHeaderFields() {
            return headerFields;
        }

        public void setHeaderFields(Map<String, List<String>> headerFields) {
            this.headerFields = headerFields;
        }
    }

    public static class Response {

        private Status status;
        private Exception exception;
        private int responseCode;
        private ResponseHeader responseHeader;
        private String responseData;

        public Status getStatus() {
            return status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public ResponseHeader getResponseHeader() {
            return responseHeader;
        }

        public void setResponseHeader(ResponseHeader responseHeader) {
            this.responseHeader = responseHeader;
        }

        public String getResponseData() {
            return responseData;
        }

        public void setResponseData(String responseData) {
            this.responseData = responseData;
        }
    }

}
