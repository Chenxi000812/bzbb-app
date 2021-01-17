package com.example.bzbb.Model;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class StreetBean implements IPickerViewData {

    private String region;
    private List<String> street_list;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public List<String> getStreet_list() {
        return street_list;
    }

    public void setStreet_list(List<String> street_list) {
        this.street_list = street_list;
    }

    @Override
    public String getPickerViewText() {
        return this.region;
    }
}
