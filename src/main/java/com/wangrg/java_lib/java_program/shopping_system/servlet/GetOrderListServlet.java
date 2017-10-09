package com.wangrg.java_lib.java_program.shopping_system.servlet;

import com.wangrg.java_lib.java_util.GsonUtil;
import com.wangrg.java_lib.java_program.shopping_system.Response;
import com.wangrg.java_lib.java_program.shopping_system.StateCode;
import com.wangrg.java_lib.java_program.shopping_system.bean.Orders;
import com.wangrg.java_lib.java_program.shopping_system.dao.OrdersDao;
import com.wangrg.java_lib.web.CustomHttpServlet;

import java.util.HashMap;
import java.util.List;

/**
 * by wangrongjun on 2016/12/16.
 */
public class GetOrderListServlet extends CustomHttpServlet {

    private Response<List<Orders>> response;

    @Override
    protected String[] onGetParameterStart() {
        return new String[]{"userId"};
    }

//    @Override
    protected void onGetParameterFinish(HashMap parameterMap) {

        int userId;
        try {
            userId = Integer.parseInt((String) parameterMap.get("userId"));
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>(StateCode.PARAM_ERROR, "����������");
            return;
        }

        try {
            List<Orders> orderList = new OrdersDao().query(userId);
            response = new Response<>(StateCode.OK, null, orderList);
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
