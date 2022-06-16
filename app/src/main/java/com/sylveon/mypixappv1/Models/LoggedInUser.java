package com.sylveon.mypixappv1.Models;

public class LoggedInUser {
    private int userId;
    private String userName;
    private String userMail;
    private String userProfileUrl;
    private int allowNsfw;
    private int followersCount;
    private String password;

    public LoggedInUser(int userId, String userName, String userMail, String userProfileUrl, int allowNsfw, int followersCount, String password) {
        this.userId = userId;
        this.userName = userName;
        this.userMail = userMail;
        this.userProfileUrl = userProfileUrl;
        this.allowNsfw = allowNsfw;
        this.followersCount = followersCount;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserProfileUrl() {
        return userProfileUrl;
    }

    public void setUserProfileUrl(String userProfileUrl) {
        this.userProfileUrl = userProfileUrl;
    }

    public int getAllowNsfw() {
        return allowNsfw;
    }

    public void setAllowNsfw(int allowNsfw) {
        this.allowNsfw = allowNsfw;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }

}
