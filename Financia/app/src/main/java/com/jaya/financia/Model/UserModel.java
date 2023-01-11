package com.jaya.financia.Model;

public class UserModel {
    private int id;
    private String name;
    private String email;
    private String user_uid;
    private String imageUrl;

    public UserModel() {
    }

    public UserModel(String name, String email, String user_uid, String imageUrl) {
        this.name = name;
        this.email = email;
        this.user_uid = user_uid;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
