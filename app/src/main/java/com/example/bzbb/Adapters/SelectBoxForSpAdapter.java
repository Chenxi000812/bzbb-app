package com.example.bzbb.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bzbb.Model.Goodspecifications;
import com.example.bzbb.R;
import com.example.bzbb.Utils.HttpUtils;

import java.util.List;

public class SelectBoxForSpAdapter extends BaseAdapter {
    private Context mcontext;
    private List<Goodspecifications> objects;

    public SelectBoxForSpAdapter(Context context,List<Goodspecifications> objects) {
        super();
        mcontext = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Goodspecifications goodspecifications = (Goodspecifications) getItem(position);
        if (convertView == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(mcontext);     //布局加载权限
            convertView = mLayoutInflater.inflate(R.layout.spselectboxitem, parent,false);
        }

        TextView textView = convertView.findViewById(R.id.textView);
        textView.setText(goodspecifications.getName());
        ImageView imageView = convertView.findViewById(R.id.imageView6);
        HttpUtils.loadImg(convertView,goodspecifications.getImg(),imageView);
        return convertView;

    }
}
