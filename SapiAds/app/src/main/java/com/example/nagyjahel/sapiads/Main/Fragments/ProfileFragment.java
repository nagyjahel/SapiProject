package com.example.nagyjahel.sapiads.Main.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nagyjahel.sapiads.Database.Models.Ad;
import com.example.nagyjahel.sapiads.Database.Models.User;
import com.example.nagyjahel.sapiads.R;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private EditText mFirstNameValue;
    private EditText mLastNameValue;
    private EditText mPhoneNumber;
    private ImageView mProfilePicture;
    private static final String TAG = "ProfileFragment";

    private String phone;
    private Button mSaveButton;

    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "Profile fragment created");

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Log.d(TAG, "Current user is not null");
            phone = currentUser.getPhoneNumber();

            //String name = currentUser.getDisplayName();
            //String photoUrl = currentUser.getPhotoUrl().toString();
            FirebaseDatabase.getInstance().getReference("users/" + phone).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                        String firstName = (String)dataSnapshot.child("firstName").getValue();
                        String lastName = (String)dataSnapshot.child("lastName").getValue();
                        String photoUrl = (String)dataSnapshot.child("photoUrl").getValue();
                        Log.d(TAG, firstName + " " + lastName + " " + photoUrl);
                        mFirstNameValue.setText(firstName);
                        mLastNameValue.setText(lastName);
                        mPhoneNumber.setText(phone);
                        Glide.with(view.getContext())
                            .asBitmap()
                            .load(photoUrl)
                            .into(mProfilePicture);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

       mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFirstNameValue.getText().toString().equals("") && !mLastNameValue.getText().toString().equals("") && !mPhoneNumber.getText().toString().equals("")) {
                    Map<String, String> map = new HashMap<>();
                    String phone, firstName, lastName, photoUrl;
                    phone = mPhoneNumber.getText().toString();
                    firstName = mFirstNameValue.getText().toString();
                    lastName = mLastNameValue.getText().toString();
                }

            }
        });


        return view;
    }

    private void initView(View view) {
        mSaveButton = view.findViewById(R.id.saveButton);
        mFirstNameValue = view.findViewById(R.id.firstNameValue);
        mLastNameValue = view.findViewById(R.id.lastNameValue);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mProfilePicture = view.findViewById(R.id.profilePicture);
    }



}
