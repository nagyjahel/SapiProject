package com.example.nagyjahel.sapiads.Main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.Database.User;
import com.example.nagyjahel.sapiads.R;

import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;

public class AdRecyclerViewAdapter extends RecyclerView.Adapter<AdRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "AdRecyclerViewAdapter";
    private ArrayList<User> mUsers;
    private ArrayList<Ad> mAds;
    private FragmentActivity mFragmentActivity;

    public AdRecyclerViewAdapter(FragmentActivity fragmentActivity, ArrayList<User> Users, ArrayList<Ad> Ads) {
        this.mAds = Ads;
        this.mUsers = Users;
        this.mFragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Ad currentAd = mAds.get(i);
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(viewHolder.itemView.getContext())
                .asBitmap()
                .load(mUsers.get(i).getmPhotoUrl())
                .into(viewHolder.userImage);

        Glide.with(viewHolder.itemView.getContext())
                .load(mAds.get(i).getmImageUrl())
                .into(viewHolder.adImage);

        viewHolder.userName.setText(mUsers.get(i).getmFirstName() + mUsers.get(i).getmLastName());
        viewHolder.adTitle.setText(mAds.get(i).getmTitle());
        viewHolder.adContent.setText(mAds.get(i).getmContent());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdDetailFragment detailFragment = new AdDetailFragment();
                Bundle fragmentArgs = new Bundle();
                fragmentArgs.putInt("adId",currentAd.getmId());
                detailFragment.setArguments(fragmentArgs);
                FragmentTransaction ft = mFragmentActivity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.ad_list_fragment, detailFragment);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView userImage;
        TextView userName;
        TextView adTitle;
        TextView adContent;
        ImageView adImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            adTitle = itemView.findViewById(R.id.ad_title);
            adContent = itemView.findViewById(R.id.ad_content);
            adImage = itemView.findViewById(R.id.ad_image);

        }
    }
}