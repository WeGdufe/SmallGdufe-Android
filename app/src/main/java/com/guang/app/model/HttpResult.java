package com.guang.app.model;

public class HttpResult<T> {
    private int code;
    private T data;
    private String msg;
    public boolean isSuccess(){
        return code == 0;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
