package com.example.bzbb.Model;

import java.io.Serializable;
import java.util.Date;

public class Shippingaddress implements Serializable {
    private Long id;

    private Long uid;

    private String name;

    private String mobile;

    private String province;

    private String city;

    private String region;

    private String street;

    private String detailaddress;

    private Integer def;

    private Date createtime;

    public Shippingaddress(Long id, Long uid, String name, String mobile, String province, String city, String region, String street, String detailaddress, Integer def, Date createtime) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.mobile = mobile;
        this.province = province;
        this.city = city;
        this.region = region;
        this.street = street;
        this.detailaddress = detailaddress;
        this.def = def;
        this.createtime = createtime;
    }

    public Shippingaddress() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street == null ? null : street.trim();
    }

    public String getDetailaddress() {
        return detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        this.detailaddress = detailaddress == null ? null : detailaddress.trim();
    }

    public Integer getDef() {
        return def;
    }

    public void setDef(Integer def) {
        this.def = def;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    @Override
    public String toString() {
        return "Shippingaddress{" +
                "id=" + id +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", region='" + region + '\'' +
                ", street='" + street + '\'' +
                ", detailaddress='" + detailaddress + '\'' +
                ", def=" + def +
                ", createtime=" + createtime +
                '}';
    }
}