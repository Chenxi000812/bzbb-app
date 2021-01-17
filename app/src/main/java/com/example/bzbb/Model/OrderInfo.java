package com.example.bzbb.Model;

import com.example.bzbb.StaticValues.GoodBrandKV;
import com.example.bzbb.StaticValues.GoodTypeKV;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderInfo implements Serializable {

    private Long id;
    private int count;
    private BigDecimal price;
    private Long gid;
    private String goodTitle;
    private String spName;
    private String type;
    private String spimg;
    private String brand;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public OrderInfo() {
        count = 1;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }



    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGoodTitle() {
        return goodTitle;
    }

    public void setGoodTitle(String goodTitle) {
        this.goodTitle = goodTitle;
    }

    public String getSpName() {
        return spName;
    }

    public void setSpName(String spName) {
        this.spName = spName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpimg() {
        return spimg;
    }

    public void setSpimg(String spimg) {
        this.spimg = spimg;
    }

    public static OrderInfo createOrderInfo(Good good,Goodspecifications goodspecifications){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setGid(good.getId());
        orderInfo.setGoodTitle(good.getTitle());
        orderInfo.setId(goodspecifications.getId());
        orderInfo.setPrice(goodspecifications.getPrice());
        orderInfo.setSpimg(goodspecifications.getImg());
        orderInfo.setType(GoodTypeKV.getType(good.getType()));
        orderInfo.setBrand(GoodBrandKV.getType(good.getBrand()));
        orderInfo.setSpName(goodspecifications.getName());
        return orderInfo;
    }
}
