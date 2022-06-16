package com.sylveon.mypixappv1.Models;

public class Illustrations {
    private int illustId, authorId, views, favorites;
    private String title, thumbLink, publishDate;
    private boolean isLiked;

    public Illustrations(int illustId, int authorId, int views, int favorites, String title, String thumbLink, String publishDate, boolean isLiked) {
        this.illustId = illustId;
        this.authorId = authorId;
        this.views = views;
        this.favorites = favorites;
        this.title = title;
        this.thumbLink = thumbLink;
        this.publishDate = publishDate;
        this.isLiked = isLiked;
    }

    public int getIllustId() {
        return illustId;
    }

    public void setIllustId(int illustId) {
        this.illustId = illustId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbLink() {
        return thumbLink;
    }

    public void setThumbLink(String thumbLink) {
        this.thumbLink = thumbLink;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
