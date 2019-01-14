package ro.sapientia.ms.sapvertiser.Main.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.sapientia.ms.sapvertiser.Authentication.AuthenticationActivity;
import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.Main.MainActivity;
import ro.sapientia.ms.sapvertiser.Navigation;
import ro.sapientia.ms.sapvertiser.R;

public class ProfileFragment extends Fragment {

    private TextInputEditText mFirstNameValue;
    private TextInputEditText mLastNameValue;
    private EditText mPhoneNumber;
    private ImageButton mProfilePicture;

    private TextInputLayout mFirstNameInputLayout;
    private TextInputLayout mLastNameInputLayout;
    private TextInputLayout mPhoneNumberInputLayout;

    private Button mSaveButton;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DataHandler dataHandler;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser currentUser;

    private Boolean firstnameUpdated;
    private Boolean lastnameUpdated;



    private User mUser;

    private static final String TAG = "ProfileFragment";

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


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mProfilePicture = view.findViewById(R.id.profileImage);
        mFirstNameInputLayout = view.findViewById(R.id.firstNameInputLayout);
        mLastNameInputLayout = view.findViewById(R.id.lastNameInputLayout);
        mPhoneNumberInputLayout = view.findViewById(R.id.phoneNumberLayout);

        Log.d("TAG", "User: " + currentUser.getPhoneNumber());

        mSaveButton = view.findViewById(R.id.saveButton);
        mFirstNameValue = view.findViewById(R.id.firstNameValue);
        mLastNameValue = view.findViewById(R.id.lastNameValue);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);


        getUserDetails();

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

                if(firstnameUpdated == true && lastnameUpdated == true){
                    Toast.makeText(getActivity(), "Your profile has been updated.",
                            Toast.LENGTH_LONG).show();
                    Fragment fragment = new ProfileFragment();
                    replaceFragment(fragment);

                    //getUserDetails();
                }

            }
        });

        return view;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getUserDetails() {
        DataHandler.getDataHandlerInstance().getUser(currentUser.getPhoneNumber(), new RetrieveDataListener<User>(){
            @Override
            public void onSucces(User data) {
                Log.d(TAG, "Get user from database: success.");
                mPhoneNumber.setText(data.getTelephone());
                mFirstNameValue.setText(data.getFirstName());
                mLastNameValue.setText(data.getLastName());
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Get user from database: failure.");
            }
        });

    }


}
