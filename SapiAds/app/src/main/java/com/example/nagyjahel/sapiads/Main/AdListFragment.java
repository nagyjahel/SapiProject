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

    private ArrayList<User> mUsers = new ArrayList<>();
    private ArrayList<Ad> mAds  = new ArrayList<>();

    public AdListFragment() {



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ads = database.getReference("ads");

        ads.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {

                    String title = adSnapshot.child("title").getValue(String.class);
                    String photoUrl = adSnapshot.child("photoUrl").getValue(String.class);
                    String content = adSnapshot.child("content").getValue(String.class);
                    String publishingUserId = adSnapshot.child("publishingUserId").getValue(String.class);

                    mAds.add(new Ad(title, photoUrl, content, publishingUserId, 0, 1));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        final DatabaseReference users = database.getReference("users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers.add(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view){
        //Log.d(TAG, "initRecyclerView: init recycler view");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        AdRecyclerViewAdapter adapter = new AdRecyclerViewAdapter((FragmentActivity) this.getContext(),mUsers, mAds);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }


}
