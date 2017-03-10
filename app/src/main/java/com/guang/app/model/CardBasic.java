package com.guang.app.model;

/**
 * 饭卡基本信息
 * Created by xiaoguang on 2017/3/10.
 */
public class CardBasic {

    /**
     * cardNum : 132461
     * cash : 152.62
     * cardState : 正常
     * checkState : 正常
     * lossState : 正常
     * freezeState : 正常
     */

    private String cardNum;
    private String cash;
    private String cardState;
    private String checkState;
    private String lossState;
    private String freezeState;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCardState() {
        return cardState;
    }

    public void setCardState(String cardState) {
        this.cardState = cardState;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getLossState() {
        return lossState;
    }

    public void setLossState(String lossState) {
        this.lossState = lossState;
    }

    public String getFreezeState() {
        return freezeState;
    }

    public void setFreezeState(String freezeState) {
        this.freezeState = freezeState;
    }
}
