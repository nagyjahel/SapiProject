package com.example.nagyjahel.sapiads.Main.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nagyjahel.sapiads.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AdCreateFragment extends Fragment {

    private static final String TAG = "AdCreateFragment";
    private FirebaseAuth loggedUser;
    private FirebaseDatabase database;
    private TextInputEditText adTitle;
    private TextInputEditText adContent;
    private ImageView adImage;
    private Button addButton;
    private DatabaseReference advertisement;
    private String newKey;

    /*****************************************************************************************************
    The constructor of the Advertisement create fragment
     *****************************************************************************************************/
    public AdCreateFragment() {
        Log.d(TAG, "Constructor called.");
        database = FirebaseDatabase.getInstance();
        newKey = Long.toString(System.currentTimeMillis());
        advertisement = database.getReference("ads/" + newKey);
        loggedUser = FirebaseAuth.getInstance();
    }


    /*****************************************************************************************************
     The onCreate method of the Advertisement create fragment
     *****************************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate method called.");
        super.onCreate(savedInstanceState);
    }


    /*****************************************************************************************************
     The onCreateView method of the Advertisement create fragment
     - The initiation of the view
     - The declaration of various listeners
     *****************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate method called.");
        final View view = inflater.inflate(R.layout.fragment_ad_create, container, false);
        initView(view);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allRequiredDataExist()) {
                    advertisement.setValue(prepareData())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "advertisement uploaded.");
                                    Toast toast = Toast.makeText(view.getContext(), "Your advertisement has been successfully uploaded!", Toast.LENGTH_LONG);
                                    toast.show();
                                    changeFragment();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "failure during advertisement uploading.");
                                    Toast toast = Toast.makeText(view.getContext(), "An error occured during the upload of your advertisement. Please try again later!", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            });
                }
            }
        });

        return view;
    }


    /*****************************************************************************************************
     The initView method of the Advertisement create fragment
     - Setting the corresponding layout elements to the member variables of this class.
     *****************************************************************************************************/
    private void initView(View view) {
        Log.d(TAG, "initView method called.");
        adTitle = view.findViewById(R.id.new_ad_title);
        adContent = view.findViewById(R.id.new_ad_content);
        addButton = view.findViewById(R.id.new_ad_button);
    }


    /*****************************************************************************************************
     The allRequiredDataExist method of the Advertisement create fragment
     - Verifies if al the required data is set, thus avoiding errors when adding them to database
     *****************************************************************************************************/
    private boolean allRequiredDataExist() {
        Log.d(TAG, "allRequiredDataExist method called.");
        return !adTitle.getText().toString().equals("") && !adContent.getText().toString().equals("");
    }


    /*****************************************************************************************************
     The prepareData method of the Advertisement create fragment
     - Prepares the entered data to be added into the database
     *****************************************************************************************************/
    private Map<String,String> prepareData(){
        Log.d(TAG, "prepareData method called.");
        Map<String, String> map = new HashMap<>();
        map.put("title", adTitle.getText().toString());
        map.put("content", adContent.getText().toString());
        map.put("imageUrl", "");
        map.put("isReported", "0");
        map.put("publishingUserId", loggedUser.getCurrentUser().getPhoneNumber());
        map.put("viewed", "1");
        return map;
    }


    /*****************************************************************************************************
     The changeFragment method of the Advertisement create fragment
     - Changes the actual fragment to another selected one.
     *****************************************************************************************************/
    private void changeFragment(){
        Log.d(TAG, "changeFragment method called.");
        AdListFragment listFragment = new AdListFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, listFragment);
        fragmentTransaction.commit();
    }
}