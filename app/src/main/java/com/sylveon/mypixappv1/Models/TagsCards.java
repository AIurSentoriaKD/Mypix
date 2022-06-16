package com.sylveon.mypixappv1.Models;

public class TagsCards {
    private String tagName, tagTrad;

    public TagsCards(String tagName, String tagTrad) {
        this.tagName = tagName;
        this.tagTrad = tagTrad;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagTrad() {
        return tagTrad;
    }

    public void setTagTrad(String tagTrad) {
        this.tagTrad = tagTrad;
    }
}
