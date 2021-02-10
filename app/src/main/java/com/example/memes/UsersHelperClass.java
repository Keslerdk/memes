package com.example.memes;

public class UsersHelperClass {
    String name, userName, email, password;

    public UsersHelperClass () {

    }

    public UsersHelperClass(String name, String userName, String email) {
        this.name = name;
        this.userName = userName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
