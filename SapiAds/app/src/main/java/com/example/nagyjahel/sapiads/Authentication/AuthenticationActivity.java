package com.example.nagyjahel.sapiads.Authentication;

<<<<<<< HEAD
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
=======
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nagyjahel.sapiads.Main.MainActivity;
import com.example.nagyjahel.sapiads.R;
import com.example.nagyjahel.sapiads.Splash.SplashScreenActivity;


/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity {
<<<<<<< HEAD

    private static final String TAG = "AuthenticationActivity";
    //private static final int REQUEST_SIGNUP = 0;

    private EditText mPhoneNumber;
    private EditText mPasswordText;
    private Button mLoginButton;
    private TextView mSignupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "Created");
        mPhoneNumber = findViewById(R.id.input_phone);
        mPasswordText = findViewById(R.id.input_password);
        mLoginButton = findViewById(R.id.btn_login);
        mSignupLink = findViewById(R.id.link_signup);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mLoginButton = findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click detected");
                Intent authentication = new Intent(AuthenticationActivity.this, MainActivity.class);
                startActivity(authentication);
                finish();
                Log.d(TAG, "End app");
            }



        });
    }

    /*public void login() {
        Log.d(TAG, "Login");

=======

        private static final String TAG = "AuthenticationActivity";
        //private static final int REQUEST_SIGNUP = 0;

        private EditText mEmailText;
        private EditText mPasswordText;
        private Button mLoginButton;
        private TextView mSignupLink;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            Log.d(TAG, "Created");
            mEmailText = findViewById(R.id.input_email);
            mPasswordText = findViewById(R.id.input_password);
            mLoginButton = findViewById(R.id.btn_login);
            mSignupLink = findViewById(R.id.link_signup);

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_authentication);

            /*_loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login();
                }
            });*/
        }

    /*public void login() {
        Log.d(TAG, "Login");

>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
        if (!validate()) {
            onLoginFailed();
            return;
        }

<<<<<<< HEAD
        mLoginButton.setEnabled(false);
=======
        _loginButton.setEnabled(false);
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc

        final ProgressDialog progressDialog = new ProgressDialog(AuthenticationActivity.this,
                R.style.ThemeOverlay_AppCompat_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

<<<<<<< HEAD
        String phone = mPhoneNumber.getText().toString();
        String password = mPasswordText.getText().toString();

        Intent mainActivity = new Intent(AuthenticationActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
=======
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

<<<<<<< HEAD
        mLoginButton.setEnabled(true);
=======
        _loginButton.setEnabled(true);
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
    }

    public boolean validate() {
        boolean valid = true;

<<<<<<< HEAD
        String phone = mPhoneNumber.getText().toString();
        String password = mPasswordText.getText().toString();

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
=======
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
        }

        return valid;
    }
*/

}

