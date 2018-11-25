package com.example.nagyjahel.sapiads.Main;

import android.util.Log;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class AdvertisementManager {

    public static void getAdvertisements(final RetrieveDataListener<ArrayList<Ad>> listener){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ads = database.getReference("ads");

        ads.addListenerForSingleValueEvent(new ValueEventListener() {

            ArrayList<Ad> ads = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    String title = (String)adSnapshot.child("title").getValue();
                    String photoUrl = (String)adSnapshot.child("imageUrl").getValue();
                    String content = (String)adSnapshot.child("content").getValue();
                    String publishingUserId = (String)adSnapshot.child("publishingUserId").getValue();
                    Log.d(TAG, "new ad: " + title + " " + photoUrl + " " + content + " " + publishingUserId);
                    ads.add(new Ad(title, photoUrl, content, publishingUserId, 0, 1));
                }

                listener.onSucces(ads);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }


}
