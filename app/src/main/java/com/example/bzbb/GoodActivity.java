package com.example.bzbb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.bzbb.Adapters.SelectBoxForSpAdapter;
import com.example.bzbb.Model.Good;
import com.example.bzbb.Model.Goodspecifications;
import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.Utils.CartListUtils;
import com.example.bzbb.Utils.HttpUtils;
import com.example.bzbb.custom.BannerImageLoad;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class GoodActivity extends Activity {

    private Good good;
    private List<Goodspecifications> goodspecificationsList;
    private Banner banner;
    private TextView tvPrice;
    private TextView tvTitle;
    private Button payBtn;
    private Button addCartListBtn;
    private Spinner selectBoxSp;

    private Handler getSpHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                SelectBoxForSpAdapter selectBoxForSpAdapter = new SelectBoxForSpAdapter(GoodActivity.this, goodspecificationsList);
                selectBoxSp.setAdapter(selectBoxForSpAdapter);
            } else {
                Toast.makeText(GoodActivity.this, "出错", Toast.LENGTH_LONG);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        //赋值
        banner = findViewById(R.id.goodbanner);
        tvPrice = findViewById(R.id.textView21);
        tvTitle = findViewById(R.id.textView22);
        payBtn = findViewById(R.id.button8);
        selectBoxSp = findViewById(R.id.spinner2);
        addCartListBtn = findViewById(R.id.button7);

        //接受来自Main的数据
        Intent intent = getIntent();
        good = (Good) intent.getSerializableExtra("Good");
        if (good == null) {
            finish();
        }
        //获取该商品的款式
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = -1;
                Response response = HttpUtils.get("/app/querygoodsp?gid=" + good.getId());
                try {
                    String res = response.body().string();
                    JSONObject jsonObject = JSON.parseObject(res);
                    if (jsonObject.getBoolean("success")) {
                        goodspecificationsList = jsonObject.getJSONArray("object").toJavaList(Goodspecifications.class);
                        message.what = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    getSpHandler.sendMessage(message);
                }
            }
        }).start();
        //绑定款式item监听
        selectBoxSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Goodspecifications goodspecifications = goodspecificationsList.get(position);
                tvPrice.setText("￥" + goodspecifications.getPrice().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //绑定标题
        tvTitle.setText(good.getTitle());
        //图片展示
        List<String> imgs = JSON.parseArray(good.getImgs(),String.class);
        List<String> title = new ArrayList<>();
        for (String img : imgs) {
            title.add("132");
        }
        banner.setImages(imgs);
        banner.setImageLoader(new BannerImageLoad());
        banner.isAutoPlay(false);
        banner.setBannerTitles(title);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setBannerAnimation(Transformer.CubeOut);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.start();

        //加入购物车功能
        addCartListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goodspecifications goodspecifications = (Goodspecifications) selectBoxSp.getSelectedItem();
                CartListUtils.insert(OrderInfo.createOrderInfo(good, goodspecifications));
                Toast.makeText(GoodActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
            }
        });

        //跳转到创建订单
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Goodspecifications goodspecifications = (Goodspecifications) selectBoxSp.getSelectedItem();
                Intent intent = new Intent(GoodActivity.this, OrderInfoActivity.class);
                ArrayList<OrderInfo> orderInfos = new ArrayList<>();
                orderInfos.add(OrderInfo.createOrderInfo(good, goodspecifications));
                intent.putExtra("OrderInfo", orderInfos);
                startActivity(intent);


            }
        });

    }

}
