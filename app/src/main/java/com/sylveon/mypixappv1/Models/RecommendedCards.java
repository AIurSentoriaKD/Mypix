package com.sylveon.mypixappv1.Models;

public class RecommendedCards {
    private String userProfile;
    private String userName;
    private String userIlOne;
    private String userIlTwo;
    private Boolean isFollowing;
    private int userId;
    private int followersCount;

    public RecommendedCards(String userProfile, String userName, String userIlOne, String userIlTwo, Boolean isFollowing, int userId, int followersCount) {
        this.userProfile = userProfile;
        this.userName = userName;
        this.userIlOne = userIlOne;
        this.userIlTwo = userIlTwo;
        this.isFollowing = isFollowing;
        this.userId = userId;
        this.followersCount = followersCount;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIlOne() {
        return userIlOne;
    }

    public void setUserIlOne(String userIlOne) {
        this.userIlOne = userIlOne;
    }

    public String getUserIlTwo() {
        return userIlTwo;
    }

    public void setUserIlTwo(String userIlTwo) {
        this.userIlTwo = userIlTwo;
    }

    public Boolean getFollowing() {
        return isFollowing;
    }

    public void setFollowing(Boolean following) {
        isFollowing = following;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }
}
