package com.example.nagyjahel.sapiads.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.nagyjahel.sapiads.Database.User;
import com.example.nagyjahel.sapiads.Main.AdCreateFragment;
import com.example.nagyjahel.sapiads.Main.AdListFragment;
import com.example.nagyjahel.sapiads.Main.MainActivity;
import com.example.nagyjahel.sapiads.R;
import com.example.nagyjahel.sapiads.Splash.SplashScreenActivity;
import com.google.firebase.auth.FirebaseAuthException;
//import com.google.firebase.auth.FirebaseAuth;


/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";
    //private static final int REQUEST_SIGNUP = 0;

    private EditText mPhoneNumber;
    private EditText mPasswordText;
    private Button mLoginButton;
    private TextView mSignupLink;
    private EditText mFirstName;
    private EditText mLastName;
    private boolean mRegistration = false;

    //private FirebaseAuthException mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Created");



        Log.d(TAG, "Created2");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mPhoneNumber = (EditText) findViewById(R.id.input_phone);
        mPasswordText = findViewById(R.id.input_password);
        mFirstName = findViewById(R.id.input_firstname);
        mLastName = findViewById(R.id.input_lastname);
        mLoginButton = findViewById(R.id.btn_login);
        mSignupLink = findViewById(R.id.link_signup);

        mLoginButton = findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click detected");
                if(mRegistration)
                {
                    signUp();
                }
                else
                {
                    Intent authentication = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(authentication);
                    finish();
                    Log.d(TAG, "End app");
                }

            }



        });

        mSignupLink = findViewById(R.id.link_signup);
        mSignupLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click detected");

                mRegistration = true;
                mFirstName.setVisibility(View.VISIBLE);
                mLastName.setVisibility(View.VISIBLE);
                mLoginButton.setText("Sign Up");
                Log.d(TAG, "End app");
                //signUp();
            }


        });

    }


    public void signUp() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AuthenticationActivity.this,
                R.style.ThemeOverlay_AppCompat_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String phone = mPhoneNumber.getText().toString();
        String password = mPasswordText.getText().toString();
        String lastName = mLastName.getText().toString();
        String firstName = mFirstName.getText().toString();

        User user = new User(phone, firstName, lastName, "");
        Intent mainActivity = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mLoginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = mPhoneNumber.getText().toString();
        String password = mPasswordText.getText().toString();
        String lastName = mLastName.getText().toString();
        String firstName = mFirstName.getText().toString();

        if (phone.isEmpty() || !Patterns.PHONE.matcher(phone).matches()) {
            mPhoneNumber.setError("enter a valid phone number");
            valid = false;
        } else {
            mPhoneNumber.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }
        if (lastName.isEmpty()) {
            mLastName.setError("You must type your last name");
            valid = false;
        } else {
            mLastName.setError(null);
        }
        if (firstName.isEmpty()) {
            mFirstName.setError("You must type your last name");
            valid = false;
        } else {
            mFirstName.setError(null);
        }
        return valid;
    }


}

