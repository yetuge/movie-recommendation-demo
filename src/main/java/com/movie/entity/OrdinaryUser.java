package com.movie.entity;

import java.util.Date;

/**
 * 普通用户实体类
 */
public class OrdinaryUser {

    private int userId;            // 用户ID
    private String phoneNumber;     // 手机号
    private String userMailbox;    // 邮箱
    private String userPassword;   // 密码
    private String userName;        // 用户名
    private String gender;          // 性别
    private Date birthday;         // 生日
    private Date registerTime;      // 注册时间

    public OrdinaryUser() {
    }

    public OrdinaryUser(int userId, String phoneNumber, String userMailbox, String userPassword,
                        String userName, String gender, Date birthday, Date registerTime) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userMailbox = userMailbox;
        this.userPassword = userPassword;
        this.userName = userName;
        this.gender = gender;
        this.birthday = birthday;
        this.registerTime = registerTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserMailbox() {
        return userMailbox;
    }

    public void setUserMailbox(String userMailbox) {
        this.userMailbox = userMailbox;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "OrdinaryUser{" +
                "userId=" + userId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userMailbox='" + userMailbox + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userName='" + userName + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", registerTime=" + registerTime +
                '}';
    }
}
