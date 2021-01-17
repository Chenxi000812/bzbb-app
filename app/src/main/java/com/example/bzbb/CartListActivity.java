package com.example.bzbb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bzbb.Adapters.CardListAdapter;
import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.Utils.CartListUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartListActivity extends Activity {

    private GridView listView;

    private Button createOrderBtn;
    private TextView totalTV;
    private ImageView delBtn;
    private List<OrderInfo> orderInfos;
    private ArrayList<OrderInfo> selectedOrders = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        //赋值
        orderInfos = CartListUtils.get();
        listView = findViewById(R.id.cartlist);
        totalTV = findViewById(R.id.textView4);
        delBtn = findViewById(R.id.imageView7);
        createOrderBtn = findViewById(R.id.button);

        final CardListAdapter cardListAdapter = new CardListAdapter(CartListActivity.this,R.layout.cartlist_item, orderInfos);
        cardListAdapter.setCountBtnOnClickCallBack(new CardListAdapter.countBtnOnClickCallBack() {
            @Override
            public void onClickListener(int position,OrderInfo orderInfo) {
                int i;
                if ( (i = selectedOrders.indexOf(orderInfos.get(position)))!=-1){
                    selectedOrders.set(i,orderInfo);
                }
                orderInfos.set(position,orderInfo);
                updateTotal();
                cardListAdapter.notifyDataSetChanged();
            }
        });
        cardListAdapter.setCartIsOnCheckedCallBack(new CardListAdapter.cartIsOnCheckedCallBack() {
            @Override
            public void onCheckedListener(int position, boolean isChecked) {
                OrderInfo item = cardListAdapter.getItem(position);
                if (isChecked){
                    selectedOrders.add(item);
                }else {
                    selectedOrders.remove(item);
                }
                updateTotal();
            }
        });
        listView.setAdapter(cardListAdapter);


        createOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartListActivity.this,OrderInfoActivity.class);
                intent.putExtra("OrderInfo",selectedOrders);
                startActivity(intent);
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (OrderInfo orderInfo:selectedOrders){
                    orderInfos.remove(orderInfo);
                    CartListUtils.remove(orderInfo.getId());
                }
                cardListAdapter.notifyDataSetChanged();
                updateTotal();
            }
        });
    }

    private void updateTotal(){
        BigDecimal bigDecimal = new BigDecimal(0.00);
        for (OrderInfo orderInfo : selectedOrders){
            bigDecimal = bigDecimal.add(orderInfo.getPrice().multiply(new BigDecimal(orderInfo.getCount())));
        }
        totalTV.setText("￥"+bigDecimal);
    }


}
