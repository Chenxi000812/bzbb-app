package com.example.bzbb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.Cookie;
import com.example.bzbb.Model.UserInfo;
import com.example.bzbb.Utils.HttpUtils;
import com.example.bzbb.custom.VerificationCodeDialog;

import java.io.IOException;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.Response;

public class LoginActivity extends Activity {

    Button btnLogin;
    Button btnSendSms;
    EditText mobileEd;
    EditText codeEd;
    Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                UserInfo userInfo = JSON.parseObject((String) msg.obj).getJSONObject("principal").toJavaObject(UserInfo.class);
                persistentUserInfo(userInfo);
                Intent intent = new Intent();
                intent.putExtra("UserInfo",userInfo);
                HttpUtils.isLogin = true;
                HttpUtils.setLoginInfo(userInfo);
                LoginActivity.this.setResult(1,intent);
                LoginActivity.this.finish();

            }else {
                Toast.makeText(LoginActivity.this, (String) msg.obj, Toast.LENGTH_LONG).show();
            }

        }
    };
    Handler sendSmsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                AjaxResult ajaxResult = (AjaxResult) msg.obj;
                Toast.makeText(LoginActivity.this, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(LoginActivity.this, "发生异常", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //赋值区
        btnLogin = findViewById(R.id.button_login);
        btnSendSms = findViewById(R.id.button_sendSms);
        mobileEd = findViewById(R.id.et_mobile);
        codeEd = findViewById(R.id.et_password);


        //发送验证码按钮绑定事件
        btnSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VerificationCodeDialog.Builder(LoginActivity.this).setAfterBtnOnClick(new VerificationCodeDialog.Builder.afterBtnOnClick() {
                    @Override
                    public void processing(final String inputcode) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Response response = HttpUtils.sessionGet("/sms/getCode?mobile=" + mobileEd.getText().toString()+"&code="+inputcode);
                                HttpUtils.processHttpResponse(response,sendSmsHandler,AjaxResult.class);
                            }
                        }).start();

                    }
                }).create().show();
            }
        });


        //登录按钮绑定事件
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = mobileEd.getText().toString();
                String code = codeEd.getText().toString();
                final FormBody.Builder builder = new FormBody.Builder();
                builder.add("mobile", mobile);
                builder.add("smsCode", code);
                builder.add("remember-me","true");
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = HttpUtils.sessionPost("/sms/login", builder.build());
                        Message message = new Message();
                        message.what = -1;
                        message.obj = "验证码错误！";
                        try {
                            int status = response.code();
                            if (status == 200) {
                                message.obj = response.body().string();
                                message.what = 0;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            message.obj="发送错误请重试";
                        } finally {
                            loginHandler.sendMessage(message);
                        }

                    }
                })).start();
            }
        });
    }


    private void persistentUserInfo(UserInfo userInfo) {
        Map<String, Cookie> cookies = HttpUtils.getCookies();
        if (cookies.containsKey("remember-me")) {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginCache", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("UserInfo",JSON.toJSONString(userInfo));
            edit.apply();
        }
    }
}
