package com.example.bzbb.Handler;

import android.app.Activity;
import android.os.Message;
import android.widget.Toast;

import com.example.bzbb.Model.AjaxResult;

public class NotifyServicePaySuccessHandler extends MyHandler {

    public NotifyServicePaySuccessHandler(Activity activity) {
        super(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what==0){
            AjaxResult ajaxResult = (AjaxResult) msg.obj;
            if (ajaxResult.getSuccess()){
                Toast.makeText(activity, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(activity, "出现异常", Toast.LENGTH_SHORT).show();
        }
    }

}
