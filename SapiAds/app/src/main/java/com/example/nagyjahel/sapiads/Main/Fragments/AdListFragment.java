package com.example.nagyjahel.sapiads.Main.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nagyjahel.sapiads.Database.Models.Ad;
import com.example.nagyjahel.sapiads.Database.Models.User;
import com.example.nagyjahel.sapiads.Main.Helpers.AdRecyclerViewAdapter;
import com.example.nagyjahel.sapiads.Database.Collections.AdvertisementManager;
import com.example.nagyjahel.sapiads.Main.Interfaces.RetrieveDataListener;
import com.example.nagyjahel.sapiads.Database.Collections.UserManager;
import com.example.nagyjahel.sapiads.R;

import java.util.ArrayList;


public class AdListFragment extends Fragment {


    private static final String TAG = "AdListFragment";
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Ad> advertisements = new ArrayList<>();
    private AdRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

    /*****************************************************************************************************
     The constructor of the Advertisement list fragment
     *****************************************************************************************************/
    public AdListFragment() {

        Log.d(TAG, "constructor called");
    }


    /*****************************************************************************************************
     The onCreateView method of the Advertisement list fragment
     - Initiation of the view
     - Getting the data from the database for the list which is going to be shown
     *****************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView method called");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initRecyclerView(view);
        downloadData();
        return view;
    }


    /*****************************************************************************************************
     The initRecyclerView method of the Advertisement list fragment
     - Finding the recycler view in layout
     - Setting its' adapter and layout manager.
     *****************************************************************************************************/
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView method called");
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdRecyclerViewAdapter((FragmentActivity) this.getContext(), users, advertisements);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }


    /*****************************************************************************************************
     The downloadData method of the Advertisement list fragment
     - Getting the necessary collections of datas for realising the list: the advertisements and their publishers.
     *****************************************************************************************************/
    private void downloadData() {
        Log.d(TAG, "downloadData method called");
        UserManager.getUsers(new RetrieveDataListener<ArrayList<User>>() {
            @Override
            public void onSucces(ArrayList<User> data) {
                Log.d(TAG, "Get users from database: success.");
                users.addAll(data);
                AdvertisementManager.getAdvertisements(new RetrieveDataListener<ArrayList<Ad>>() {
                    @Override
                    public void onSucces(ArrayList<Ad> data) {
                        Log.d(TAG, "Get advertisements from database: success.");
                        advertisements.addAll(data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d(TAG, "Get advertisements from database: failure.");
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Get users from database: failure.");
            }
        });
    }

}
