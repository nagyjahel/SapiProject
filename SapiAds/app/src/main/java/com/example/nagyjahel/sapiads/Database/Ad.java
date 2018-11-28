package com.example.nagyjahel.sapiads.Database;

import com.example.nagyjahel.sapiads.Main.RetrieveDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Ad {

    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void incrementViewed() {this.viewed++;}
}
