package com.example.nagyjahel.sapiads.Database;

import java.util.ArrayList;

public class User {

    private String mTelephone;
    private String mFirstName;
    private String mLastName;
    private String mPhotoUrl;
    private ArrayList<Ad> mPublishedAds;


    public User(String mTelephone, String mFirstName, String mLastName, String mPhotoUrl) {
        this.mTelephone = mTelephone;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmTelephone() {
        return mTelephone;
    }

    public void setmTelephone(String mTelephone) {
        this.mTelephone = mTelephone;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public ArrayList<Ad> getmPublishedAds() {
        return mPublishedAds;
    }


}
