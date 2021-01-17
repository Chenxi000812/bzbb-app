package com.example.bzbb.Model;

import java.math.BigDecimal;
import java.util.Date;

public class Coupon {
    private Long ucid;

    private Integer id;

    private String name;

    private BigDecimal requirement;

    private BigDecimal reduction;

    private Date createtime;

    private Date expire;

    private Integer status;

    public Long getUcid() {
        return ucid;
    }

    public void setUcid(Long ucid) {
        this.ucid = ucid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Coupon(Integer id, String name, BigDecimal requirement, BigDecimal reduction, Date createtime, Date expire) {
        this.id = id;
        this.name = name;
        this.requirement = requirement;
        this.reduction = reduction;
        this.createtime = createtime;
        this.expire = expire;
    }

    public Coupon() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getRequirement() {
        return requirement;
    }

    public void setRequirement(BigDecimal requirement) {
        this.requirement = requirement;
    }

    public BigDecimal getReduction() {
        return reduction;
    }

    public void setReduction(BigDecimal reduction) {
        this.reduction = reduction;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }
}