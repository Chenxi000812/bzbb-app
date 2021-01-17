package com.example.bzbb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.bzbb.Model.Shippingaddress;
import com.example.bzbb.R;

import org.w3c.dom.Text;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<Shippingaddress> {

    private int resourceId;

    public AddressAdapter(Context context, int resource, List<Shippingaddress> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resourceId,null);
        Shippingaddress obj = getItem(position);
        TextView nameTV = convertView.findViewById(R.id.textView33);
        nameTV.setText(obj.getName());
        TextView mobileTV = convertView.findViewById(R.id.textView34);
        mobileTV.setText(obj.getMobile());
        TextView addressTV = convertView.findViewById(R.id.textView26);
        addressTV.setText(obj.getProvince()+" "+obj.getCity()+" "+obj.getRegion()+" "+obj.getStreet()+" "+obj.getDetailaddress());
        TextView editBtn = convertView.findViewById(R.id.textView17);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBtnListenerImp.itemEditBtnOnClick(position);
            }
        });
        return convertView;
    }

    private editBtnListener editBtnListenerImp;

    public void setItemEditBtnOnClickListener(editBtnListener editBtnListenerImp){
        this.editBtnListenerImp = editBtnListenerImp;
    }

    public interface editBtnListener{
        void itemEditBtnOnClick(int position);
    }

}
