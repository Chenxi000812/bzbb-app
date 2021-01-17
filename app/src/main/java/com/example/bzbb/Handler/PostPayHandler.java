package com.example.bzbb.Handler;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.GoodActivity;
import com.example.bzbb.Tasks.MyPayTask;
import com.example.bzbb.Utils.AlipayUtils.PayResult;

import java.util.Map;

public class PostPayHandler extends MyHandler{

    public PostPayHandler(Activity activity) {
        super(activity);
    }


    @Override
    public void handleMessage(Message msg) {
        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
        Toast.makeText(activity, payResult.getMemo(), Toast.LENGTH_SHORT).show();
        String code = payResult.getResultStatus();
        if (code.equals("9000")){
            JSONObject jsonObject = JSON.parseObject(payResult.getResult());
            jsonObject = jsonObject.getJSONObject("alipay_trade_app_pay_response");
            MyPayTask.notifyServicePaySuccess(activity,jsonObject.getString("out_trade_no"));
        }
        super.activity.finish();
    }
}
