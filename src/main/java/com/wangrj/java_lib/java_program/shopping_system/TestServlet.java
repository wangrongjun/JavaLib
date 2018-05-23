package com.wangrj.java_lib.java_program.shopping_system;

import com.wangrj.java_lib.java_util.DebugUtil;
import com.wangrj.java_lib.java_util.GsonUtil;
import com.wangrj.java_lib.java_util.HttpRequest;
import org.junit.Test;

/**
 * by wangrongjun on 2017/5/2.
 */
public class TestServlet {

    @Test
    public void testRegister() {
        String url = "http://localhost:8080/shopping_system/register?phone=15521302230&password=123";
        HttpRequest.Response response = new HttpRequest().request(url);
        DebugUtil.println(url + "\n" + GsonUtil.printPrettyJson(response));
    }

}
