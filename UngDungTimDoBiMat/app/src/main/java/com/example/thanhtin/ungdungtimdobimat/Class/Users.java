package com.example.thanhtin.ungdungtimdobimat.Class;

import java.util.Date;

public class Users {
    private String address,postaddress,email,image,name,userID,birthday,phone;


    public Users() {
    }

    public Users(String address, String postaddress, String email, String image, String name, String userID, String birthday, String phone) {
        this.address = address;
        this.postaddress = postaddress;
        this.email = email;
        this.image = image;
        this.name = name;
        this.userID = userID;
        this.birthday = birthday;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostaddress() {
        return postaddress;
    }

    public void setPostaddress(String postaddress) {
        this.postaddress = postaddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
