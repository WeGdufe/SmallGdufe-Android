package com.guang.app.model;

/**
 * 部分宿舍楼电控信息
 * Created by xiaoguang on 2017/9/9.
 */

public class Electric {

    /**
     * electric : 5.0 剩余电量
     * money : 3.24 剩余金额
     * time : 2017-09-02 10:00:00
     */

    private String electric;
    private String money;
    private String time;

    public String getElectric() {
        return electric;
    }

    public void setElectric(String electric) {
        this.electric = electric;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
