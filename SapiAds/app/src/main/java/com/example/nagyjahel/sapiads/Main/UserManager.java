package com.example.nagyjahel.sapiads.Main;

import android.util.Log;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.Database.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static android.support.constraint.Constraints.TAG;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserManager {

    public static void getUsers(final RetrieveDataListener<ArrayList<User>> listener) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users");

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<User> users = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String telephone = userSnapshot.getKey();
                    String firstName = (String)userSnapshot.child("firstName").getValue();
                    String lastName = (String)userSnapshot.child("lastName").getValue();
                    String photoUrl = (String)userSnapshot.child("photoUrl").getValue();
                    Log.d(TAG, "new user: " + firstName + " " + lastName + " " + telephone + " " + photoUrl);
                    users.add(new User(telephone,firstName,lastName,photoUrl));
                }

                listener.onSucces(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });
    }
}
