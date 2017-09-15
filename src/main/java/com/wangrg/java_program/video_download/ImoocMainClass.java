package com.wangrg.java_program.video_download;

import com.wangrg.java_program.video_download.bean.Course;
import com.wangrg.java_program.video_download.website.Imooc;
import com.wangrg.java_util.FileUtil;
import com.wangrg.java_util.FreeMakerUtil;
import com.wangrg.java_util.GsonUtil;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * by wangrongjun on 2017/8/15.
 */

public class ImoocMainClass {

    public static void main(String[] args) throws Exception {
        String url = "http://www.imooc.com/learn/337";
        File cookieFile = new File("E:/ImoocCookie.txt");
        if (!cookieFile.exists()) {
            cookieFile.createNewFile();
        }
        Imooc imooc = new Imooc(url, Imooc.VIDEO_QUALITY_M, FileUtil.read(cookieFile));
        Course course = imooc.getCourse();
        FileUtil.write(GsonUtil.formatJson(course), "E:/course.txt");
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("course", course);
        FreeMakerUtil.convert(new File("E:/"), "index.ftl", dataModel, new FileWriter("E:/output.html"));
    }

}
