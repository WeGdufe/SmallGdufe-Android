package com.guang.app.model;

/**
 * Created by xiaoguang on 2017/2/18.
 */
public class Book {

    /**
     * barId : S1325072
     * name : Servlet和JSP学习指南:a tutorial
     * author : (加) Budi Kurniawan著
     * borrowedTime : 2016-10-12
     * returnTime : 2016-12-10
     * location : 广州校区自然科学图书区(N-Z类)
     */

    private String barId;
    private String name;
    private String author;
    private String borrowedTime;
    private String returnTime;
    private String location;

    public String getBarId() {
        return barId;
    }

    public void setBarId(String barId) {
        this.barId = barId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBorrowedTime() {
        return borrowedTime;
    }

    public void setBorrowedTime(String borrowedTime) {
        this.borrowedTime = borrowedTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
