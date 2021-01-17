package com.example.bzbb.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.Fastmail;
import com.example.bzbb.Model.Order;
import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.R;
import com.example.bzbb.StaticValues.FastMailKV;
import com.example.bzbb.Tasks.MyPayTask;
import com.example.bzbb.Utils.HttpUtils;

import java.math.BigDecimal;
import java.util.List;

import okhttp3.Response;
import okhttp3.internal.http2.Http2Reader;

public class OrderListAdapter extends ArrayAdapter<Order> {
    private int resourceId;

    public static final int WATINGFORPAY = 0;
    public static final int HAVEPAYED = 1;
    public static final int HAVESENDED = 2;
    public static final int FINISH = 3;

    private confirmOrderProcess confirmOrderProcess;

    public interface confirmOrderProcess{
        void confirmOrderAction(int position,Order order,AjaxResult ajaxResult);
    }

    public void  setConfirmOrderHandler (confirmOrderProcess confirmOrderProcess){
        this.confirmOrderProcess = confirmOrderProcess;
    }

    public OrderListAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
        final Order order = getItem(position);
        TextView textView = convertView.findViewById(R.id.textView18);
        TextView fastmailTV = convertView.findViewById(R.id.textView47);
        TextView reductionTV = convertView.findViewById(R.id.textView46);
        TextView trackingNumTV = convertView.findViewById(R.id.textView53);
        Fastmail fastMail = FastMailKV.getFastMail(order.getFastmail());
        fastmailTV.setText("快递方式: "+ fastMail.getName()+"  运费: ￥"+fastMail.getPrice().setScale(2));
        int status = order.getStatus();
        if (status == WATINGFORPAY){
            textView.setText("等待买家付款");
            Button cancelBtn = convertView.findViewById(R.id.button4);
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setText("取消订单");
            Button continuePayBtn = convertView.findViewById(R.id.button5);
            continuePayBtn.setVisibility(View.VISIBLE);
            continuePayBtn.setText("付款");

            final Handler cancelPay = new Handler(){
                public void handleMessage(Message msg) {
                    if (msg.what==0){
                        AjaxResult ajaxResult = (AjaxResult) msg.obj;
                        if (ajaxResult.getSuccess()){
                            cancelPayHandler.cancelPayAction(position);
                        }
                    }
                }
            };

            final Handler continuePay = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what==0){
                        AjaxResult ajaxResult = (AjaxResult) msg.obj;
                        if (ajaxResult.getSuccess()){
                            continuePayHandler.continuePayAction(ajaxResult.getObject());
                        }
                    }
                }
            };

            final AlertDialog cancelOrderDialog = new AlertDialog.Builder(parent.getContext())
                    .setTitle("确定要删除吗")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Response response = HttpUtils.sessionGet("/usr/cancelOrder?orderId="+order.getId());
                                    HttpUtils.processHttpResponse(response,cancelPay,AjaxResult.class);
                                }
                            }).start();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();

            continuePayBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Response response = HttpUtils.sessionGet("/usr/continueOrder?orderId="+order.getId());
                            HttpUtils.processHttpResponse(response,continuePay,AjaxResult.class);
                        }
                    }).start();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelOrderDialog.show();
                }
            });
        }else if (status == HAVEPAYED){
            textView.setText("等待商家发货");
        }else if (status == HAVESENDED){
            final Handler confirmOrderHandler = new Handler(){
                public void handleMessage(Message msg) {
                    if (msg.what==0){
                        AjaxResult ajaxResult = (AjaxResult) msg.obj;
                        order.setStatus(3);
                        confirmOrderProcess.confirmOrderAction(position,order,ajaxResult);
                    }
                }
            };
            textView.setText("等待买家收货");
            Button queryFastMailBtn = convertView.findViewById(R.id.button4);
            queryFastMailBtn.setVisibility(View.VISIBLE);
            queryFastMailBtn.setText("复制运单号");
            Button confirmBtn = convertView.findViewById(R.id.button5);
            confirmBtn.setVisibility(View.VISIBLE);
            confirmBtn.setText("确认收货");
            confirmBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Response response = HttpUtils.sessionGet("/usr/confirmOrder?orderId=" + order.getId());
                            HttpUtils.processHttpResponse(response,confirmOrderHandler,AjaxResult.class);
                        }
                    }).start();
                }
            });
            trackingNumTV.setVisibility(View.VISIBLE);
            trackingNumTV.setText("运单号: "+order.getTrackingnum());
            queryFastMailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyString.copyStringAction(order.getTrackingnum());
                }
            });
        }else if (status == FINISH){
            textView.setText("订单已完成");
        }
        TextView textView1 = convertView.findViewById(R.id.textView12);
        textView1.setText("总价:￥"+order.getTotal().toString());
        GridView gridView = convertView.findViewById(R.id.orderGridView);
        List<OrderInfo> list = JSON.parseArray(order.getGoods()).toJavaList(OrderInfo.class);
        BigDecimal originalPrice = new BigDecimal(0.00);
        for (OrderInfo orderInfo:list){
            originalPrice = originalPrice.add(orderInfo.getPrice().multiply(new BigDecimal(orderInfo.getCount())));
        }
        BigDecimal reduction = originalPrice.subtract(order.getTotal().subtract(fastMail.getPrice()));
        if (reduction.compareTo(BigDecimal.ZERO) > 0){
            reductionTV.setVisibility(View.VISIBLE);
            reductionTV.setText("优惠：-￥"+reduction.setScale(2).toString());
        }
        OrderAdapter orderAdapter = new OrderAdapter(getContext(),R.layout.orderitem,list,true);
        gridView.setAdapter(orderAdapter);
        View view1 = gridView.getAdapter().getView(0, null, null);
        view1.measure(0,0);
        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        layoutParams.height = view1.getMeasuredHeight()*list.size();
        gridView.setLayoutParams(layoutParams);


        return convertView;
    }



    private continuePayHandler continuePayHandler;

    private cancelPayHandler cancelPayHandler;

    public copyString copyString;

    public interface copyString{
        void copyStringAction(String s);
    }

    public interface continuePayHandler{
        void continuePayAction(String orderInfo);
    }

    public interface cancelPayHandler{
        void cancelPayAction(int position);
    }

    public void setCancelPayHandler(cancelPayHandler cancelPayHandler) {
        this.cancelPayHandler = cancelPayHandler;
    }

    public void setContinuePayHandler(continuePayHandler continuePayHandler) {
        this.continuePayHandler = continuePayHandler;
    }

}
