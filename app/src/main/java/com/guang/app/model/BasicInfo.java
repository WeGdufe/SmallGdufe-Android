package com.guang.app.model;

import org.litepal.crud.DataSupport;

/**
 * 个人基本信息
 * Created by xiaoguang on 2017/2/23.
 */
public class BasicInfo extends DataSupport {

    /**
     * department : 信息学院
     * major : 计算机科学与技术
     * classroom : 2013计算机科学与技术2班
     * name : XXX
     * sex : XX
     * namePy : XXX XX
     * birthday : 19999990
     * party : 共青团员
     * nation : 汉族
     * education : 普通本科
     */

    private String department;
    private String major;
    private String classroom;   //跟getClass重名了
    private String name;
    private String sex;
    private String namePy;
    private String birthday;
    private String party;
    private String nation;
    private String education;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNamePy() {
        return namePy;
    }

    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }


}
