package com.example.bzbb.Tasks;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.example.bzbb.GoodActivity;
import com.example.bzbb.Handler.NotifyServicePaySuccessHandler;
import com.example.bzbb.Handler.PostPayHandler;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.CreateOrderRequestModel;
import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.Utils.AlipayUtils.OrderInfoUtil2_0;
import com.example.bzbb.Utils.HttpUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyPayTask {

    public static Response createOrder(CreateOrderRequestModel model){
        return HttpUtils.sessionPostForJSON("/usr/createOrder", JSON.toJSONString(model));
    }

    public static void payTask(final Activity activity, final String orderInfo){
        final PostPayHandler postPayHandler = new PostPayHandler(activity);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String,String> result = alipay.payV2(orderInfo,true);
                Message message = new Message();
                message.obj = result;
                postPayHandler.sendMessage(message);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    public static void notifyServicePaySuccess(final Activity activity, final String out_trade_no){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = HttpUtils.sessionGet("/usr/paySuccess?outTradeNo="+out_trade_no);
                HttpUtils.processHttpResponse(response,new NotifyServicePaySuccessHandler(activity), AjaxResult.class);
            }
        }).start();
    }
}
