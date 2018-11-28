package com.example.nagyjahel.sapiads.Main;

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

    private Ad mSelectedAd;
    private ImageView mReport;
    private int id;
    private CircleImageView userImage;
    private TextView userName;
    private TextView title;
    private TextView content;
    private ImageView image;
    private TextView viewed;
    private ImageView moreButton;

    public AdDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        Gson gson = new Gson();
        Ad ad = gson.fromJson(args.getString("currentAd"), Ad.class);
        User user = gson.fromJson(args.getString("currentUser"), User.class);
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        initView(view, ad, user);
        return view;

    }

    private void initView(View view, Ad currentAd, User currentUser) {

        title = view.findViewById(R.id.ad_title);
        content = view.findViewById(R.id.ad_content);
        image = view.findViewById(R.id.ad_image);
        userImage = view.findViewById(R.id.ad_user_image);
        userName = view.findViewById(R.id.ad_user_name);
        viewed = view.findViewById(R.id.viewed_nr);
        moreButton = view.findViewById(R.id.more_button);

        moreButton.setOnClickListener(new View.OnClickListener() {

            @Override  public void onClick(View v) {
                //List of items to be show in  alert Dialog are stored in array of strings/char sequences  final
                String[] items = {"Report","Delete"};

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                //set the title for alert dialog
                builder.setTitle("Choose names: ");

                //set items to alert dialog. i.e. our array , which will be shown as list view in alert dialog
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override public void onClick(DialogInterface dialog, int item) {
                        //setting the button text to the selected itenm from the list
                        //button.setText(items[item]);
                    }
                });

                //Creating CANCEL button in alert dialog, to dismiss the dialog box when nothing is selected
                builder .setCancelable(false)
                        .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                            @Override  public void onClick(DialogInterface dialog, int id) {
                                //When clicked on CANCEL button the dalog will be dismissed
                                dialog.dismiss();
                            }
                        });

                //Creating alert dialog
                AlertDialog alert =builder.create();

                //Showingalert dialog
                alert.show();

            }
        });

        title.setText(currentAd.getTitle());
        content.setText(currentAd.getContent());
        userName.setText(currentUser.getLastName()+ " " + currentUser.getFirstName());
        currentAd.incrementViewed();
        viewed.setText(String.valueOf(currentAd.getViewed()));

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ad = database.getReference("ads/"+currentAd.getId()+"/viewed");
        ad.setValue(currentAd.getViewed()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("AdDetailFragment:", "Nr of viewed incremented");
            }});

        Glide.with(view.getContext())
                .asBitmap()
                .load(currentUser.getPhotoUrl())
                .into(userImage);


        Glide.with(view.getContext())
                .load(currentAd.getImageUrl())
                .into(image);

    }
}
