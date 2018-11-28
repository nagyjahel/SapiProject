package com.example.nagyjahel.sapiads.Main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdCreateFragment extends Fragment {

    private TextInputEditText adTitle;
    private TextInputEditText adContent;
    private ImageView adImage;
    private Button addButton;

    public AdCreateFragment() {
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
        final DatabaseReference ad = database.getReference("ads").push();
        final FirebaseAuth user = FirebaseAuth.getInstance();

        final View view = inflater.inflate(R.layout.fragment_ad_create, container, false);
        adTitle = view.findViewById(R.id.new_ad_title);
        adContent = view.findViewById(R.id.new_ad_content);
        addButton = (Button) view.findViewById(R.id.new_ad_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adTitle.getText().toString().equals("") && !adContent.getText().toString().equals("")) {

                    ad.setValue(new Ad(adTitle.getText().toString(),
                            "",
                            adContent.getText().toString(),
                            user.getUid(),
                            0,
                            0))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast toast = Toast.makeText(view.getContext(), "Your advertisement has been successfully uploaded!" , Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast toast = Toast.makeText(view.getContext(), "An error occured during the upload of your advertisement. Please try again later!" , Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });

                }
            }
        });

        return view;
    }
}