package com.example.bzbb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bzbb.Adapters.OrderAdapter;
import com.example.bzbb.Handler.CreateOrderHandler;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.Coupon;
import com.example.bzbb.Model.CreateOrderRequestModel;
import com.example.bzbb.Model.Fastmail;
import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.Model.Shippingaddress;
import com.example.bzbb.StaticValues.FastMailKV;
import com.example.bzbb.Tasks.MyPayTask;
import com.example.bzbb.Utils.HttpUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

public class OrderInfoActivity extends Activity {

    private GridView gridView;
    private Button submitOrder;
    private CreateOrderHandler createOrderHandler;
    private List<OrderInfo> orderInfos;
    private OrderAdapter orderAdapter;
    private LinearLayout addressShowLinearLayout;
    private TextView nameShowTV;
    private TextView mobileShowTV;
    private TextView addressShowTV;
    private TextView totalShowTV;
    private Handler setDefaultAddressHandler;
    private Shippingaddress selectedAddress;
    private AlertDialog fastMailSelectDialog;
    private TextView fastMailNameShowTV;
    private TextView fastMailPriceShowTV;
    private LinearLayout fastMailLinearLayout;
    private Fastmail selectedFastMailId;
    private TextView couponNameTV;
    private TextView reductionTV;
    private LinearLayout couponLinearLayout;
    private Coupon selectedCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderinfo);
        //判断有没有登录
        if (!HttpUtils.isLogin) {
            Toast.makeText(this, "您尚未登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Intent intent = getIntent();
        orderInfos = (ArrayList<OrderInfo>) intent.getSerializableExtra("OrderInfo");
        //赋值
        gridView=findViewById(R.id.orderGridView1);
        orderAdapter = new OrderAdapter(this,R.layout.orderitem,orderInfos);
        gridView.setAdapter(orderAdapter);
        submitOrder=findViewById(R.id.button3);
        addressShowLinearLayout = findViewById(R.id.addressShowLinearLayout);
        nameShowTV = addressShowLinearLayout.findViewById(R.id.nameShowTV);
        mobileShowTV = addressShowLinearLayout.findViewById(R.id.mobileShowTV);
        addressShowTV = addressShowLinearLayout.findViewById(R.id.addressShowTV);
        fastMailNameShowTV = findViewById(R.id.textView28);
        fastMailPriceShowTV = findViewById(R.id.textView29);
        fastMailLinearLayout = findViewById(R.id.fastMailLinearLayout);
        totalShowTV = findViewById(R.id.textView31);
        couponNameTV = findViewById(R.id.textView49);
        reductionTV = findViewById(R.id.textView50);
        couponLinearLayout = findViewById(R.id.couponLinearLayout);
        updateTotal();

        //优惠券选择器弹窗

        final Handler couponHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()){
                        final List<Coupon> coupons = JSON.parseArray(ajaxResult.getObject()).toJavaList(Coupon.class);
                        String[] strings = new String[coupons.size()];
                        for (int i = 0 ;i < coupons.size();i++){
                            strings[i] = coupons.get(i).getName();
                        }
                        AlertDialog couponDialog = new AlertDialog.Builder(OrderInfoActivity.this)
                                .setTitle("可用的优惠券")
                                .setItems(strings, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedCoupon = coupons.get(which);
                                        couponNameTV.setText(selectedCoupon.getName());
                                        reductionTV.setText("- ￥"+selectedCoupon.getReduction());
                                        updateTotal();
                                    }
                                }).create();
                        couponDialog.show();
                    }
                }else {
                    Toast.makeText(OrderInfoActivity.this, "出现异常", Toast.LENGTH_SHORT).show();
                }
            }
        };
        couponLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Response response = HttpUtils.sessionGet("/usr/myCoupons?status=0");
                        HttpUtils.processHttpResponse(response,couponHandler,AjaxResult.class);
                    }
                }).start();
            }
        });

        //收货地址选择器弹窗
        final List<Fastmail> fastMailList = FastMailKV.getFastMailList();
        fastMailSelectDialog = new AlertDialog.Builder(this)
                .setTitle("选择快递方式")
                .setItems(FastMailKV.getFastMailNames(fastMailList), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFastMailId= fastMailList.get(which);
                        fastMailNameShowTV.setText(selectedFastMailId.getName());
                        fastMailPriceShowTV.setText("￥"+selectedFastMailId.getPrice());
                        updateTotal();
                    }
                }).create();

        //快递方式选择器点击事件
        fastMailLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fastMailSelectDialog.show();
            }
        });

        //构建DefaultAddressHandler
        setDefaultAddressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()){
                        List<Shippingaddress> shippingaddresses = JSON.parseArray(ajaxResult.getObject(), Shippingaddress.class);
                        selectedAddress = shippingaddresses.get(0);
                        resetSelectedAddressTV();
                    }
                }else {
                    Toast.makeText(OrderInfoActivity.this,"出现异常",Toast.LENGTH_SHORT);
                }
            }
        };

        //初始化默认收货地址显示
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = HttpUtils.sessionGet("/usr/userAddress?def=true");
                HttpUtils.processHttpResponse(response,setDefaultAddressHandler,AjaxResult.class);
            }
        }).start();


        //addressShowLinearLayout绑定点击事件
        addressShowLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OrderInfoActivity.this,AddressListActivity.class),1);
            }
        });



        createOrderHandler = new CreateOrderHandler(this);
        //设置商品数量变化监听器
        orderAdapter.setGoodNumChangeListener(new OrderAdapter.goodNumChangeListener() {
            @Override
            public void goodNumChange(Long id,boolean isPlus,int count,int position,boolean isAllow) {
                OrderInfo orderInfo = orderInfos.get(position);
                if (isAllow){
                    if (isPlus){
                        orderInfo.setCount(orderInfo.getCount()+count);
                    }else {
                        orderInfo.setCount(orderInfo.getCount()-count);
                    }
                    orderInfos.set(position,orderInfo);
                    orderAdapter.notifyDataSetChanged();
                    updateTotal();
                }
            }
        });

        //提交订单按钮监听
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForm()){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CreateOrderRequestModel createOrderRequestModel = new CreateOrderRequestModel();
                            createOrderRequestModel.setOrderInfos(orderInfos);
                            createOrderRequestModel.setAddressId(selectedAddress.getId());
                            createOrderRequestModel.setFastMail(selectedFastMailId.getId());
                            if (selectedCoupon!=null){
                                createOrderRequestModel.setCoupon(selectedCoupon.getUcid());
                            }
                            Response order = MyPayTask.createOrder(createOrderRequestModel);
                            HttpUtils.processHttpResponse(order,createOrderHandler,AjaxResult.class);
                        }
                    }).start();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==1){
            selectedAddress = (Shippingaddress) data.getSerializableExtra("SelectedAddress");
            resetSelectedAddressTV();
        }
    }

    private void resetSelectedAddressTV(){
        nameShowTV.setText(selectedAddress.getName());
        mobileShowTV.setText(selectedAddress.getMobile());
        addressShowTV.setText(selectedAddress.getProvince()+" "+
                selectedAddress.getCity()+" "+
                selectedAddress.getRegion()+" " +
                selectedAddress.getStreet()+" "+
                selectedAddress.getDetailaddress()
        );
    }

    private void updateTotal(){
        BigDecimal total = new BigDecimal(0.00);
        for (OrderInfo orderInfo:orderInfos){
            total = total.add(orderInfo.getPrice().multiply(new BigDecimal(orderInfo.getCount())));
        }
        if (selectedCoupon!=null){
            total = total.subtract(selectedCoupon.getReduction());
        }
        BigDecimal zero = BigDecimal.ZERO;
        if (total.compareTo(zero)==-1){
            total = zero;
        }
        if (selectedFastMailId!=null){
            total = total.add(selectedFastMailId.getPrice());
        }
        totalShowTV.setText(total.setScale(2).toString());
    }

    private boolean checkForm(){
        if (selectedAddress!=null&&selectedFastMailId!=null&&!orderInfos.isEmpty()){
            return true;
        }
        Toast.makeText(this, "请把信息填写完整", Toast.LENGTH_SHORT).show();
        return false;
    }
}
