package com.example.nagyjahel.sapiads.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nagyjahel.sapiads.R;
import com.example.nagyjahel.sapiads.Main.DummyData.DummyContent;
import com.example.nagyjahel.sapiads.Main.DummyData.DummyContent.DummyItem;


public class AdListFragment extends Fragment {

    public AdListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }
}
