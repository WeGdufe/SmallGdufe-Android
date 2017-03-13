package com.guang.app.model;

/**
 * 搜书后点击的库存详情
 * Created by xiaoguang on 2017/3/13.
 */
public class SearchBookStoreItem {
    /**
     * barId : S1836879
     * serial : TP312JA/1077
     * volume  : -
     * location : 广州校区自然科学图书区(N-Z类)
     * state : 可借
     */

    private String barId;   //条码号
    private String serial;  // 索书号
    private String volume;  //年卷期
    private String location;
    private String state;

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
