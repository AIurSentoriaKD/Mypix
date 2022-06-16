package com.sylveon.mypixappv1.Models;

public class RankingCards {
    private int id;
    private String authorname;
    private String illustTitle;
    private String imageUrl;
    private String authorImageUrl;

    public RankingCards(int id, String authorname, String illustTitle, String imageUrl, String authorImageUrl) {
        this.id = id;
        this.authorname = authorname;
        this.illustTitle = illustTitle;
        this.imageUrl = imageUrl;
        this.authorImageUrl = authorImageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorname() {
        return authorname;
    }

    public void setAuthorname(String authorname) {
        this.authorname = authorname;
    }

    public String getIllustTitle() {
        return illustTitle;
    }

    public void setIllustTitle(String illustTitle) {
        this.illustTitle = illustTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }
}
