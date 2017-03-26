package com.guang.app.model;

/**
 * Created by xiaoguang on 2017/2/20.
 */
public class UserAccount {
    private String sno;
    private String idsPwd;
    private String jwPwd;
    private int defaultPage; //APP启动后的默认首页

    public UserAccount() {
    }

    public UserAccount(String sno, String idsPwd, String jwPwd) {
        this.idsPwd = idsPwd;
        this.sno = sno;
        this.jwPwd = jwPwd;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getIdsPwd() {
        return idsPwd;
    }

    public void setIdsPwd(String idsPwd) {
        this.idsPwd = idsPwd;
    }

    public String getJwPwd() {
        return jwPwd;
    }

    public void setJwPwd(String jwPwd) {
        this.jwPwd = jwPwd;
    }

    public int getDefaultPage() {
        return defaultPage;
    }

    public void setDefaultPage(int defaultPage) {
        this.defaultPage = defaultPage;
    }
}
