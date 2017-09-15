package com.wangrg.java_program.shopping_system.servlet;

import com.wangrg.java_util.GsonUtil;
import com.wangrg.java_util.TextUtil;
import com.wangrg.java_program.shopping_system.Response;
import com.wangrg.java_program.shopping_system.StateCode;
import com.wangrg.java_program.shopping_system.bean.Good;
import com.wangrg.java_program.shopping_system.dao.GoodDao;
import com.wangrg.web.CustomHttpServlet;

import java.util.HashMap;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 * http://localhost:8080/shopping_system/getGoodImageList?goodId=1
 */
public class GetGoodListByShopIdServlet extends CustomHttpServlet {

    private Response<List<Good>> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"shopId"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        String shopId = (String) parameterMap.get("shopId");

        if (TextUtil.isEmpty(shopId)) {
            response = new Response<>(StateCode.PARAM_ERROR, "shopId����Ϊ��");
            return;
        }

        try {
            GoodDao goodDao = new GoodDao();
            List<Good> goodList = goodDao.queryGoodListByShopId(Integer.parseInt(shopId));
            response = new Response<>(StateCode.OK, null, goodList);

        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.ERROR_UNKNOWN, e.toString());
        }

    }

    @Override
    protected String onWriteResultStart() {
        return GsonUtil.formatJson(response);
    }
}
