package ro.sapientia.ms.sapvertiser.Main.Presenters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ro.sapientia.ms.sapvertiser.Database.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Database.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Helpers.AdvertisementRecyclerViewAdapter;

public class AdvertisementListPresenter {

    private ArrayList<Advertisement> advertisements;
    private ArrayList<User> users;
    private View view;
    private AdvertisementRecyclerViewAdapter recyclerViewAdapter;

    public AdvertisementListPresenter(View view){
        this.advertisements = new ArrayList<>();
        this.users = new ArrayList<>();
        this.view = view;

    }

    public void populateUserList(ArrayList<User> users){
        this.users.addAll(users);
    }

    public void populateAdvertisementList(ArrayList<Advertisement> advertisements){
        this.advertisements.addAll(advertisements);
    }
    public interface ViewActions{
        void showAdvertisementList();
        void showNewAdvertisement(int position);
        void hideDeletedAdvertisement(int position);
        void deleteAdvertisementResult();
        void reportAdvertisementResult();
    }


}
