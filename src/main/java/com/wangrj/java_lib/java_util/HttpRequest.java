package com.wangrj.java_lib.java_util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * by wangrongjun on 2018/4/29.
 */
public class HttpRequest {

    private int connectTimeOut = 0;// 如果小于等于0，就不限时
    private int readTimeOut = 0;// 如果小于等于0，就不限时
    private String charsetName = "UTF-8";
    private String requestMethod = "GET";
    private Map<String, String> requestHeaderMap = new HashMap<>();
    private ByteArrayOutputStream requestBody;
    private boolean returnResponseHeader = false;
    private String boundary;// 如果不为空，说明是请求体是 MultipartFormData 格式

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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(requestBody);
            baos.flush();
            if (this.requestBody != null) {
                this.requestBody.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.requestBody = baos;
        return this;
    }

    public HttpRequest setRequestBody(String body) {
        setRequestBody(body.getBytes());
        return this;
    }

    public HttpRequest returnResponseHeader(boolean returnResponseHeader) {
        this.returnResponseHeader = returnResponseHeader;
        return this;
    }

    private void setMultipartFormData() {
        if (boundary == null) {
            boundary = "--" + UUID.randomUUID().toString() + "--";
        }
        requestHeaderMap.put("Content-Type", "multipart/form-data; boundary=" + boundary);
    }

    public HttpRequest addMultipartField(String name, String value) throws IOException {
        setMultipartFormData();
        if (requestBody == null) {
            requestBody = new ByteArrayOutputStream();
        }
        requestBody.write(("--" + boundary + "\r\n").getBytes());
        requestBody.write(("Content-Disposition: form-data; name=\"" + name + "\"" + "\r\n").getBytes());
        requestBody.write("\r\n".getBytes());
        requestBody.write((value + "\r\n").getBytes());
        return this;
    }

    public HttpRequest addMultipartFile(String name, String fileName, byte[] file) throws IOException {
        setMultipartFormData();
        if (requestBody == null) {
            requestBody = new ByteArrayOutputStream();
        }
        requestBody.write(("--" + boundary + "\r\n").getBytes());
        requestBody.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + fileName + "\"\r\n").getBytes());
        requestBody.write("Content-Type: application/octet-stream\r\n".getBytes());
        requestBody.write("\r\n".getBytes());
        requestBody.write(file);
        requestBody.write("\r\n".getBytes());
        return this;
    }

    public HttpRequest addMultipartFile(String name, String fileName, InputStream file) throws IOException {
        return addMultipartFile(name, fileName, StreamUtil.toBytes(file));
    }

    public HttpRequest addMultipartFile(String name, String fileName, String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return addMultipartFile(name, fileName, StreamUtil.toBytes(fis));
        }
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
        conn.setDoOutput(requestBody != null && requestBody.size() > 0);
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
        if (requestBody != null && requestBody.size() > 0) {
            if (boundary != null) {// 如果请求体是 MultipartFormData 的格式，需要补上最后的 boundary
                requestBody.write(("--" + boundary + "--\r\n").getBytes());
            }
            OutputStream os = conn.getOutputStream();
            os.write(requestBody.toByteArray());
            os.flush();
            os.close();
            requestBody.close();
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

        public String toResponseHeaderString() {
            ResponseHeader responseHeader = getResponseHeader();
            if (responseHeader == null) {
                return null;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("Content-Length: ").append(responseHeader.getContentLength()).append("\n");
            if (responseHeader.getContentType() != null) {
                builder.append("Content-Type: ").append(responseHeader.getContentType()).append("\n");
            }
            if (responseHeader.getCookie() != null) {
                builder.append("Cookie: ").append(responseHeader.getCookie()).append("\n");
            }
            if (responseHeader.getSetCookie() != null) {
                builder.append("Set-Cookie: ").append(responseHeader.getSetCookie()).append("\n");
            }
            Map<String, List<String>> headerFields = responseHeader.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                String value = null;
                if (entry.getValue() != null) {
                    value = entry.getValue().stream().collect(Collectors.joining(" ||| "));
                }
                builder.append(entry.getKey()).append(": ").append(value).append("\n");
            }
            return builder.toString();
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
