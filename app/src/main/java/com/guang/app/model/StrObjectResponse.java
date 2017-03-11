package com.guang.app.model;

/**
 * 因retrofit2解析返回的纯String值和Gson形式混用不便，且接口不统一，故单纯返回一个值的，也封装为对象，后台也改为返回对象
 * 即 {"code":0,"msg":"","data":"我是内容"} 改为 {"code":0,"msg":"","data":{"data":"我是内容"}}
 * Created by xiaoguang on 2017/3/11.
 */
public class StrObjectResponse {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
