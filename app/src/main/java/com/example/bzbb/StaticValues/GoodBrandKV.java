package com.example.bzbb.StaticValues;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoodBrandKV {
    private static Map<Integer,String> kv = new HashMap<>();

    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = HttpUtils.get("/app/getGoodbrand").body().string();
                    JSONArray objects = JSON.parseArray(res);
                    for (int i = 0 ; i < objects.size(); i ++){
                        JSONObject object = objects.getJSONObject(i);
                        Integer id = object.getInteger("id");
                        String name = object.getString("name");
                        kv.put(id,name);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }).start();


    }

    public static void init() {

    }

    public static String getType(int id){
        return kv.get(id);
    }
}
