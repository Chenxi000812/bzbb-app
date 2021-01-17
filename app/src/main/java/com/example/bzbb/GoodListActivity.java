package com.example.bzbb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bzbb.Adapters.GoodAdapter;
import com.example.bzbb.Adapters.GoodClassAdapter;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.Good;
import com.example.bzbb.StaticValues.GoodTypeKV;
import com.example.bzbb.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class GoodListActivity extends Activity {
    private ListView goodclasslistLV;
    private GridView goodlistGV;
    private Handler afterGetGoodsHandler;
    private List<Good> displayGoods = new ArrayList<>();
    private Integer selectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodlist);
        //赋值
        goodlistGV = findViewById(R.id.gooditem);
        goodclasslistLV = findViewById(R.id.goodclass);


        final List<Integer> goodclasslist = new ArrayList<>(GoodTypeKV.kv.keySet());
        goodclasslist.add(0,null);
        final GoodClassAdapter goodClassAdapter = new GoodClassAdapter(GoodListActivity.this,R.layout.goodclassitem,goodclasslist);
        goodclasslistLV.setAdapter(goodClassAdapter);
        goodclasslistLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedType = goodClassAdapter.getItem(position);
                getGoodsByType();
            }
        });
        final GoodAdapter goodAdapter = new GoodAdapter(GoodListActivity.this,R.layout.goodlist_item,displayGoods);
        goodlistGV.setAdapter(goodAdapter);
        goodlistGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Good good = (Good) goodlistGV.getAdapter().getItem(position);
                Intent intent = new Intent(GoodListActivity.this, GoodActivity.class);
                intent.putExtra("Good", good);
                startActivity(intent);
            }
        });


        afterGetGoodsHandler = new Handler(){
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()){
                        displayGoods.clear();
                        displayGoods.addAll(JSON.parseArray(ajaxResult.getObject()).toJavaList(Good.class));
                        goodAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(GoodListActivity.this, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        getGoodsByType();

    }

    private void getGoodsByType(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder("/app/getGoodsByType");
                if (selectedType!=null){
                    sb.append("?type=").append(selectedType);
                }
                Response response = HttpUtils.get(sb.toString());
                HttpUtils.processHttpResponse(response,afterGetGoodsHandler,AjaxResult.class);
            }
        }).start();
    }
}
