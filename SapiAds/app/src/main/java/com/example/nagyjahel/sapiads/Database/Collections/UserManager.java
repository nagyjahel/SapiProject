package com.example.nagyjahel.sapiads.Database.Collections;

import android.util.Log;

import com.example.nagyjahel.sapiads.Database.Models.User;
import com.example.nagyjahel.sapiads.Main.Interfaces.RetrieveDataListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class UserManager {

    private static final String TAG  = "UserManager";

    public static void getUsers(final RetrieveDataListener<ArrayList<User>> listener) {

        Log.d(TAG, "getUsers method called.");

        // Getting database reference of node "users"
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        // Getting the values from the database
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<User> users = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange method of the eventListener called");
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if(userSnapshot.exists()){
                        User user = getDataFromSnapshot(userSnapshot);
                        if(user != null){
                            users.add(user);
                        }
                    }
                }
                listener.onSucces(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled method of the eventListener called");
            }
        });
    }

    private static User getDataFromSnapshot(DataSnapshot dataSnapshot){
        Log.d(TAG, "getDataFromSnapshot method called.");
        String telephone = dataSnapshot.getKey();
        String firstName = (String)dataSnapshot.child("firstName").getValue();
        String lastName = (String)dataSnapshot.child("lastName").getValue();
        String photoUrl = (String)dataSnapshot.child("photoUrl").getValue();

        if(allRequiredDataExist(telephone,firstName,lastName,photoUrl)){
            Log.d(TAG, "new user: " + firstName + " " + lastName + " " + telephone + " " + photoUrl);
            return new User(telephone,firstName,lastName,photoUrl);
        }

        return null;

    }

    private static boolean allRequiredDataExist(String telephone, String firstName, String lastName, String photoUrl){
        Log.d(TAG, "allRequiredDataExist method called.");
        return telephone != "" && firstName != "" && lastName != "" && photoUrl != "";
    }
}
