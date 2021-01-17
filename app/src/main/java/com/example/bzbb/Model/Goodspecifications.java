package com.example.bzbb.Model;

import java.math.BigDecimal;
import java.util.Date;

public class Goodspecifications {
    private Long id;

    private Long gid;

    private String name;

    private BigDecimal price;

    private Long stock;

    private Date createtime;

    private Date updatetime;

    private String img;

    public Goodspecifications(Long id, Long gid, String name, BigDecimal price, Long stock, Date createtime, Date updatetime, String img) {
        this.id = id;
        this.gid = gid;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createtime = createtime;
        this.updatetime = updatetime;
        this.img = img;
    }

    public Goodspecifications() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img == null ? null : img.trim();
    }
}