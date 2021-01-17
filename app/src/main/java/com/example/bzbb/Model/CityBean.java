package com.example.bzbb.Model;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class CityBean implements IPickerViewData {

    private String province;
    private List<String> city_list;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<String> getCity_list() {
        return city_list;
    }

    public void setCity_list(List<String> city_list) {
        this.city_list = city_list;
    }

    @Override
    public String getPickerViewText() {
        return this.province;
    }
}
