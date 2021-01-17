package com.example.bzbb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.bzbb.Model.Good;
import com.example.bzbb.R;
import com.example.bzbb.Utils.HttpUtils;


import java.util.List;

public class GoodAdapter extends ArrayAdapter<Good> {
    private int sourceId;
    public GoodAdapter( Context context, int resource,  List<Good> objects) {
        super(context, resource, objects);
        sourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(sourceId,null);
        Good good = getItem(position);
        TextView titleTV = convertView.findViewById(R.id.textView10);
        TextView priceTV = convertView.findViewById(R.id.textView13);
        ImageView img = convertView.findViewById(R.id.imageView2);
        titleTV.setText(good.getTitle());
        priceTV.setText("￥"+good.getMinprice()+"~"+good.getMaxprice());
        HttpUtils.loadImg(convertView, JSON.parseArray(good.getImgs(),String.class).get(0),img);
        return convertView;
    }
}
