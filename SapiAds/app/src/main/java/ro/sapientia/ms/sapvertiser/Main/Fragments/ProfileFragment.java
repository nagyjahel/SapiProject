package ro.sapientia.ms.sapvertiser.Main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Helpers.AdvertisementRecyclerViewAdapter;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.R;
/*********************************************************
 * Profile page of a specific user.
 * Opportunity to update user details, such as last name,
 * first name or the profile picture.
 *********************************************************/
public class ProfileFragment extends Fragment {

    private TextInputEditText mFirstNameValue;
    private TextInputEditText mLastNameValue;
    private EditText mPhoneNumber;
    private CircleImageView mProfilePicture;
    private TextInputLayout mFirstNameInputLayout;
    private TextInputLayout mLastNameInputLayout;
    private TextInputLayout mPhoneNumberInputLayout;
    private Button mSaveButton;
    private Button mMyAds;
    private Button mHideMyAds;
    private FirebaseAuth mAuth;
    private DataHandler dataHandler;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser currentUser;
    private Boolean firstnameUpdated;
    private Boolean lastnameUpdated;
    private User mUser;
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Advertisement> advertisements = new ArrayList<>();
    private AdvertisementRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        initRecyclerView(view);
        downloadData();
        getUserDetails();

        /*********************************************************
         * Check if a field is edited
         *********************************************************/
        mFirstNameValue.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Verification code field changed.");
                mSaveButton.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        mLastNameValue.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Verification code field changed.");
                mSaveButton.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });


        /*********************************************************
         * Edit the current user first name and last name if it's
         * necesarry
         *********************************************************/
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstnameUpdated = false;
                lastnameUpdated = false;
                Log.d(TAG, "Update user information");
                String firstName = mFirstNameValue.getText().toString();
                String lastName = mLastNameValue.getText().toString();

                DataHandler.getDataHandlerInstance().editUserFirstName(currentUser.getPhoneNumber(), firstName, new RetrieveDataListener<String>(){
                    @Override
                    public void onSucces(String message) {
                        Log.d(TAG, "Edit user information: success.");
                        firstnameUpdated = true;

                    }
                    @Override
                    public void onFailure(String message) {
                        Log.d(TAG, "Edit user information: failure.");
                    }
                });

                DataHandler.getDataHandlerInstance().editUserLastName(currentUser.getPhoneNumber(), lastName, new RetrieveDataListener<String>(){
                    @Override
                    public void onSucces(String message) {
                        Log.d(TAG, "Edit user information: success.");
                        lastnameUpdated = true;

                    }
                    @Override
                    public void onFailure(String message) {
                        Log.d(TAG, "Edit user information: failure.");
                    }
                });

                Log.d(TAG, "Update is ready");
                successfulUpdateDialog();
                downloadData();

            }
        });

        mMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.VISIBLE);
                mMyAds.setVisibility(View.GONE);
                mHideMyAds.setVisibility(View.VISIBLE);

            }
        });

        mHideMyAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.setVisibility(View.GONE);
                mHideMyAds.setVisibility(View.GONE);
                mMyAds.setVisibility(View.VISIBLE);

            }
        });

        return view;
    }


    /*********************************************************
     * Initialize the fargment view with attributes and data
     *********************************************************/
    private void initView(View view){
        mProfilePicture = view.findViewById(R.id.profileImage);
        mFirstNameInputLayout = view.findViewById(R.id.firstNameInputLayout);
        mLastNameInputLayout = view.findViewById(R.id.lastNameInputLayout);
        mPhoneNumberInputLayout = view.findViewById(R.id.phoneNumberLayout);

        Log.d("TAG", "User: " + currentUser.getPhoneNumber());

        mSaveButton = view.findViewById(R.id.saveButton);
        mMyAds = view.findViewById(R.id.my_ads);
        mHideMyAds = view.findViewById(R.id.hide_my_ads);
        mHideMyAds.setVisibility(View.GONE);
        mFirstNameValue = view.findViewById(R.id.firstNameValue);
        mLastNameValue = view.findViewById(R.id.lastNameValue);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
    }

    /*********************************************************
     * Get current user details
     *********************************************************/

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
                DataHandler.getDataHandlerInstance().getCurrentUserAdvertisements(currentUser.getPhoneNumber(), new RetrieveDataListener<ArrayList<Advertisement>>() {
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

    private void getUserDetails() {
        DataHandler.getDataHandlerInstance().getUser(currentUser.getPhoneNumber(), new RetrieveDataListener<User>(){
            @Override
            public void onSucces(User data) {
                Log.d(TAG, "Get user from database: success.");
                mPhoneNumber.setText(data.getTelephone());
                mFirstNameValue.setText(data.getFirstName());
                mLastNameValue.setText(data.getLastName());
                Glide.with(getContext())
                        .asBitmap()
                        .load(data.getPhotoUrl())
                        .into(mProfilePicture);
                mSaveButton.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Get user from database: failure.");
            }
        });

    }

    /*********************************************************
     * If user data update was successful a pop up shows up
     *********************************************************/
    private void successfulUpdateDialog() {
        AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Successful update");
        alertDialog.setCancelable(false);

        alertDialog.setIcon(R.drawable.success_icon);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //refresh profile fragment
                Fragment fragment = new ProfileFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.detach(fragment);
                fragmentTransaction.attach(fragment);
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                mSaveButton.setVisibility(View.GONE);

            }
        });

        dialog = alertDialog.create();
        dialog.show();
    }

}
