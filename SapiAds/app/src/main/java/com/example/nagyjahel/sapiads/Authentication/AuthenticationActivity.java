package com.example.nagyjahel.sapiads.Authentication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nagyjahel.sapiads.R;


/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity {

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

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AuthenticationActivity.this,
                R.style.ThemeOverlay_AppCompat_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

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
        }

        return valid;
    }
*/

}

