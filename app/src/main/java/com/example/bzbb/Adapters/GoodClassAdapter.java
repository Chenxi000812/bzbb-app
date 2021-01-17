package com.example.bzbb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bzbb.R;
import com.example.bzbb.StaticValues.GoodTypeKV;

import java.util.List;

public class GoodClassAdapter extends ArrayAdapter<Integer> {

    private int sourceId;

    public GoodClassAdapter(@NonNull Context context, int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        sourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(sourceId,null);
        Integer id = getItem(position);
        TextView textView = view.findViewById(R.id.textView7);
        if (id == null){
            textView.setText("全部");
        }else {
            textView.setText(GoodTypeKV.getType(id));
        }

        return view;
    }
}
