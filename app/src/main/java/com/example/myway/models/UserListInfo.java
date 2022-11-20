package com.example.myway.models;

public class UserListInfo {
    private String user_id;
    private String userEmail;
    private String userPassword;
    private String userName;


    public UserListInfo(String user_id, String userEmail,String userPassword,String userName) {
        this.user_id = user_id;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
