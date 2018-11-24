package com.example.nagyjahel.sapiads.Database;

public class Ad {

    private int mId;
    private String mTitle;
    private String mImageUrl;
    private String mContent;
    private User mPublishingUser;

    public Ad(String mTitle, String mImageUrl, String mContent, User mPublishingUser) {
        this.mTitle = mTitle;
        this.mImageUrl = mImageUrl;
        this.mContent = mContent;
        this.mPublishingUser = mPublishingUser;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public User getmPublishingUser() {
        return mPublishingUser;
    }

    public void setmPublishingUser(User mPublishingUser) {
        this.mPublishingUser = mPublishingUser;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
