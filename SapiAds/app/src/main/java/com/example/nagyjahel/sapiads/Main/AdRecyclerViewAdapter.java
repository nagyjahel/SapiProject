package com.example.nagyjahel.sapiads.Main;

import android.support.annotation.NonNull;
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

    public AdRecyclerViewAdapter(ArrayList<User> Users, ArrayList<Ad> Ads) {
        this.mAds = Ads;
        this.mUsers = Users;
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
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(viewHolder.itemView.getContext())
                .asBitmap()
                .load(mUsers.get(i).getmPhotoUrl())
                .into(viewHolder.userImage);

        Glide.with(viewHolder.itemView.getContext())
                .asBitmap()
                .load(mAds.get(i).getmImageUrl())
                .into(viewHolder.adImage);

        viewHolder.userName.setText(mUsers.get(i).getmFirstName() + mUsers.get(i).getmLastName());
        viewHolder.adTitle.setText(mAds.get(i).getmTitle());
        viewHolder.adContent.setText(mAds.get(i).getmContent());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here goes the sending to the detail page
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