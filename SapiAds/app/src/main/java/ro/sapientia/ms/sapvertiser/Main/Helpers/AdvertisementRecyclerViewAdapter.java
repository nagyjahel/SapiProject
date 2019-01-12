package ro.sapientia.ms.sapvertiser.Main.Helpers;

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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Fragments.AdvertisementDetailFragment;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.Navigation;
import ro.sapientia.ms.sapvertiser.R;

import java.util.ArrayList;

public class AdvertisementRecyclerViewAdapter extends RecyclerView.Adapter<AdvertisementRecyclerViewAdapter.ViewHolder> {


    private static final String TAG = "AdvRecyclerViewAdapter";
    private ArrayList<User> mUsers;
    private ArrayList<Advertisement> mAds;
    private FragmentActivity fragmentActivity;

    /*****************************************************************************************************
     The constructor of the Advertisement Recycler View Adapter
     *****************************************************************************************************/
    public AdvertisementRecyclerViewAdapter(FragmentActivity fragmentActivity, ArrayList<User> Users, ArrayList<Advertisement> Ads) {
        this.mAds = Ads;
        this.mUsers = Users;
        this.fragmentActivity = fragmentActivity;
    }


    /*****************************************************************************************************
     The onCreateViewHolder method of the Advertisement Recycler View Adapter
     - Preparing the viewHolder
     *****************************************************************************************************/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder method called");
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_item, viewGroup, false));
    }

    /*****************************************************************************************************
     The onBindViewHolder method of the Advertisement Recycler View Adapter
     - Preparing the viewHolder
     *****************************************************************************************************/
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder method called");
        final Advertisement currentAd = mAds.get(i);
        DataHandler.getDataHandlerInstance().getUser(currentAd.getPublishingUserId(), new RetrieveDataListener<User>() {
            @Override
            public void onSucces(User user) {
                Log.d("ViewHolderInit: ", user.getFirstName() + ":"  +currentAd.getTitle());
                initViewHolder(viewHolder,user,currentAd);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "User" +currentAd.getPublishingUserId()+ " does not exists in the database.");
            }
        });

    }


    /*****************************************************************************************************
     The getItemCount method of the Advertisement Recycler View Adapter
     - Returns the length of the advertisement collection
     *****************************************************************************************************/
    @Override
    public int getItemCount() {
        return mAds.size();
    }



    private Bundle createArguments(Advertisement currentAd, User currentUser){
        Gson gson = new Gson();
        Bundle fragmentArgs = new Bundle();
        fragmentArgs.putString("currentAd", gson.toJson(currentAd));
        fragmentArgs.putString("currentUser", gson.toJson(currentUser));
        return fragmentArgs;
    }
    /*****************************************************************************************************
     The initViewHolder method of the Advertisement Recycler View Adapter
     - Fills the viewHolder with actual data
     - Sets various listeners
     *****************************************************************************************************/
    private void initViewHolder(ViewHolder viewHolder, final User currentUser, final Advertisement currentAd){

        Log.d(TAG, "initViewHolder method called");

        Glide.with(viewHolder.itemView.getContext())
                .asBitmap()
                .load(currentUser.getPhotoUrl())
                .into(viewHolder.userImage);

        Glide.with(viewHolder.itemView.getContext())
                .load(currentAd.getImageUrl())
                .into(viewHolder.adImage);

        viewHolder.userName.setText(currentUser.getLastName() + " " + currentUser.getFirstName());
        viewHolder.adTitle.setText(currentAd.getTitle());
        viewHolder.adContent.setText(currentAd.getContent());
        viewHolder.nrViews.setText(String.valueOf( currentAd.getViewed()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.getNavigationInstance().changeFragment(fragmentActivity.getSupportFragmentManager(), new AdvertisementDetailFragment(),true, createArguments(currentAd,currentUser));
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