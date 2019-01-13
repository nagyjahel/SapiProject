package ro.sapientia.ms.sapvertiser.Data.Remote;

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
    void uploadAdvertisement(String key, Map<String,String> values, RetrieveDataListener<String> callback);
    void uploadAdvertisementWithPhotos(Map<String,String> values, RetrieveDataListener<Advertisement> callback, byte[] bytes, String path);
    void reportAdvertisement(long advertisementId, RetrieveDataListener<String> callback);
    void deleteAdvertisement(long advertisementId, RetrieveDataListener<String> callback);
    void incrementViewedNumberOnAd(long advertisementId, int actualNumber,final RetrieveDataListener<String> callback);
}
