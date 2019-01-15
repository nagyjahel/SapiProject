package ro.sapientia.ms.sapvertiser.Main.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Helpers.AdvertisementReportDeleteDialog;
import ro.sapientia.ms.sapvertiser.Main.Helpers.ImageAdapter;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.OnDialogButtonClicked;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.R;

import static android.view.View.GONE;


public class AdvertisementDetailFragment extends Fragment {

    private static final String TAG = "AdDetailFragment";
    private CircleImageView userImage;
    private TextView userName;
    private User publisher;
    private TextView title;
    private TextView content;
    private ImageView image;
    private TextView viewed;
    private ImageView moreButton;
    private Advertisement selectedAd;
    private FirebaseAuth auth;
    private FirebaseUser loggedUser;
    private OnDialogButtonClicked listener;
    private RetrieveDataListener<String> onDeleteListener;
    private RetrieveDataListener<String> onReportListener;
    private ViewPager imageSlider;
    private TextView price;
    public AdvertisementDetailFragment(){

    }

    /*****************************************************************************************************
     The constructor of the Advertisement detail fragment
     *****************************************************************************************************/
    @SuppressLint("ValidFragment")
    public AdvertisementDetailFragment(RetrieveDataListener<String> onDeleteListener, RetrieveDataListener<String> onReportListener) {
        Log.d(TAG, "Constructor called");
        this.auth = FirebaseAuth.getInstance();
        this.loggedUser = auth.getCurrentUser();
        this.onDeleteListener = onDeleteListener;
        this.onReportListener = onReportListener;
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


        getDataFromArguments(getArguments());
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        initView(view);
        fillViewWithCorrespondingData(view);
        return view;

    }

    public void getDataFromArguments(Bundle args){
        Gson gson = new Gson();
        selectedAd= gson.fromJson(args.getString("currentAd"), Advertisement.class);
        publisher = gson.fromJson(args.getString("currentUser"), User.class);
    }

    /*****************************************************************************************************
     The initView method of the Advertisement detail fragment
     - Setting the corresponding layout elements to the member variables of this class
     - Setting up various listeners
     *****************************************************************************************************/
    private void initView(View view) {
        Log.d(TAG, "initView method called.");
        ImageAdapter imageAdapter = new ImageAdapter(getActivity(), selectedAd.getImageUrl());

        title = view.findViewById(R.id.ad_title);
        content = view.findViewById(R.id.ad_content);
        price = view.findViewById(R.id.price_value);
        userImage = view.findViewById(R.id.ad_user_image);
        userName = view.findViewById(R.id.ad_user_name);
        viewed = view.findViewById(R.id.viewed_nr);
        imageSlider = view.findViewById(R.id.ad_images);
        imageSlider.setAdapter(imageAdapter);
        price = view.findViewById(R.id.price_value);



        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AdvertisementReportDeleteDialog dialog = new AdvertisementReportDeleteDialog(selectedAd.getId(), listener, onDeleteListener, onReportListener);
                dialog.show(getFragmentManager(),getString(R.string.dialog_manage_advertisement ));
                dialog.setTargetFragment(AdvertisementDetailFragment.this,1);
                return true;
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
        if(selectedAd.getPrice() != null){
            price.setText(selectedAd.getPrice());
        }
        else{
            price.setText("12.5 $");
        }
        selectedAd.incrementViewed();
        viewed.setText(String.valueOf(selectedAd.getViewed()));

        if(selectedAd.getImageUrl().size()==0){
            imageSlider.setVisibility(GONE);
        }
        else{
            imageSlider.setAdapter(new ImageAdapter(getActivity(),selectedAd.getImageUrl()));
        }


        userName.setText(publisher.getLastName()+ " " + publisher.getFirstName());
        Glide.with(view.getContext())
                .asBitmap()
                .load(publisher.getPhotoUrl())
                .into(userImage);

    }

}
