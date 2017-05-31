package com.guang.app.model;


import org.litepal.crud.DataSupport;

/**
 * 课程表
 * Created by xiaoguang on 2017/2/21.
 */
public class Schedule extends DataSupport {

    /**
     * name : 计算机系统结构
     * teacher : 陈振宇副教授
     * period : 1-16(周)
     * location : 拓新楼(SS1)334
     * dayInWeek : 1
     * startSec : 1
     * endSec : 2
     */
//     private Long sId; //自增id，只为存本地数据库用，与业务无关
    private String name;
    private String teacher;
    private String period;       //周情况
    private String location;
    private int dayInWeek;      //星期几
    private int startSec;
    private int endSec;         //终止小节
    public Schedule(){}
    public Schedule( String name, String teacher, String period, String location, int dayInWeek, int startSec, int endSec) {
        this.name = name;
        this.teacher = teacher;
        this.period = period;
        this.location = location;
        this.dayInWeek = dayInWeek;
        this.startSec = startSec;
        this.endSec = endSec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDayInWeek() {
        return dayInWeek;
    }

    public void setDayInWeek(int dayInWeek) {
        this.dayInWeek = dayInWeek;
    }

    public int getStartSec() {
        return startSec;
    }

    public void setStartSec(int startSec) {
        this.startSec = startSec;
    }

    public int getEndSec() {
        return endSec;
    }

    public void setEndSec(int endSec) {
        this.endSec = endSec;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", period='" + period + '\'' +
                ", location='" + location + '\'' +
                ", dayInWeek=" + dayInWeek +
                ", startSec=" + startSec +
                ", endSec=" + endSec +
                '}';
    }
}
