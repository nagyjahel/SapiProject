package ro.sapientia.ms.sapvertiser.Authentication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import ro.sapientia.ms.sapvertiser.Main.MainActivity;
import ro.sapientia.ms.sapvertiser.R;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/*********************************************************
 * A login screen that offers authentication via phonenumber.
 * Google and facebook sign in in progress.
 * Registration of a new user or signing in an existing user
 * Testnumber1: +16505553434 verification code: 123456
 * Testnumber2: +16505551212 verification code: 123456
 *********************************************************/

public class AuthenticationActivity extends AppCompatActivity {

    /*********************************************************
     * Basic attributes that are required for signing in or signing up
     *********************************************************/
    private TextInputEditText mPhoneNumber;
    private TextInputEditText mVerificationCode;
    private TextInputLayout mFirstName;
    private TextInputEditText mFirstNameValue;
    private TextInputEditText mLastNameValue;
    private TextInputLayout mLastName;
    private Button mGetCodeButton;
    private Button mSignInButton;
    private Button mRegisterButton;
    private ProgressBar mLoadingBar;

    /*********************************************************
     * Animations between sign in and register views
     *********************************************************/
    private Animation slide_in_left, slide_out_left, slide_in_right, slide_out_right;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String sentCode;
    private String mVerificationId;
    private String phoneNumber;
    private String userInputCode;
    private ViewFlipper authenticationLayout, signInLayout, registerLayout;
    private PhoneAuthCredential phoneCredential;
    private static final String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        this.setTitle("Authentication");
        Log.d(TAG, "Authentication layout called");
        initView();

        /*********************************************************
         * Filling required fields, first the phonenumber to
         * get a verification code in order to sign in.
         *********************************************************/
        mPhoneNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Phone number field changed.");
                mGetCodeButton.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
            }
        });

        mVerificationCode.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Verification code field changed.");
                mSignInButton.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });

        mFirstNameValue.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                Log.d(TAG, "Verification code field changed.");
                mRegisterButton.setVisibility(View.VISIBLE);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });



        /*********************************************************
         * Sending a verification code to the user
         *********************************************************/
        mGetCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Get code button pressed");
                signInLayout.setVisibility(View.VISIBLE);
                authenticationLayout.setVisibility(View.GONE);
                signInLayout.showNext();
                Log.d(TAG, "Sign in layout called");
                mPhoneNumber.setEnabled(false);
                mLoadingBar.setVisibility(View.VISIBLE);
                sendVerificationCode();
                Log.d(TAG, "Phone: " + phoneNumber);
            }
        });

        /*********************************************************
         * After verification code is added, the next step is to
         * sign in the user.
         *********************************************************/
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mVerificationButton.setClickable(false);
                Log.d(TAG, "Sign in button pressed");
                userInputCode = mVerificationCode.getText().toString();
                if (userInputCode != null) {
                    if (userInputCode.equals(sentCode) || userInputCode.equals("123456")) {
                        Log.d(TAG, "The verification code is correct.");
                        mVerificationCode.setEnabled(false);

                        /*********************************************************
                         * Verifying if the current user already exists or is a new one.
                         *********************************************************/
                        userAlreadyExists();
                    } else {
                        Toast.makeText(AuthenticationActivity.this, "Your verification code is NOT correct!",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AuthenticationActivity.this, "Verification code is required.",
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        /*********************************************************
         * If there was a new user, after filling all fields he
         * can register to the app.
         *********************************************************/
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mVerificationButton.setClickable(false);
                Log.d(TAG, "Register button pressed");
                if (mFirstNameValue.getText().toString() != null && mLastNameValue.getText().toString() != null) {
                    registerNewUser();
                } else {
                    Toast.makeText(AuthenticationActivity.this, "Firstname and lastname is required",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
    }


    /*********************************************************
     * Initialize the AuthenticationActivity view
     *********************************************************/
    private void initView() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        authenticationLayout = findViewById(R.id.authenticationLayout);
        signInLayout = findViewById(R.id.signInLayout);
        registerLayout = findViewById(R.id.registerLayout);
        initAnimations();
        authenticationLayout.showNext();

        mAuth = FirebaseAuth.getInstance();
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mPhoneNumber.setEnabled(true);
        mGetCodeButton = findViewById(R.id.getCode);
        mGetCodeButton.setEnabled(true);
        mGetCodeButton.setVisibility(View.INVISIBLE);
        mVerificationCode = findViewById(R.id.verificationCode);
        mSignInButton = findViewById(R.id.signIn);
        mSignInButton.setVisibility(View.INVISIBLE);
        mRegisterButton = findViewById(R.id.register);
        mRegisterButton.setVisibility(View.INVISIBLE);
        mLoadingBar = findViewById(R.id.progress_loader);
        mLoadingBar.setVisibility(View.INVISIBLE);

        mFirstName = findViewById(R.id.firstNameInputLayout);
        mFirstNameValue = findViewById(R.id.firstNameValue);
        mFirstNameValue.setEnabled(true);
        mLastNameValue = findViewById(R.id.lastNameValue);
        mLastName = findViewById(R.id.lastNameInputLayout);
        mLastNameValue.setEnabled(true);
    }

    /*********************************************************
     * Load animations and initialize their attributes.
     *********************************************************/
    private void initAnimations(){

        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        slide_in_right = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slide_out_right = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        authenticationLayout.setInAnimation(slide_in_right);
        signInLayout.setInAnimation(slide_in_right);
        registerLayout.setInAnimation(slide_in_right);

        authenticationLayout.setOutAnimation(slide_out_right);
        signInLayout.setOutAnimation(slide_out_right);
        registerLayout.setOutAnimation(slide_out_right);
    }

    /*********************************************************
     * Near the phone authentication the app allows you
     * to sign in with google or facebook.
     *********************************************************/
    private void otherSignInOptionsDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setTitle("Sign in with");

        /** set the custom dialog components - text, image and button**/
        ImageButton googleSignin = dialog.findViewById(R.id.google_signin);
        ImageButton facebookSignin = dialog.findViewById(R.id.facebook_signin);

        /**if button is clicked, close the custom dialog**/
        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mVerificationButton.setClickable(false);
                Log.d(TAG, "Google sign in button pressed");
                signInWithGoogle();
                dialog.dismiss();
            }
        });
        facebookSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mVerificationButton.setClickable(false);
                Log.d(TAG, "Facebook sign in button pressed");
                signInWithFacebook();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void signInWithFacebook() {
        Log.d(TAG, "Sign in with facebook");
    }

    private void signInWithGoogle() {
        Log.d(TAG, "Sign in with google");

    }


    /*********************************************************
     * Send verification code to current user.
     *********************************************************/
    private void sendVerificationCode() {

        phoneNumber = mPhoneNumber.getText().toString();
        Log.d(TAG, "Phonenumber " + phoneNumber);
        if (phoneNumber.isEmpty()) {
            mPhoneNumber.setError("Phone number is required");
            mPhoneNumber.requestFocus();
            return;
        }

        if (phoneNumber.length() < 10) {
            mPhoneNumber.setError("Please use a valid phone");
            mPhoneNumber.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,       /** Phone number to verify **/
                60,             /** Timeout duration **/
                TimeUnit.SECONDS,  /**Unit of timeout **/
                this,       /** Activity (for callback binding) **/
                mCallbacks);       /** OnVerificationStateChangedCallbacks **/
    }


    /*********************************************************
     * Check the state of the verification process.
     *********************************************************/
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            sentCode = phoneAuthCredential.getSmsCode();
            Log.d(TAG, "Verification completed " + sentCode);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.d(TAG, e.getMessage());
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            String code = s;
            mVerificationId = s;
            Log.d(TAG, "Code " + code);
            mLoadingBar.setVisibility(View.INVISIBLE);
        }
    };


    /*********************************************************
     * Check if the current user already exists or not.
     *********************************************************/
    private void userAlreadyExists() {

        Log.d(TAG, "Check user state");
        databaseReference.child("users/" + mPhoneNumber.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*********************************************************
                 * Case when there is an existing user.
                 *********************************************************/
                if (dataSnapshot.exists() && dataSnapshot.getChildren() != null) {
                    Log.d(TAG, "This user already exists");
                    Toast.makeText(getApplicationContext(),
                            "You have successfully logged in", Toast.LENGTH_LONG).show();
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, userInputCode);
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    /*********************************************************
                     * Case when there is a new user.
                     *********************************************************/
                    Log.d(TAG, "This is a new user");
                    registerLayout.setVisibility(View.VISIBLE);
                    signInLayout.setVisibility(View.GONE);
                    registerLayout.showNext();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    /*********************************************************
     * Registration process of the new user.
     *********************************************************/
    public void registerNewUser() {

        Log.d(TAG, "User registration");
        //get user details
        String firstName = mFirstNameValue.getText().toString();
        String lastName = mLastNameValue.getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("users/" + mPhoneNumber.getText().toString());
        Log.d(TAG, "User details: " + firstName + " " + lastName);
        Map<String, String> map = new HashMap<>();
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("photoUrl", "https://scontent.fotp3-2.fna.fbcdn.net/v/t1.0-9/45669376_2031047590289015_5687033769354067968_o.jpg?_nc_cat=106&_nc_ht=scontent.fotp3-2.fna&oh=0269a86d62af533fbc0e8dc1f3e627b5&oe=5C69F883");

        //add user to Firabase database
        users.setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),
                                "Your registration was successful!", Toast.LENGTH_LONG).show();
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, userInputCode);
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                        Intent homeActivity = new Intent(AuthenticationActivity.this, MainActivity.class);
                        startActivity(homeActivity);
                        finish();
                    }
                });
    }

    /*********************************************************
     * Sign in the current user to app.
     *********************************************************/
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d(TAG, "signInWithCredential called");
        Log.d(TAG, "Credential: " + credential.toString());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ///Sign in success
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                Log.d(TAG, "Current User: " + currentUser.getPhoneNumber());
                            }
                            Log.d(TAG, "User: " + user.getPhoneNumber());
                        } else {
                            ///Sign in failed
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                ///The verification code entered was invalid
                            }
                        }
                    }
                });
    }


}




