package com.guang.app.model;

/**
 * 成绩
 * Created by xiaoguang on 2017/2/14.
 */
public class Score {

    /**
     * time : 2014-2015-1
     * name : 现代空间信息技术导论
     * score : 77
     * credit : 1
     * classCode : 160381
     * dailyScore : 85
     * expScore : 0
     * paperScore : 74
     */

    private String time;
    private String name;
    private int score;
    private int credit;
    private String classCode;  //课程编号
    private int dailyScore;   //平时成绩
    private int expScore;     //实验成绩
    private int paperScore;   //卷面期末分

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public int getDailyScore() {
        return dailyScore;
    }

    public void setDailyScore(int dailyScore) {
        this.dailyScore = dailyScore;
    }

    public int getExpScore() {
        return expScore;
    }

    public void setExpScore(int expScore) {
        this.expScore = expScore;
    }

    public int getPaperScore() {
        return paperScore;
    }

    public void setPaperScore(int paperScore) {
        this.paperScore = paperScore;
    }
}
