package com.example.nagyjahel.sapiads.Main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nagyjahel.sapiads.Database.Ad;
import com.example.nagyjahel.sapiads.R;


public class AdDetailFragment extends Fragment {

    private Ad mSelectedAd;
    private ImageView mReport;
    public AdDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = getArguments();
        return inflater.inflate(R.layout.fragment_item, container, false);
    }
}
