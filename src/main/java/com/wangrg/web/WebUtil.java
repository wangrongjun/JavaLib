package com.wangrg.web;

import java.io.File;

/**
 * by 王荣俊 on 2016/8/30.
 */
public class WebUtil {
    /**
     * @return 返回tomcat下的webapps目录路径
     */
    public static String getWebappDir() {
        String path;
        path = new File("a").getAbsolutePath();
        path = path.replace("bin\\a", "webapps\\");
        return path;
    }

}
