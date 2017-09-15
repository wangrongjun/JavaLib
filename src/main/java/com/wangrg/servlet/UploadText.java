package com.wangrg.servlet;

import com.google.gson.Gson;
import com.wangrg.java_util.CharsetUtil;
import com.wangrg.java_util.DebugUtil;
import com.wangrg.java_util.FileUtil;
import com.wangrg.java_util.TextUtil;
import com.wangrg.web.JsonResult;
import com.wangrg.web.CustomHttpServlet;
import com.wangrg.web.WebUtil;

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
