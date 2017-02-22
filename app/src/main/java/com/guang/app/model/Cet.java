package com.guang.app.model;

/**
 * Created by xiaoguang on 2017/2/22.
 */
public class Cet {

    /**
     * name : XXX
     * school : 广东财经大学
     * level : 英语六级
     * cetId : 440100162200601 [440100]6位学校号[16]年第[2]次考试[2]六级【四级为1】 [006]考场号[01]座位号
     * score : 0 零分为<220或者违规
     * listenScore : 0
     * readScore : 0
     * writeScore : 0
     */

    private String name;
    private String school;
    private String level;
    private String cetId;
    private String score;
    private String listenScore;
    private String readScore;
    private String writeScore;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCetId() {
        return cetId;
    }

    public void setCetId(String cetId) {
        this.cetId = cetId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getListenScore() {
        return listenScore;
    }

    public void setListenScore(String listenScore) {
        this.listenScore = listenScore;
    }

    public String getReadScore() {
        return readScore;
    }

    public void setReadScore(String readScore) {
        this.readScore = readScore;
    }

    public String getWriteScore() {
        return writeScore;
    }

    public void setWriteScore(String writeScore) {
        this.writeScore = writeScore;
    }
}
