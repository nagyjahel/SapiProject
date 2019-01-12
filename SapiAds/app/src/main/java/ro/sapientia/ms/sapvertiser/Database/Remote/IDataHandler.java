package ro.sapientia.ms.sapvertiser.Database.Remote;

import android.telecom.Call;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import ro.sapientia.ms.sapvertiser.Database.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Database.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;

public interface IDataHandler {

    void getUsers(RetrieveDataListener<ArrayList<User>> callback);
    void getUser(String userId, RetrieveDataListener<User> callback);

    void getAdvertisements(RetrieveDataListener<ArrayList<Advertisement>> callback);
    void getAdvertisement(long advertisementId, RetrieveDataListener<Advertisement> callback);
}
