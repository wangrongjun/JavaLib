package com.wangrj.java_lib.java_program.ocr_baidu;

import com.wangrj.java_lib.java_util.*;
import com.wangrj.java_lib.java_util.Base64;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * by wangrongjun on 2017/9/20.
 */
public class OCRBaidu {

    class OCRResult {
        String log_id;
        int words_result_num;
        List<String> words_result;

        String error_code;
        String error_msg;
    }

    public static void main(String[] args) throws IOException {
//        ConfigUtil.read(Config.class, "config.txt", true);

        byte[] bytes = StreamUtil.toBytes(new FileInputStream("E:/Test/1.jpg"));
        String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        String requestBody = "access_token=" + Config.accessToken + "&image=" + base64;
        HttpRequest.Response response = new HttpRequest().
                setRequestHeader("Content-Type", "application/x-www-form-urlencoded").
                setRequestBody(requestBody.getBytes()).
                returnResponseHeader(true).
                request(Config.url);

        GsonUtil.printFormatJson(response);
    }

}
