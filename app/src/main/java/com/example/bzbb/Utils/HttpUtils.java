package com.example.bzbb.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bzbb.Model.Cookie;
import com.example.bzbb.Model.UserInfo;
import com.example.bzbb.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static final String host = "http://39.99.205.36:8080"; //服务器 http://39.99.205.36:8080  本地 192.168.0.105:8080

    public static final String imgUrl = host+"/img/";

    public static boolean isLogin = false;

    public static View view;

    private static Map<String, Cookie> cookies = new HashMap<>();

    public static SharedPreferences loginCache;

    public static OkHttpClient client = new OkHttpClient.Builder().followRedirects(false).followSslRedirects(false).build();

    public static Map<String, Cookie> getCookies(){
        return cookies;
    }

    public static Handler checkTokenIsExpire = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==-1){
                loginout();
            }else {
                isLogin = true;
                HttpUtils.setLoginInfo(JSON.parseObject(loginCache.getString("UserInfo",""),UserInfo.class));
            }
        }
    };

    public static void initLoginInfo(){
        if (loginCache.contains("UserInfo")&&loginCache.contains("remember-me")){
            HttpUtils.putCookie("remember-me",loginCache.getString("remember-me",""));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Response response = HttpUtils.sessionGet("/usr");
                    Message message = new Message();
                    if (response.code()==302){
                        message.what=-1;
                    }else {
                        message.what=0;
                    }
                    checkTokenIsExpire.sendMessage(message);
                }
            }).start();
        }
    }

    public static Response get(String url){
        Request request = new Request.Builder()
                .url(host+url)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response post(String url, FormBody formBody){
        Request request = new Request.Builder()
                .url(host+url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA);

    public static void loadImg(Context context, final String name, final ImageView imageView){
        Glide.with(context).asBitmap().load(imgUrl+name).apply(requestOptions).into(imageView);
    }

    public static void loadImg(View context, final String name, final ImageView imageView){
       loadImg(context.getContext(),name,imageView);
    }

    public static Response sessionGet(String url){
        Request.Builder builder;
        if (cookies.isEmpty()){
            builder = new Request.Builder();
        }else {
            builder = preRequest();
        }
        Request request = builder.url(host+url)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            afterRequest(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Response sessionPostForJSON(String url, String json){
        Request.Builder builder;
        if (cookies.isEmpty()){
            builder = new Request.Builder();
        }else {
            builder = preRequest();
        }
        MediaType parse = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(parse,json);
        Request request = builder.url(host+url)
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            afterRequest(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response sessionPost(String url, FormBody formBody){
        Request.Builder builder;
        if (cookies.isEmpty()){
            builder = new Request.Builder();
        }else {
            builder = preRequest();
        }
        Request request = builder.url(host+url)
                .post(formBody)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            afterRequest(response);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Request.Builder preRequest(){
        Request.Builder builder = new Request.Builder();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,Cookie> entry:cookies.entrySet()){
            Cookie cookie = entry.getValue();
            sb.append(cookie.getKey());
            sb.append("=");
            sb.append(cookie.getValue());
            sb.append("; ");
        }
        sb.substring(0,sb.length()-2);
        builder.addHeader("Cookie",sb.toString());
        return builder;
    }

    private static void afterRequest(Response response){
        List<String> headers = response.headers("Set-Cookie");
        if (headers.size() == 0){
            return;
        }
        for (String cookieString : headers){
            Cookie cookie = new Cookie();
            String[] split = cookieString.split("; ");
            String[] kv = split[0].split("=");
            if (kv.length < 2){
                continue;
            }
            cookie.setKey(kv[0]);
            cookie.setValue(kv[1]);
            //代表回话失效及时更新remember-me
            if (cookie.getKey().equals("remember-me")){
                updateRememberMe(cookie.getValue());
            }
            cookies.put(kv[0],cookie);
        }
    }

    public static void putCookie(String key,Cookie cookie){
        cookies.put(key,cookie);
    }

    public static void putCookie(String key,String value){
        Cookie cookie = new Cookie();
        cookie.setKey(key);
        cookie.setValue(value);
        cookies.put(key,cookie);
    }



    public static void setLoginInfo(UserInfo userInfo){
        TextView loginTx1 = view.findViewById(R.id.loginTx1);
        TextView loginTx2 = view.findViewById(R.id.loginTx2);
        loginTx1.setText("欢迎您");
        loginTx2.setText(userInfo.getNickname());
    }



    public static void processHttpResponse(Response response, Handler handler, Class<?> c){
        Message message = new Message();
        message.what = -1;
        try {
            if (response.code() == 200) {
                String s = response.body().string();
                message.obj = JSON.parseObject(s, c);
                message.what = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handler.sendMessage(message);
        }
    }
    public static void processHttpResponse(Response response, Handler handler, Class<?> c,int... arg){
        Message message = new Message();
        message.arg1 = arg[0];
        if (arg.length == 2){
            message.arg2 = arg[1];
        }
        message.what = -1;
        try {
            if (response.code() == 200) {
                String s = response.body().string();
                message.obj = JSON.parseObject(s, c);
                message.what = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handler.sendMessage(message);
        }
    }

    public static void updateRememberMe(String cookie){
        SharedPreferences.Editor edit = loginCache.edit();
        edit.putString("remember-me",cookie);
        edit.apply();
    }

    public static void loginout(){
        isLogin = false;
        loginCache.edit().clear().apply();
        TextView loginTx1 = view.findViewById(R.id.loginTx1);
        TextView loginTx2 = view.findViewById(R.id.loginTx2);
        loginTx1.setText("请登录");
        loginTx2.setText("请登录");
        new Thread(new Runnable() {
            @Override
            public void run() {
                sessionGet("/logout");
            }
        });
    }
}
