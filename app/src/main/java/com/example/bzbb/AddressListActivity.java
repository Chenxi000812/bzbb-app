package com.example.bzbb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.bzbb.Adapters.AddressAdapter;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.Shippingaddress;
import com.example.bzbb.Utils.HttpUtils;

import java.util.List;

import okhttp3.Response;

public class AddressListActivity extends Activity {

    private GridView addressListGV;
    private Handler addressListHandler;
    private TextView createAddressBtn;
    private static final int NewAddressActivity_UPDATE = 0;
    private static final int NewAddressActivity_INSERT = 1;
    private AddressAdapter addressAdapter;
    private List<Shippingaddress> shippingaddresses;
    private int editing;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresslist);
        addressListGV = findViewById(R.id.addressListGV);
        createAddressBtn = findViewById(R.id.textView23);
        createAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressListActivity.this,NewAddressActivity.class);
                intent.putExtra("isInsert",true);
                AddressListActivity.this.startActivityForResult(intent,NewAddressActivity_INSERT);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = HttpUtils.sessionGet("/usr/userAddress?def=false");
                HttpUtils.processHttpResponse(response,addressListHandler, AjaxResult.class);
            }
        }).start();

        addressListHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0){
                    AjaxResult ajaxResult = (AjaxResult) msg.obj;
                    if (ajaxResult.getSuccess()){
                        shippingaddresses = JSON.parseArray(ajaxResult.getObject(), Shippingaddress.class);
                        addressAdapter = new AddressAdapter(AddressListActivity.this, R.layout.addressitem, shippingaddresses);
                        addressAdapter.setItemEditBtnOnClickListener(new AddressAdapter.editBtnListener() {
                            @Override
                            public void itemEditBtnOnClick(int position) {
                                editing = position;
                                Shippingaddress item = addressAdapter.getItem(position);
                                Intent intent = new Intent(AddressListActivity.this,NewAddressActivity.class);
                                intent.putExtra("isInsert",false);
                                intent.putExtra("Shippingaddress",item);
                                AddressListActivity.this.startActivityForResult(intent,NewAddressActivity_UPDATE);
                            }
                        });
                        addressListGV.setAdapter(addressAdapter);

                        //选择地址绑定监听事件
                        addressListGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Shippingaddress shippingaddress = (Shippingaddress) addressListGV.getItemAtPosition(position);
                                Intent intent = new Intent();
                                intent.putExtra("SelectedAddress",shippingaddress);
                                setResult(1,intent);
                                AddressListActivity.this.finish();
                            }
                        });
                    }else {
                        Toast.makeText(AddressListActivity.this, ajaxResult.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddressListActivity.this, "发生异常",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1){
            Shippingaddress shippingaddress = (Shippingaddress) data.getSerializableExtra("Shippingaddress");
            if (requestCode==NewAddressActivity_INSERT){
                shippingaddresses.add(shippingaddress);

            }else {
                shippingaddresses.set(editing,shippingaddress);
            }
            addressAdapter.notifyDataSetChanged();
        }else if (resultCode == 2){
            //为删除
            shippingaddresses.remove(editing);
            addressAdapter.notifyDataSetChanged();
        }

    }
}
