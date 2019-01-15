package ro.sapientia.ms.sapvertiser.Data.Models;

import java.util.ArrayList;

public class Advertisement {

    private long id;
    private String title;
    private ArrayList<String> imageUrls;
    private String content;
    private String publishingUserId;
    private int isReported;
    private int isVisible;
    private int viewed;
    private String price;


    public Advertisement(long id, String title, ArrayList<String> imageUrl, String content, String price, String publishingUserId, int isReported, int isVisible, int viewed) {
        this.id = id;
        this.title = title;
        this.imageUrls = imageUrl;
        this.content = content;
        this.price = price;
        this.publishingUserId = publishingUserId;
        this.isReported = isReported;
        this.isVisible = isVisible;
        this.viewed = viewed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrls;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrls = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public int getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public void incrementViewed() {this.viewed++;}

    public int getIsReported() {
        return isReported;
    }

    public void setIsReported(int isReported) {
        this.isReported = isReported;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

}
