package com.example.nagyjahel.sapiads.Database;

public class Ad {

    private int id;
    private String title;
    private String imageUrl;
    private String content;
    private String publishingUserId;
    private int isReported;
    private int viewed;

    public Ad(String title, String imageUrl, String content, String publishingUserId, int isReported, int viewed) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.content = content;
        this.publishingUserId = publishingUserId;
        this.isReported = isReported;
        this.viewed = viewed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishingUserId() {
        return publishingUserId;
    }

    public void setPublishingUserId(String publishingUserId) {
        this.publishingUserId = publishingUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int isReported() {
        return isReported;
    }

    public void setReported(int reported) {
        isReported = reported;
    }

    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }
}
