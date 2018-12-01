package com.example.nagyjahel.sapiads.Main.Helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.nagyjahel.sapiads.Database.Models.Ad;
import com.example.nagyjahel.sapiads.Database.Models.User;
import com.example.nagyjahel.sapiads.Main.Fragments.AdDetailFragment;
import com.example.nagyjahel.sapiads.R;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class AdRecyclerViewAdapter extends RecyclerView.Adapter<AdRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "AdRecyclerViewAdapter";
    private ArrayList<User> mUsers;
    private ArrayList<Ad> mAds;
    private FragmentActivity fragmentActivity;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private ActionBar mToolbar;

    /*****************************************************************************************************
     The constructor of the Advertisement Recycler View Adapter
     *****************************************************************************************************/
    public AdRecyclerViewAdapter(FragmentActivity fragmentActivity, ArrayList<User> Users, ArrayList<Ad> Ads) {
        Log.d(TAG, "Constructor called");
        this.mAds = Ads;
        this.mUsers = Users;
        this.fragmentActivity = fragmentActivity;
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
        this.fragmentTransaction =fragmentManager.beginTransaction();

    }


    /*****************************************************************************************************
     The onCreateViewHolder method of the Advertisement Recycler View Adapter
     - Preparing the viewHolder
     *****************************************************************************************************/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder method called");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /*****************************************************************************************************
     The onBindViewHolder method of the Advertisement Recycler View Adapter
     - Preparing the viewHolder
     *****************************************************************************************************/
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder method called");
        final Ad currentAd = mAds.get(i);
        final User currentUser = getUserById(currentAd.getPublishingUserId());
        if(currentUser == null) {
            Log.d(TAG, "User" +currentAd.getPublishingUserId()+ " does not exists in the database.");
            return;
        }
        Log.d("ViewHolderInit: ", currentUser.getFirstName() + ":"  +currentAd.getTitle());
        initViewHolder(viewHolder,currentUser,currentAd);
    }

    /*****************************************************************************************************
     The getUserById method of the Advertisement Recycler View Adapter
     - Finds the corresponding user in the collection based on telephone number
     *****************************************************************************************************/
    @Nullable
    private User getUserById(String telephone) {
        Log.d(TAG, "getUserById method called");
        for(int i=0;i<mUsers.size(); ++i){
            if(mUsers.get(i).getTelephone().equals(telephone)){
                return mUsers.get(i);
            }
        }
        return null;
    }


    /*****************************************************************************************************
     The getItemCount method of the Advertisement Recycler View Adapter
     - Returns the length of the advertisement collection
     *****************************************************************************************************/
    @Override
    public int getItemCount() {
        return mAds.size();
    }


    /*****************************************************************************************************
     The changeFragment method of the Advertisement Recycler View Adapter
     - Prepares the values that have to be passed to the next fragment
     - Changes the current fragment with another selected one
     *****************************************************************************************************/
    private void changeFragment(Ad currentAd, User currentUser, Fragment fragment)
    {
        Log.d(TAG, "changeFragment method called");
        Gson gson = new Gson();
        String adData = gson.toJson(currentAd);
        String userData = gson.toJson(currentUser);

        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("currentAd", adData);
        fragmentArgs.putString("currentUser", userData);

        fragment.setArguments(fragmentArgs);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    /*****************************************************************************************************
     The initViewHolder method of the Advertisement Recycler View Adapter
     - Fills the viewHolder with actual data
     - Sets various listeners
     *****************************************************************************************************/
    private void initViewHolder(ViewHolder viewHolder, final User currentUser, final Ad currentAd){

        Log.d(TAG, "initViewHolder method called");
        Glide.with(viewHolder.itemView.getContext())
                .asBitmap()
                .load(currentUser.getPhotoUrl())
                .into(viewHolder.userImage);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ads/" + currentAd.getId());

        Glide.with(viewHolder.itemView.getContext())
                .load(storageReference)
                .into(viewHolder.adImage);

        viewHolder.userName.setText(currentUser.getLastName() + " " + currentUser.getFirstName());
        viewHolder.adTitle.setText(currentAd.getTitle());
        viewHolder.adContent.setText(currentAd.getContent());
        viewHolder.nrViews.setText(String.valueOf( currentAd.getViewed()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(currentAd, currentUser, new AdDetailFragment());
            }
        });
    }

    /*****************************************************************************************************
     The ViewHolder class
     *****************************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userImage;
        TextView userName;
        TextView adTitle;
        TextView adContent;
        ImageView adImage;
        TextView nrViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder class constructor called");
            userImage = itemView.findViewById(R.id.ad_user_image);
            userName = itemView.findViewById(R.id.ad_user_name);
            adTitle = itemView.findViewById(R.id.ad_title);
            adContent = itemView.findViewById(R.id.ad_content);
            adImage = itemView.findViewById(R.id.ad_image);
            nrViews = itemView.findViewById(R.id.viewed_nr);
        }
    }

}