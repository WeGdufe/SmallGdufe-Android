package com.guang.app.model;

/**
 * 校园卡消费记录
 * Created by xiaoguang on 2017/3/10.
 */
public class CardConsumeItem {

    /**
     * time : 2017/03/10 17:34:34
     * shop : 广州校区第二食堂
     * change : -11.60
     * cash : 141.02
     */

    private String time;
    private String shop;
    private String change;
    private String cash;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }
}
