package com.wangrg.java_lib.java_program.app_upgrade;

import com.google.gson.Gson;
import com.wangrg.java_lib.java_program.app_upgrade.bean.AppLatestVersion;
import com.wangrg.java_lib.java_util.FileUtil;
import com.wangrg.java_lib.web.JsonResult;
import com.wangrg.java_lib.web.CustomHttpServlet;

import java.util.HashMap;

/**
 * by 王荣俊 on 2016/10/9.
 * http://localhost:8080/AppUpgrade/queryLatest?appName=CatchCrazyCat
 */
public class QueryLatest extends CustomHttpServlet {

    private JsonResult result;

    @Override
    protected String[] onGetParameterStart() {
        result = new JsonResult();
        return new String[]{"appName"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        try {
            String appName = (String) parameterMap.get("appName");

            String appDir = getWebAppDir() + "apps\\" + appName + "\\";
            int latestVersionCode = Integer.parseInt(FileUtil.read(appDir + "\\latestVersionCode.txt"));

            String latestDir = appDir + latestVersionCode + "\\";
            String versionName = FileUtil.read(latestDir + "\\versionName.txt");
            String description = FileUtil.read(latestDir + "\\description.txt", "gbk");
            String apkFileUrl = FileUtil.read(latestDir + "\\apkFileUrl.txt", "gbk");

            AppLatestVersion message = new AppLatestVersion(latestVersionCode, versionName,
                    description, apkFileUrl);

            result.setState(JsonResult.OK);
            result.setResult(new Gson().toJson(message));

        } catch (Exception e) {
            result.setState(JsonResult.EXCEPTION);
            result.setResult(e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return new Gson().toJson(result);
    }

}

