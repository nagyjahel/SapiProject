package com.example.nagyjahel.sapiads.Database.Collections;

import android.util.Log;

import com.example.nagyjahel.sapiads.Database.Models.Ad;
import com.example.nagyjahel.sapiads.Main.Interfaces.RetrieveDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdvertisementManager {

    private static final String TAG = "AdvertisementManager";

    /*****************************************************************************************************
     The getAdvertisements method of the Advertisement Manager
     - Gets all the advertisements from the database
     - Returns a collection of non-reported advertisements.
     *****************************************************************************************************/
    public static void getAdvertisements(final RetrieveDataListener<ArrayList<Ad>> listener) {

        Log.d(TAG, "getAdvertisements method called.");

        // Getting refrence of database node
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ads = database.getReference("ads");

        // Check if we have advertisement node in the database
        if(ads == null){
            Log.d(TAG, "No such reference like 'ads' in the database");
            return;
        }

        // Getting the values of the advertisements node
        ads.addListenerForSingleValueEvent(new ValueEventListener() {

            // New arraylist of the advertisements
            ArrayList<Ad> ads = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange method of the eventlistener called.");
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    if(adSnapshot.exists()){
                        int isReported = Integer.parseInt((String) adSnapshot.child("isReported").getValue());
                        int isVisible = Integer.parseInt((String) adSnapshot.child("isVisible").getValue());
                        if (isReported == 0 && isVisible == 1){
                            ads.add(getDataFromSnapshot(adSnapshot));
                        }
                    }
                }
                listener.onSucces(ads);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled method of the eventlistener called.");
            }
        });
    }


    /*****************************************************************************************************
     The getDataFromSnapshot method of the Advertisement Manager
     - Creates an element of type Ad with the data from the database
     *****************************************************************************************************/
    private static Ad getDataFromSnapshot(DataSnapshot dataSnapshot){

        Log.d(TAG, "getDataFromSnapshot method of the eventlistener called.");
        long id = Long.parseLong(dataSnapshot.getKey());
        int viewed = Integer.parseInt((String) dataSnapshot.child("viewed").getValue());
        int isReported = Integer.parseInt((String) dataSnapshot.child("isReported").getValue());
        int isVisible = Integer.parseInt((String) dataSnapshot.child("isVisible").getValue());
        String title = (String) dataSnapshot.child("title").getValue();
        String photoUrl = (String) dataSnapshot.child("imageUrl").getValue();
        String content = (String) dataSnapshot.child("content").getValue();
        String publishingUserId = (String) dataSnapshot.child("publishingUserId").getValue();
        Log.d(TAG, "new ad: " + title + " " + photoUrl + " " + content + " " + publishingUserId);

        return new Ad(id, title,photoUrl, content,publishingUserId, isReported, isVisible, viewed);
    }



}
