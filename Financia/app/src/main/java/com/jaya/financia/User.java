package com.jaya.financia;

public class User {
    private String email;
    private String fullname;

    public User() {

    }

    public User(String email, String fullname) {
        this.email = email;
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
