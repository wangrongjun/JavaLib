package com.wangrj.java_lib.java_util;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    /**
     * @return 返回-1:网络不可用。0：文件不存在。成功则返回大于0的数字
     */
    public static int getDownloadFileLength(String url) {
        int length;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
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

}
