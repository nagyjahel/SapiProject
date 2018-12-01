package com.example.nagyjahel.sapiads.Main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.Database.User;
import com.example.nagyjahel.sapiads.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;


public class AdDetailFragment extends Fragment {

    private static final String TAG = "AdDetailFragment";
    private FirebaseDatabase database;
    private DatabaseReference advertisementReference;
    private CircleImageView userImage;
    private TextView userName;
    private User publisher;
    private TextView title;
    private TextView content;
    private ImageView image;
    private TextView viewed;
    private ImageView moreButton;
    private Ad selectedAd;


    /*****************************************************************************************************
     The constructor of the Advertisement detail fragment
     *****************************************************************************************************/
    public AdDetailFragment() {
        Log.d(TAG, "Constructor called");
        database = FirebaseDatabase.getInstance();
    }


    /*****************************************************************************************************
     The onCreateView method of the Advertisement detail fragment
     - Initiation of the view
     - Filling its' fields with corresponding data
     *****************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView method called.");
        Bundle args = getArguments();
        Gson gson = new Gson();
        selectedAd= gson.fromJson(args.getString("currentAd"), Ad.class);
        publisher = gson.fromJson(args.getString("currentUser"), User.class);
        advertisementReference = database.getReference("ads/"+selectedAd.getId()+"/viewed");
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        initView(view);
        fillViewWithCorrespondingData(view);
        incrementNumberOfViewsOnAd();
        return view;

    }


    /*****************************************************************************************************
     The initView method of the Advertisement detail fragment
     - Setting the corresponding layout elements to the member variables of this class
     - Setting up various listeners
     *****************************************************************************************************/
    private void initView(View view) {
        Log.d(TAG, "initView method called.");
        title = view.findViewById(R.id.ad_title);
        content = view.findViewById(R.id.ad_content);
        image = view.findViewById(R.id.ad_image);
        userImage = view.findViewById(R.id.ad_user_image);
        userName = view.findViewById(R.id.ad_user_name);
        viewed = view.findViewById(R.id.viewed_nr);
        moreButton = view.findViewById(R.id.more_button);

        moreButton.setOnClickListener(new View.OnClickListener() {

            @Override  public void onClick(View v) {
            }
        });

    }


    /*****************************************************************************************************
     The fillViewWithCorrespondingData method of the Advertisement detail fragment
     - Filling the view's fields with actual data
     *****************************************************************************************************/
    private void fillViewWithCorrespondingData(View view){
        Log.d(TAG, "fillViewWithCorrespondingData method called.");
        title.setText(selectedAd.getTitle());
        content.setText(selectedAd.getContent());
        selectedAd.incrementViewed();
        viewed.setText(String.valueOf(selectedAd.getViewed()));
        Glide.with(view.getContext())
                .load(selectedAd.getImageUrl())
                .into(image);

        userName.setText(publisher.getLastName()+ " " + publisher.getFirstName());

        Glide.with(view.getContext())
                .asBitmap()
                .load(publisher.getPhotoUrl())
                .into(userImage);

    }


    /*****************************************************************************************************
     The incrementNumberOfViewsOnAd method of the Advertisement detail fragment
     - The number of views on an advertisement increments with every click on it.
     This function saves the incremented value to the database/.
     *****************************************************************************************************/
    private void incrementNumberOfViewsOnAd(){
        Log.d(TAG, "incrementNumberOfViewsOnAd method called.");
        advertisementReference.setValue(String.valueOf(selectedAd.getViewed())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("AdDetailFragment:", "Nr of viewed incremented" +  selectedAd.getViewed());
            }});
    }
}
