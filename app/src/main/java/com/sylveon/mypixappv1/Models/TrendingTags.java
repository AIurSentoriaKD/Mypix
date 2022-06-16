package com.sylveon.mypixappv1.Models;

public class TrendingTags {
    private String tagName;
    private String tagTranslate;
    private String tagImage;

    public TrendingTags(String tagName, String tagTranslate, String tagImage) {
        this.tagName = tagName;
        this.tagTranslate = tagTranslate;
        this.tagImage = tagImage;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagTranslate() {
        return tagTranslate;
    }

    public void setTagTranslate(String tagTranslate) {
        this.tagTranslate = tagTranslate;
    }

    public String getTagImage() {
        return tagImage;
    }

    public void setTagImage(String tagImage) {
        this.tagImage = tagImage;
    }
}
