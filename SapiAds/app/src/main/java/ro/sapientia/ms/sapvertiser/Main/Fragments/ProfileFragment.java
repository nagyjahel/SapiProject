package ro.sapientia.ms.sapvertiser.Main.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
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

            }
        });

        return view;
    }


    private void getUserDetails() {
        DataHandler.getDataHandlerInstance().getUser(currentUser.getPhoneNumber(), new RetrieveDataListener<User>(){
            @Override
            public void onSucces(User data) {
                Log.d(TAG, "Get user from database: success.");
                mPhoneNumber.setText(data.getTelephone());
                mFirstNameValue.setText(data.getFirstName());
                mLastNameValue.setText(data.getLastName());
                mSaveButton.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String message) {
                Log.d(TAG, "Get user from database: failure.");
            }
        });

    }

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
