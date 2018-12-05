package com.example.nagyjahel.sapiads.Main.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nagyjahel.sapiads.Database.Models.User;
import com.example.nagyjahel.sapiads.Main.Interfaces.OnPhotoSelectedListener;
import com.example.nagyjahel.sapiads.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdvertisementReportDeleteDialog extends DialogFragment {

    private static final String TAG = "AdReportDeleteDialog";
    private TextView reportAdvertisement;
    private TextView deleteAdvertisement;
    private TextView editAdvertisement;
    private FirebaseAuth auth;
    private FirebaseUser loggedUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private long currentAdId;

    /*****************************************************************************************************
     The default constructor of the AdvertisementReportDeleteDialog class
     *****************************************************************************************************/
    public AdvertisementReportDeleteDialog(){

    }


    /*****************************************************************************************************
     The constructor of the AdvertisementReportDeleteDialog class
     - gets the advertisement Id as parameter
     - initiates the member variables
     *****************************************************************************************************/
    @SuppressLint("ValidFragment")
    public AdvertisementReportDeleteDialog(long adId){
        currentAdId = adId;
        auth = FirebaseAuth.getInstance();
        loggedUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("ads/" + adId);
    }


    /*****************************************************************************************************
     The onCreateView method of the AdvertisementReportDeleteDialog class
     - Sets the listeners to the views.
     *****************************************************************************************************/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_advertisement_report_delete, container,false);

        reportAdvertisement = view.findViewById(R.id.dialog_report_ad);
        deleteAdvertisement = view.findViewById(R.id.dialog_delete_ad);
        editAdvertisement = view.findViewById(R.id.dialog_edit_ad);


        databaseReference.child("publishingUserId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String publishingUserId = (String)dataSnapshot.getValue();

                if(loggedUser.getPhoneNumber().equals(publishingUserId)){
                    editAdvertisement.setVisibility(View.VISIBLE);
                    editAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Edit advertisement", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }
                    });

                    deleteAdvertisement.setVisibility(View.VISIBLE);
                    deleteAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Delete advertisement", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }
                    });

                }
                else {
                    reportAdvertisement.setVisibility(View.VISIBLE);
                    reportAdvertisement.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(),"Thank you for reporting an inappropiate content!", Toast.LENGTH_SHORT).show();
                            getDialog().dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return view;
    }

}
