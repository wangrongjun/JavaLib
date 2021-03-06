package com.wangrj.java_lib.servlet;

import com.google.gson.Gson;
import com.wangrj.java_lib.java_util.CharsetUtil;
import com.wangrj.java_lib.java_util.DebugUtil;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.web.JsonResult;
import com.wangrj.java_lib.web.CustomHttpServlet;
import com.wangrj.java_lib.web.WebUtil;
import com.wangrj.java_lib.java_util.CharsetUtil;
import com.wangrj.java_lib.java_util.DebugUtil;
import com.wangrj.java_lib.java_util.FileUtil;
import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.java_lib.web.CustomHttpServlet;
import com.wangrj.java_lib.web.JsonResult;
import com.wangrj.java_lib.web.WebUtil;

import java.util.HashMap;

/**
 * by 王荣俊 on 2016/9/17.
 */
public class UploadText extends CustomHttpServlet {

    private JsonResult result;

    @Override
    protected String[] onGetParameterStart() {
        result = new JsonResult();
        return new String[]{"text"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        String text = (String) parameterMap.get("text");
        if (!TextUtil.isEmpty(text)) {

            text = CharsetUtil.decode(text);
            try {
                FileUtil.write(text, WebUtil.getWebappDir() + "ROOT/file/content.txt");
            } catch (Exception e) {
                result.setState(JsonResult.EXCEPTION);
                result.setResult(DebugUtil.getExceptionStackTrace(e));
                return;
            }

            result.setState(JsonResult.OK);
            result.setResult("text upload succeed!!!");

        } else {
            result.setState(JsonResult.ERROR);
            result.setResult("text is null!!!");
        }

    }

    @Override
    protected String onWriteResultStart() {
        return new Gson().toJson(result);
    }

}
