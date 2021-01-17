package com.example.bzbb.Utils;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Model.OrderInfo;

import java.net.IDN;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CartListUtils {

    private static SharedPreferences cartListCache;
    private static SharedPreferences.Editor editor;

    public static void init(SharedPreferences sharedPreferences){
        cartListCache = sharedPreferences;
        editor = cartListCache.edit();
    }

    public static void insert(OrderInfo orderInfo){
        String cart = cartListCache.getString(String.valueOf(orderInfo.getId()),null);
        if (cart == null){
            editor.putString(String.valueOf(orderInfo.getId()),JSON.toJSONString(orderInfo)).apply();
        }else {
            OrderInfo ord = JSON.parseObject(cart).toJavaObject(OrderInfo.class);
            ord.setCount(ord.getCount()+orderInfo.getCount());
            editor.putString(String.valueOf(orderInfo.getId()),JSON.toJSONString(ord)).apply();
        }

    }

    public static void add(Long id){
        String cart = cartListCache.getString(String.valueOf(id),null);
        if (cart!=null){
            OrderInfo ord = JSON.parseObject(cart).toJavaObject(OrderInfo.class);
            ord.setCount(ord.getCount()+1);
            editor.putString(String.valueOf(id),JSON.toJSONString(ord)).apply();
        }
    }

    public static void sub(Long id){
        String cart = cartListCache.getString(String.valueOf(id),null);
        if (cart!=null){
            OrderInfo ord = JSON.parseObject(cart).toJavaObject(OrderInfo.class);
            ord.setCount(ord.getCount()-1);
            editor.putString(String.valueOf(id),JSON.toJSONString(ord)).apply();
        }
    }

    public static void remove(Long id){
        cartListCache.edit().remove(String.valueOf(id)).apply();
    }


    public static List<OrderInfo> get(){
        Map<String, ?> all = cartListCache.getAll();
        List<OrderInfo> orderInfos = new ArrayList<>();
        for (String key :all.keySet()){
            String s = cartListCache.getString(key,null);
            orderInfos.add(JSON.parseObject(s).toJavaObject(OrderInfo.class));
        }
        return orderInfos;
    }

}
