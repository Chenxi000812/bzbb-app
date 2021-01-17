package com.example.bzbb.Model;

import java.util.List;

public class CreateOrderRequestModel {

    private List<OrderInfo> orderInfos;

    private Long addressId;

    private Integer fastMail;

    private Long coupon;

    public Long getCoupon() {
        return coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Integer getFastMail() {
        return fastMail;
    }

    public void setFastMail(Integer fastMail) {
        this.fastMail = fastMail;
    }

    public List<OrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public void setOrderInfos(List<OrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }


}
