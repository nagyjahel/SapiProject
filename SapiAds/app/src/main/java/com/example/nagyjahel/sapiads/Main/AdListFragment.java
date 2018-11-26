package com.example.nagyjahel.sapiads.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.Database.User;
import com.example.nagyjahel.sapiads.R;
import com.example.nagyjahel.sapiads.Main.DummyData.DummyContent;
import com.example.nagyjahel.sapiads.Main.DummyData.DummyContent.DummyItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import static android.support.constraint.Constraints.TAG;


public class AdListFragment extends Fragment {


    private static final String TAG = "AdListFragment";
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Ad> advertisements = new ArrayList<>();
    private AdRecyclerViewAdapter adapter;

    public AdListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initRecyclerView(view);
        downloadData();
        return view;
    }

    private void initRecyclerView(View view) {
        //Log.d(TAG, "initRecyclerView: init recycler view");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new AdRecyclerViewAdapter((FragmentActivity) this.getContext(), users, advertisements);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }


    private void downloadData() {
        UserManager.getUsers(new RetrieveDataListener<ArrayList<User>>() {
            @Override
            public void onSucces(ArrayList<User> data) {
                users.addAll(data);

                AdvertisementManager.getAdvertisements(new RetrieveDataListener<ArrayList<Ad>>() {
                    @Override
                    public void onSucces(ArrayList<Ad> data) {
                        advertisements.addAll(data);
                        Log.d(TAG, "Advertisements nr:" + advertisements.size());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

}
