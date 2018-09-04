package com.wangrj.java_lib.test;

import com.wangrj.java_lib.java_util.HttpRequest;

import java.io.IOException;
import java.util.UUID;

public class JavaLibTestClass {

    public static void main(String[] args) throws IOException, HttpRequest.ResponseCodeNot200Exception {
        HttpRequest.Response response = new HttpRequest().
                setRequestMethod("POST").
                addMultipartField("param1", "abc").
                addMultipartField("param2", "def").
                addMultipartFile("file1", "a.zip", "E:/Test/a.zip1").
                addMultipartFile("file2", "b.zip", "E:/Test/b.zip1").
                request("http://119.29.102.44:8080/testUploadFile");
        System.out.println(response.toResponseText());
    }

}
