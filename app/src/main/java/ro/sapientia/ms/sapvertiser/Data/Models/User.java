package ro.sapientia.ms.sapvertiser.Data.Models;

import java.util.ArrayList;
/*********************************************************
 * User class with attributes and methods of a user.
 *********************************************************/
public class User {

    private String telephone;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private ArrayList<Advertisement> mPublishedAds;

    public User() {
    }

    public User(String telephone, String firstName, String lastName, String photoUrl) {
        this.telephone = telephone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoUrl = photoUrl;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public ArrayList<Advertisement> getmPublishedAds() {
        return mPublishedAds;
    }


}
