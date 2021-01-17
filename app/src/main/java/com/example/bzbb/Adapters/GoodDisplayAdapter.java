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

import org.w3c.dom.Text;

import java.util.List;


public class GoodDisplayAdapter extends ArrayAdapter<Good> {
    private int sourceId;

    public GoodDisplayAdapter(@NonNull Context context, int resource, @NonNull List<Good> objects) {
        super(context, resource, objects);
        sourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Good good =getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(sourceId,null);
        TextView textView = convertView.findViewById(R.id.textView9);
        ImageView imageView = convertView.findViewById(R.id.imageView3);
        textView.setText(good.getTitle());
        HttpUtils.loadImg(convertView,JSON.parseArray(good.getImgs(),String.class).get(0),imageView);
        TextView priceTV = convertView.findViewById(R.id.textView11);
        priceTV.setText("￥"+good.getMinprice()+"~"+good.getMaxprice());
        return convertView;
    }
}
