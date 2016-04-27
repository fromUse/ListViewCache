package com.demo.chen_gui.activity;

/**
 * Created by chen-gui on 16-4-26.
 */
public class NewsBean {

    public NewsBean(String img, String title, String content) {
        this.imgurl = img;
        this.title = title;
        this.content = content;
    }

    public  NewsBean(){

    }

    public String imgurl;
    public String title;
    public String content;

}
