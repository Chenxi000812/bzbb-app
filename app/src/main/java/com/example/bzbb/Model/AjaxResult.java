package com.example.bzbb.Model;

public class AjaxResult {
    private Boolean success;//默认操作成功
    private String msg ;//返回前端操作的文字结果
    private String object;//返回后台的对象

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}

