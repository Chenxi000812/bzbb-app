package com.example.bzbb.Handler;

import android.app.Activity;
import android.os.Message;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Tasks.MyPayTask;

import java.math.BigDecimal;

public class CreateOrderHandler extends MyHandler {


    public CreateOrderHandler(Activity activity) {
        super(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 0) {
            AjaxResult ajaxResult = (AjaxResult) msg.obj;
            if (ajaxResult.getSuccess()) {
                MyPayTask.payTask(activity, ajaxResult.getObject());
            } else {
                Toast.makeText(activity, ajaxResult.getMsg(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity, "出现异常", Toast.LENGTH_LONG).show();
        }


    }
}
