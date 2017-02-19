package com.guang.app.model;

/**
 * Created by xiaoguang on 2017/2/19.
 */
public class SearchBookQueryModel {
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    String bookName;

    public SearchBookQueryModel(String bookName) {
        this.bookName = bookName;
    }
}
