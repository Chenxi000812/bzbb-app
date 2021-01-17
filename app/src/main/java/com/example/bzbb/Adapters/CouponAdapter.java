package com.example.bzbb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bzbb.Model.Coupon;
import com.example.bzbb.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class CouponAdapter extends ArrayAdapter<Coupon> {

    private int resourceID;

    public CouponAdapter(@NonNull Context context, int resource, @NonNull List<Coupon> objects) {
        super(context, resource, objects);
        resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resourceID,null);
        Coupon coupon = getItem(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        TextView tv_coupon_name = convertView.findViewById(R.id.tv_coupon_name);
        tv_coupon_name.setText(coupon.getName());
        TextView tv_coupon_faceValue = convertView.findViewById(R.id.tv_coupon_faceValue);
        tv_coupon_faceValue.setText(coupon.getReduction().toString());
        TextView tv_coupon_end_time = convertView.findViewById(R.id.tv_coupon_end_time);
        if (coupon.getExpire()!=null){
            tv_coupon_end_time.setText(sdf.format(coupon.getExpire()));
        }
        return convertView;
    }
}
