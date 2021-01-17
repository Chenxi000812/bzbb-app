package com.example.bzbb.Model;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private String id;

    private String goods;

    private Integer status;

    private BigDecimal total;

    private Long uid;

    private Long coupon;

    private Long address;

    private Integer fastmail;

    private String trackingnum;

    private Date createtime;

    public Order(String id, String goods, Integer status, BigDecimal total, Long uid, Long coupon, Long address, Integer fastmail, String trackingnum, Date createtime) {
        this.id = id;
        this.goods = goods;
        this.status = status;
        this.total = total;
        this.uid = uid;
        this.coupon = coupon;
        this.address = address;
        this.fastmail = fastmail;
        this.trackingnum = trackingnum;
        this.createtime = createtime;
    }

    public Order() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods == null ? null : goods.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getCoupon() {
        return coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Long getAddress() {
        return address;
    }

    public void setAddress(Long address) {
        this.address = address;
    }

    public Integer getFastmail() {
        return fastmail;
    }

    public void setFastmail(Integer fastmail) {
        this.fastmail = fastmail;
    }

    public String getTrackingnum() {
        return trackingnum;
    }

    public void setTrackingnum(String trackingnum) {
        this.trackingnum = trackingnum == null ? null : trackingnum.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}