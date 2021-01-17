package com.example.bzbb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bzbb.Model.OrderInfo;
import com.example.bzbb.R;
import com.example.bzbb.Utils.HttpUtils;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<OrderInfo>{
    private int resourceId;

    private boolean isOrderCreated = false;

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<OrderInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<OrderInfo> objects,boolean isOrderCreated) {
        super(context, resource, objects);
        resourceId = resource;
        this.isOrderCreated = isOrderCreated;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
        final OrderInfo obj = getItem(position);
        ImageView imageView = convertView.findViewById(R.id.imageView5);
        HttpUtils.loadImg(convertView,obj.getSpimg(),imageView);
        TextView textView = convertView.findViewById(R.id.textView14);
        textView.setText(obj.getGoodTitle());
        TextView textView1 = convertView.findViewById(R.id.textView15);
        textView1.setText("款式:"+obj.getSpName()+"  类型:"+obj.getType());
        TextView textView2 = convertView.findViewById(R.id.textView16);
        textView2.setText("发货时间:3天内发货");
        TextView textView3 = convertView.findViewById(R.id.textView19);
        textView3.setText("￥"+obj.getPrice().setScale(2).toString());
        TextView textView4 = convertView.findViewById(R.id.textView20);
        textView4.setText("x"+obj.getCount());
        Button sub = convertView.findViewById(R.id.button12);
        Button plus = convertView.findViewById(R.id.button13);
        TextView editText = convertView.findViewById(R.id.editText3);
        if (!isOrderCreated){
            editText.setText(String.valueOf(obj.getCount()));
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isPlus;
                    boolean isAllow = true;
                    if (v.getId() == R.id.button12){
                        isPlus = false;
                        if (obj.getCount()<= 1){
                            isAllow=false;
                        }
                    }else {
                        isPlus = true;
                        if (obj.getCount()>30){
                            isAllow = false;
                        }
                    }
                    goodNumChangeListener.goodNumChange(obj.getId(),isPlus,1,position,isAllow);
                }
            };
            sub.setOnClickListener(onClickListener);
            plus.setOnClickListener(onClickListener);
        }else {
            sub.setVisibility(View.GONE);
            plus.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
        }

        return convertView;
    }


    private goodNumChangeListener goodNumChangeListener;

    public void setGoodNumChangeListener(OrderAdapter.goodNumChangeListener goodNumChangeListener) {
        this.goodNumChangeListener = goodNumChangeListener;
    }

    public interface goodNumChangeListener{
        void goodNumChange(Long id,boolean isPlus,int count,int position,boolean isAllow);
    }


}
