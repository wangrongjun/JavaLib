package com.wangrj.java_lib.web;

import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class CustomUploadServlet extends HttpServlet {

    /**
     * @return 文件保存路径，若为空，则不保存
     */
    protected abstract String onGetFilePath(String requestCookie, String fileName);

    protected abstract void onUploadFinish(boolean success, String msg);

    protected abstract String onWriteResultStart();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String charset = "utf-8";
        request.setCharacterEncoding(charset);
        response.setCharacterEncoding(charset);

//            1.获取cookie
        String requestCookie = request.getHeader("Set-Cookie");

//            2.获取上传文件的文件名
        String fileName = request.getHeader("fileName");

        String filePath = onGetFilePath(requestCookie, fileName);
        if (!TextUtil.isEmpty(filePath)) {
//            3.开始读取上传的文件
            try {
                InputStream is = request.getInputStream();
                FileOutputStream fos = new FileOutputStream(filePath);
                int len;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.flush();
                fos.close();
                is.close();

                onUploadFinish(true, fileName);

            } catch (Exception e) {
                e.printStackTrace();
                onUploadFinish(false, e.toString());
            }
        }

//        4.调用onWriteResultStart获取数据，并写出到response的输出流
        String out = onWriteResultStart();
        if (!TextUtil.isEmpty(out)) {
            OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), charset);
            osw.write(out);
            osw.flush();
            osw.close();
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    protected String getWebAppDir() {
        return getServletContext().getRealPath("") + File.separator;
    }

    protected String getWebAppName() {
        String appDir = getWebAppDir();
        String[] split = appDir.split("webapps");
        return TextUtil.correctFileName(split[1], "");
    }

}
