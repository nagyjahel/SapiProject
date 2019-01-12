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
    void uploadAdvertisement(Map<String,String> values, RetrieveDataListener<Advertisement> callback);
    void uploadAdvertisementWithPhotos(Map<String,String> values, RetrieveDataListener<Advertisement> callback, byte[] bytes, String path);

    void incrementViewedNumberOnAd(long advertisementId, int actualNumber,final RetrieveDataListener<String> callback);
}
