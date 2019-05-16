package com.example.thanhtin.ungdungtimdobimat.Class;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {
    private String city,district,title,content,image,post,userID,newsID,datetime,created_at;
    private int type;


    public News() {
    }

    public News(String city, String district, String title, String content, String image, String post, String userID, String newsID, String datetime, String created_at, int type) {
        this.city = city;
        this.district = district;
        this.title = title;
        this.content = content;
        this.image = image;
        this.post = post;
        this.userID = userID;
        this.newsID = newsID;
        this.datetime = datetime;
        this.created_at = created_at;
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
