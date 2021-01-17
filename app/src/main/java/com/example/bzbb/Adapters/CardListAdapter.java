package com.example.bzbb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.R;
import com.example.bzbb.Utils.CartListUtils;
import com.example.bzbb.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class CardListAdapter extends ArrayAdapter<OrderInfo> {
    private int sourceId;
    private List<Long> checkedId = new ArrayList<>();

    public CardListAdapter(@NonNull Context context, int resource, @NonNull List<OrderInfo> objects) {
        super(context, resource, objects);
        sourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(sourceId,null);

        final OrderInfo orderInfo = getItem(position);

        ImageView imageView = convertView.findViewById(R.id.imageView4);
        HttpUtils.loadImg(convertView,orderInfo.getSpimg(),imageView);

        TextView titleTV = convertView.findViewById(R.id.textView6);
        titleTV.setText(orderInfo.getGoodTitle());

        final TextView textView1 = convertView.findViewById(R.id.textView8);
        textView1.setText("颜色规格");

        Spinner spinner = convertView.findViewById(R.id.spinner);
        List<String> strings = new ArrayList<>();
        strings.add(orderInfo.getSpName());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(convertView.getContext(),R.layout.support_simple_spinner_dropdown_item,strings);
        spinner.setAdapter(arrayAdapter);

        TextView totalTV = convertView.findViewById(R.id.textView2);
        totalTV.setText("￥"+orderInfo.getPrice().toString());

        TextView countTV = convertView.findViewById(R.id.textView36);
        countTV.setText(String.valueOf(orderInfo.getCount()));

        Button subBtn = convertView.findViewById(R.id.button10);
        Button plusBtn = convertView.findViewById(R.id.button11);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button10){
                    if (orderInfo.getCount()>1){
                        orderInfo.setCount(orderInfo.getCount()-1);
                        countBtnOnClickCallBack.onClickListener(position,orderInfo);
                        CartListUtils.sub(orderInfo.getId());
                    }
                }else {
                    if (orderInfo.getCount()< 30){
                        orderInfo.setCount(orderInfo.getCount()+1);
                        countBtnOnClickCallBack.onClickListener(position,orderInfo);
                        CartListUtils.add(orderInfo.getId());
                    }
                }
            }
        };
        subBtn.setOnClickListener(onClickListener);
        plusBtn.setOnClickListener(onClickListener);

        CheckBox checkBox = convertView.findViewById(R.id.checkBox3);
        if (checkedId.contains(orderInfo.getId())){
            checkBox.setChecked(true);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cartIsOnCheckedCallBack.onCheckedListener(position,isChecked);
                if (isChecked){
                    checkedId.add(orderInfo.getId());
                }else {
                    checkedId.remove(orderInfo.getId());
                }
            }
        });


        return convertView;
    }


    //数量按钮回调
    private countBtnOnClickCallBack countBtnOnClickCallBack;

    public interface countBtnOnClickCallBack{
        void onClickListener(int position,OrderInfo orderInfo);
    }

    public void setCountBtnOnClickCallBack(countBtnOnClickCallBack callBack){
        countBtnOnClickCallBack = callBack;
    }


    //选择器回调
    private cartIsOnCheckedCallBack cartIsOnCheckedCallBack;

    public interface cartIsOnCheckedCallBack{
        void onCheckedListener(int position,boolean isChecked);
    }

    public void setCartIsOnCheckedCallBack(cartIsOnCheckedCallBack callBack){
        cartIsOnCheckedCallBack = callBack;
    }

}
