package com.wangrj.java_lib.java_util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public Response request(String url) throws ResponseCodeNot200Exception, IOException {
        Response response = new Response(charsetName);

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
        if (returnResponseHeader) {
            response.responseHeader = new ResponseHeader(conn);
        }
        response.responseCode = conn.getResponseCode();
        if (response.responseCode == 200) {
            InputStream is = conn.getInputStream();
            if (is != null) {
                response.responseData = StreamUtil.toBytes(is);
            }
        } else {
            InputStream is = conn.getErrorStream();
            String errorMessage = null;
            if (is != null) {
                errorMessage = StreamUtil.readInputStream(is);
            }
            throw new ResponseCodeNot200Exception(response.responseCode, conn.getResponseMessage(), errorMessage);
        }

        return response;
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

        private Integer responseCode;
        private ResponseHeader responseHeader;
        private byte[] responseData;
        private String charsetName;

        public Response(String charsetName) {
            this.charsetName = charsetName;
        }

        public Integer getResponseCode() {
            return responseCode;
        }

        public ResponseHeader getResponseHeader() {
            return responseHeader;
        }

        public byte[] getResponseData() {
            return responseData;
        }

        public String toResponseText() {
            if (responseData != null && responseData.length > 0) {
                try {
                    return new String(responseData, charsetName);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }
    }

    public static class ResponseCodeNot200Exception extends Exception {
        private int responseCode;
        private String responseMessage;
        private String errorMessage;

        ResponseCodeNot200Exception(int responseCode, String responseMessage, String errorMessage) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.errorMessage = errorMessage;
        }

        @Override
        public String toString() {
            return ResponseCodeNot200Exception.class.getSimpleName() + ": " +
                    "responseCode = " + responseCode +
                    ", responseMessage = " + responseMessage +
                    ", errorMessage = " + errorMessage;
        }
    }

}
