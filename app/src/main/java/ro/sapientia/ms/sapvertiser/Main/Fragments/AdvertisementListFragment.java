package ro.sapientia.ms.sapvertiser.Main.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;

import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Helpers.AdvertisementRecyclerViewAdapter;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.OnDialogButtonClicked;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.Navigation;
import ro.sapientia.ms.sapvertiser.R;


public class AdvertisementListFragment extends Fragment implements OnDialogButtonClicked {


    private static final String TAG = "AdListFragment";
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Advertisement> advertisements = new ArrayList<>();
    private AdvertisementRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;


        @Override
        public void deleteAdvertisementResult() {
            Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(),new AdvertisementListFragment(),false, null, TAG);
            Toast.makeText(getContext(), "Your advertisement has been deleted!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void reportAdvertisementResult() {
            Navigation.getNavigationInstance().changeFragment(getActivity().getSupportFragmentManager(),new AdvertisementListFragment(),false, null,TAG);
            Toast.makeText(getContext(), "Thank you for reporting inappropiate advertisement!", Toast.LENGTH_SHORT).show();
        }



    /*****************************************************************************************************
     The constructor of the Advertisement list fragment
     *****************************************************************************************************/
    public AdvertisementListFragment() {
        Log.d(TAG, "constructor called");
    }


    /*****************************************************************************************************
     The onCreateView method of the Advertisement list fragment
     - Initiation of the view
     - Getting the data from the database for the list which is going to be shown
     *****************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView method called");
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        initRecyclerView(view);
        downloadData();
        return view;
    }


    /*****************************************************************************************************
     The initRecyclerView method of the Advertisement list fragment
     - Finding the recycler view in layout
     - Setting its' adapter and layout manager.
     *****************************************************************************************************/
    private void initRecyclerView(View view) {
        Log.d(TAG, "initRecyclerView method called");
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdvertisementRecyclerViewAdapter((FragmentActivity) this.getContext(), users, advertisements, new RetrieveDataListener<String>() {
            @Override
            public void onSucces(String data) {
                Toast.makeText(getContext(),"Successfull delete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(),"Unsuccessfull delete", Toast.LENGTH_SHORT).show();
            }
        }, new RetrieveDataListener<String>(){

            @Override
            public void onSucces(String data) {
                Toast.makeText(getContext(),"Successfull report", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(),"Unsuccessfull delete", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

    }


    /*****************************************************************************************************
     The downloadData method of the Advertisement list fragment
     - Getting the necessary collections of datas for realising the list: the advertisements and their publishers.
     *****************************************************************************************************/
    private void downloadData() {
        Log.d(TAG, "downloadData method called");
        DataHandler.getDataHandlerInstance().getUsers(new RetrieveDataListener<ArrayList<User>>() {
            @Override
            public void onSucces(ArrayList<User> data) {
                Log.d(TAG, "Get users from database: success.");
                if(users.size() != 0 ){
                    users.clear();
                }
                users.addAll(data);
                DataHandler.getDataHandlerInstance().getAdvertisements(new RetrieveDataListener<ArrayList<Advertisement>>() {
                    @Override
                    public void onSucces(ArrayList<Advertisement> data) {
                        Log.d(TAG, "Get advertisements from database: success.");
                        if(advertisements.size() != 0 ){
                            advertisements.clear();
                        }
                        advertisements.addAll(data);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.d(TAG, "Get advertisements from database: failure.");
                    }
                });
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Get users from database: failure.");
            }
        });
    }

}
