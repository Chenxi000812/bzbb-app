package com.example.bzbb.StaticValues;

import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Utils.HttpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodTypeKV {
    public static Map<Integer, String> kv = new HashMap<>();

    static {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = null;
                try {
                    res = HttpUtils.get("/app/getGoodtype").body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                JSONArray objects = JSON.parseArray(res);
                for (int i = 0; i < objects.size(); i++) {
                    JSONObject object = objects.getJSONObject(i);
                    Integer id = object.getInteger("id");
                    String name = object.getString("name");
                    kv.put(id, name);
                }
            }
        }).start();


    }

    public static void init() {

    }

    public static String getType(int id) {
        return kv.get(id);
    }
}
