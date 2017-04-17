package com.guang.app.model;

/**
 * 打开APP时的提醒信息
 * Created by xiaoguang on 2017/4/17.
 */
public class AppTips {

    /**
     * version : 1
     * enable : true
     * title : 通知
     * message : 更换新官网啦：http://www.wintercoder.com:8080/
     * startTime : 2017-04-17 16:42:54
     * endTime : 2017-04-18 16:25:18
     * openUrl : http://www.wintercoder.com:8080/
     */

    private int version;
    private boolean enable;
    private String title;
    private String message;
    private String startTime;
    private String endTime;
    private String openUrl;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }


    @Override
    public String toString() {
        return "AppTips{" +
                ", version=" + version +
                ", enable=" + enable +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", openUrl='" + openUrl + '\'' +
                '}';
    }
}
