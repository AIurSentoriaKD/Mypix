package com.sylveon.mypixappv1.Models;

public class IllustrationImages {
    private int pageIndex;
    private int illustId;
    private String illustPageUrl;

    public IllustrationImages(int pageIndex, int illustId, String illustPageUrl) {
        this.pageIndex = pageIndex;
        this.illustId = illustId;
        this.illustPageUrl = illustPageUrl;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getIllustId() {
        return illustId;
    }

    public void setIllustId(int illustId) {
        this.illustId = illustId;
    }

    public String getIllustPageUrl() {
        return illustPageUrl;
    }

    public void setIllustPageUrl(String illustPageUrl) {
        this.illustPageUrl = illustPageUrl;
    }
}
