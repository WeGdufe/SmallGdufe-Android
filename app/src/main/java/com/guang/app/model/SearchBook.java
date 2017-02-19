package com.guang.app.model;

/**
 * Created by xiaoguang on 2017/2/19.
 */
public class SearchBook {

    /**
     * name : 1.解忧杂货店
     * serial : I313.45/1093
     * numAll : 4
     * numCan : 0
     * author : (日) 东野圭吾著
     * publisher : 南海出版公司 2014
     */

    private String name;
    private String serial;
    private String numAll;  // 馆藏总书量
    private String numCan;  //可借书量
    private String author;
    private String publisher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getNumAll() {
        return numAll;
    }

    public void setNumAll(String numAll) {
        this.numAll = numAll;
    }

    public String getNumCan() {
        return numCan;
    }

    public void setNumCan(String numCan) {
        this.numCan = numCan;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
