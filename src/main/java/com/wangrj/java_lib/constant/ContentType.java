package com.wangrj.java_lib.constant;

/**
 * by wangrongjun on 2018/8/14.
 */
public class ContentType {

    public static final String TEXT_PLAIN = "text/plain;charset=UTF-8";
    public static final String APPLICATION_JSON = "application/json";
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * [HTTP Content-type 对照表](http://tool.oschina.net/commons)
     */
    public static String toContentType(String fileExtension) {
        fileExtension = fileExtension.toLowerCase();
        switch (fileExtension) {
            case "txt":
                return "text/plain";
            case "html":
                return "text/html";
            case "xml":
                return "text/xml";
            case "pdf":
                return "application/pdf";
            case "bmp":
            case "gif":
            case "png":
                return "image/" + fileExtension;
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "mp3":
                return "audio/mp3";
            case "mp4":
                return "video/mpeg4";

            case "vsd":
                return "application/vnd.visio";
            case "ppt":
            case "pptx":
                return "application/vnd.ms-powerpoint";
            case "doc":
            case "docx":
                return "application/msword";
            default:
                return "application/octet-stream";
        }
    }

}
