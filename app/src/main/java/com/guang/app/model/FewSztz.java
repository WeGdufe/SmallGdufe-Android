package com.guang.app.model;

/**
 * Created by xiaoguang on 2017/2/15.
 */
public class FewSztz {
    /**
     * {"name":"身心素质","requireScore":"1.5","common_listview":"4.4"}
     */
    private String name;
    private String requireScore;
    private String score;

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setRequireScore(String requireScore){
        this.requireScore = requireScore;
    }
    public String getRequireScore(){
        return this.requireScore;
    }
    public void setScore(String score){
        this.score = score;
    }
    public String getScore(){
        return this.score;
    }

}
