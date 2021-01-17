package com.example.bzbb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.bzbb.Model.AjaxResult;
import com.example.bzbb.Model.CityBean;
import com.example.bzbb.Model.Shippingaddress;
import com.example.bzbb.Model.StreetBean;
import com.example.bzbb.Utils.AssetUtils;
import com.example.bzbb.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Response;

public class NewAddressActivity extends Activity {

    private ArrayList<CityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<StreetBean> options1Items1 = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items1 = new ArrayList<>();
    private JSONObject pcasJson;
    private String selectedProvince;
    private String selectedCity;
    private String selectedRegion;
    private String selectedStreet;
    private TextView saveBtn;
    private boolean isInsert;

    private EditText receiverNameET;
    private EditText receiverMobileET;
    private EditText detailAddressET;
    private Button delBtn;
    private Switch isdefaultSW;

    private TextView pcTV;
    private TextView asTV;

    private LinearLayout pcLL;
    private LinearLayout asLL;

    private Shippingaddress shippingaddress = new Shippingaddress();

    @SuppressLint("HandlerLeak")
    private Handler afterAddressRequest = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                AjaxResult ajaxResult = (AjaxResult) msg.obj;
                if (ajaxResult.getSuccess()){
                    Shippingaddress shippingaddress = JSON.parseObject(ajaxResult.getObject(), Shippingaddress.class);
                    if (msg.arg1==1){
                        setResult(2);
                    }else {
                        Intent intent = new Intent();
                        intent.putExtra("Shippingaddress",shippingaddress);
                        setResult(1,intent);
                    }
                    NewAddressActivity.this.finish();
                }else {
                    Toast.makeText(NewAddressActivity.this, ajaxResult.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(NewAddressActivity.this, "发生异常", Toast.LENGTH_SHORT).show();
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaddress);
        //赋值
        receiverNameET = findViewById(R.id.editText1);
        receiverMobileET = findViewById(R.id.editText2);
        detailAddressET = findViewById(R.id.editText);
        saveBtn = findViewById(R.id.textView35);
        delBtn = findViewById(R.id.button6);
        pcTV = findViewById(R.id.textView38);
        asTV = findViewById(R.id.textView41);
        pcLL = findViewById(R.id.pcLinerLayout);
        asLL = findViewById(R.id.asLinearLayout);
        isdefaultSW = findViewById(R.id.switch2);


        pcLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPCPickerView();
            }
        });

        asLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedProvince!=null&&selectedCity!=null){
                    showASPickerView();
                }else {
                    Toast.makeText(NewAddressActivity.this, "请先选择省市", Toast.LENGTH_SHORT).show();
                }
            }
        });
        initPCData();

        isInsert = getIntent().getBooleanExtra("isInsert",false);
        if (isInsert){
            //为新建操作
            delBtn.setVisibility(View.GONE);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setShippingaddress();
                            Response response = HttpUtils.sessionPostForJSON("/usr/createAddress",JSON.toJSONString(shippingaddress));
                            HttpUtils.processHttpResponse(response,afterAddressRequest, AjaxResult.class);
                        }
                    }).start();
                }
            });
        }else {
            //为编辑操作
            shippingaddress = (Shippingaddress) getIntent().getSerializableExtra("Shippingaddress");
            selectedProvince = shippingaddress.getProvince();
            selectedCity = shippingaddress.getCity();
            selectedRegion = shippingaddress.getRegion();
            selectedStreet = shippingaddress.getStreet();
            detailAddressET.setText(shippingaddress.getDetailaddress());
            receiverNameET.setText(shippingaddress.getName());
            receiverMobileET.setText(shippingaddress.getMobile());
            initASData();
            if (shippingaddress.getDef() != null){
                isdefaultSW.setChecked(true);
            }else {
                isdefaultSW.setChecked(false);
            }
            pcTV.setText(selectedProvince+" "+selectedCity+" ");
            asTV.setText(selectedRegion+" "+selectedStreet+" ");

            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Response response = HttpUtils.sessionGet("/usr/delAddress?id="+shippingaddress.getId());
                            HttpUtils.processHttpResponse(response,afterAddressRequest,AjaxResult.class,1);
                        }
                    }).start();
                }
            });


            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setShippingaddress();
                            Response response = HttpUtils.sessionPostForJSON("/usr/updateAddress",JSON.toJSONString(shippingaddress));
                            HttpUtils.processHttpResponse(response,afterAddressRequest, AjaxResult.class);

                        }
                    }).start();
                }
            });
        }


    }


    private void setShippingaddress(){
        shippingaddress.setProvince(selectedProvince);
        shippingaddress.setCity(selectedCity);
        shippingaddress.setRegion(selectedRegion);
        shippingaddress.setStreet(selectedStreet);
        shippingaddress.setDetailaddress(detailAddressET.getText().toString());
        shippingaddress.setName(receiverNameET.getText().toString());
        shippingaddress.setMobile(receiverMobileET.getText().toString());
        if (isdefaultSW.isChecked()){
            shippingaddress.setDef(1);
        }else {
            shippingaddress.setDef(0);
        }
    }

    private void initPCData(){
        String cityData = AssetUtils.readStringFile(this,"pcas.json");
        options1Items = parsePCData(cityData);
        for (CityBean cityBean:options1Items){
            ArrayList<String> cityList = new ArrayList<>();
            for (String city:cityBean.getCity_list()){
                cityList.add(city);
            }
            options2Items.add(cityList);
        }
    }

    private void initASData(){
        options2Items1.clear();
        options1Items1 = parseASData();
        for (StreetBean streetBean:options1Items1){
            ArrayList<String> streetList = new ArrayList<>(streetBean.getStreet_list());
            options2Items1.add(streetList);
        }
    }

    private ArrayList<StreetBean> parseASData(){
        ArrayList<StreetBean> streetBeans = new ArrayList<>();
        JSONObject selectedObject = pcasJson.getJSONObject(selectedProvince).getJSONObject(selectedCity);
        for (String key:selectedObject.keySet()){
            StreetBean streetBean = new StreetBean();
            streetBean.setRegion(key);
            List<String> street_list = new ArrayList<>(selectedObject.getJSONArray(key).toJavaList(String.class));
            streetBean.setStreet_list(street_list);
            streetBeans.add(streetBean);
        }
        return streetBeans;
    }

    private ArrayList<CityBean> parsePCData(String data){
        ArrayList<CityBean> cityBeans = new ArrayList<>();
        pcasJson = JSON.parseObject(data);
        for (String key : pcasJson.keySet()){
            CityBean cityBean = new CityBean();
            cityBean.setProvince(key);
            List<String> city_list = new ArrayList<>();
            city_list.addAll(pcasJson.getJSONObject(key).keySet());
            cityBean.setCity_list(city_list);
            cityBeans.add(cityBean);
        }
        return cityBeans;
    }
    private void showPCPickerView(){
        OptionsPickerView pickerView1 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                selectedProvince = options1Items.get(options1).getProvince();
                selectedCity = options2Items.get(options1).get(options2);
                pcTV.setText(selectedProvince+" "+selectedCity+" ");
                selectedRegion = null;
                selectedStreet = null;
                asTV.setText("请选择");
                initASData();
            }
        })
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setOutSideCancelable(false)
                .build();
        pickerView1.setPicker(options1Items,options2Items);
        pickerView1.show();
    }

    private void showASPickerView() {
        OptionsPickerView pickerView2 = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                selectedRegion  = options1Items1.get(options1).getRegion();
                selectedStreet = options2Items1.get(options1).get(options2);
                asTV.setText(selectedRegion+" "+selectedStreet+" ");
            }
        })
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setOutSideCancelable(false)
                .build();

        pickerView2.setPicker(options1Items1,options2Items1);
        pickerView2.show();

    }
}
