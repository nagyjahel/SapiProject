package com.example.nagyjahel.sapiads.Main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.Database.User;
import com.example.nagyjahel.sapiads.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private EditText mFirstNameValue;
    private EditText mLastNameValue;
    private EditText mPhoneNumber;
    private ImageView mProfilePicture;

    private Button mSaveButton;


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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = Long.toString(System.currentTimeMillis());
        final DatabaseReference ref = database.getReference("ads/" + key);
        final FirebaseAuth user = FirebaseAuth.getInstance();


       /* mSaveButton.setOnClickListener(new View.OnClickListener() {
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
        });*/
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        /*mSaveButton.findViewById(R.id.saveButton);
        mFirstNameValue.findViewById(R.id.firstNameValue);
        mLastNameValue.findViewById(R.id.lastNameValue);
        mPhoneNumber.findViewById(R.id.phoneNumber);

        mPhoneNumber.setText(user.getCurrentUser().getProviderId());
        mFirstNameValue.setText(user.getCurrentUser().getDisplayName());
        mLastNameValue.setText(user.getCurrentUser().getDisplayName());*/
        return view;
    }
}
