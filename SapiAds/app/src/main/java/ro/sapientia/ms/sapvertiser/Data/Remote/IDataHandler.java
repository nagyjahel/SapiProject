package ro.sapientia.ms.sapvertiser.Data.Remote;

import android.net.Uri;

import java.util.ArrayList;
import java.util.Map;

import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;

public interface IDataHandler {

    void userExists(RetrieveDataListener<ArrayList<User>> callback);
    void getUsers(RetrieveDataListener<ArrayList<User>> callback);
    void getUser(String userId, RetrieveDataListener<User> callback);

    void getAdvertisements(RetrieveDataListener<ArrayList<Advertisement>> callback);
    void getAdvertisement(long advertisementId, RetrieveDataListener<Advertisement> callback);
    void editUserLastName(String key, String lastname, RetrieveDataListener<String> callback);
    void editUserFirstName(String key, String firstname, RetrieveDataListener<String> callback);
    void uploadAdvertisement(long key, Map<String,String> values, RetrieveDataListener<String> callback);
    void uploadAdvertisementWithPhoto(long advertisementId,  Map<String,String> values, Uri newPhotoUri, RetrieveDataListener<Advertisement> callback);
   void updateAdvertisementPhotos(long advertisementId, ArrayList<Integer> photos, RetrieveDataListener<String> callback);

    void reportAdvertisement(long advertisementId, RetrieveDataListener<String> callback);
    void deleteAdvertisement(long advertisementId, RetrieveDataListener<String> callback);
    void incrementViewedNumberOnAd(long advertisementId, int actualNumber,final RetrieveDataListener<String> callback);
}
