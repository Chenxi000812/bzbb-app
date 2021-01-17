package com.example.bzbb.StaticValues;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Model.Fastmail;
import com.example.bzbb.Utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FastMailKV {
    public static Map<Integer, Fastmail> kv = new HashMap<>();
    static {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = HttpUtils.get("/app/getFastMail").body().string();
                    List<Fastmail> fastmails = JSON.parseArray(res).toJavaList(Fastmail.class);
                    for (Fastmail fastmail:fastmails){
                        kv.put(fastmail.getId(),fastmail);
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

    public static Fastmail getFastMail(int id){
        return kv.get(id);
    }

    public static String[] getFastMailNames(List<Fastmail> fastmails){
        String[] s = new String[fastmails.size()];
        for (int i = 0; i< fastmails.size();i++){
            s[i] = fastmails.get(i).getName();
        }
        return s;
    }

    public static List<Fastmail> getFastMailList(){
        Collection<Fastmail> values = kv.values();
        return new ArrayList<>(values);
    }
}
