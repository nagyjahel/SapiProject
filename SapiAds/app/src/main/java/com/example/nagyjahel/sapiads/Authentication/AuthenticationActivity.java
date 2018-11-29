package com.example.nagyjahel.sapiads.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static android.media.AudioTrack.STATE_INITIALIZED;


/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity {


    private EditText mPhoneNumber;
    private EditText mVerificationCode;
    private TextView mRegister;

    private FirebaseAuth mAuth;
    private String codeSent;
    private String userCode;

    private boolean mExist;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mAuth = FirebaseAuth.getInstance();
        mVerificationCode = findViewById(R.id.verificationCode);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mRegister = findViewById(R.id.register);

        findViewById(R.id.verificationButton).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    sendVerificationCode();
                }
        });

        findViewById(R.id.signinButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                verifySignInCode();

            }
        });
    }

    private void verifySignInCode(){

        userCode = mVerificationCode.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, userCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String key = Long.toString(System.currentTimeMillis());
                            final DatabaseReference users = database.getReference("users/" + key);
                                        Map<String,String > map = new HashMap<>();
                                        map.put("phoneNumber", mPhoneNumber.getText().toString());
                                        map.put("firstName", "Jahel");
                                        map.put("lastName", "Nagy");
                                        map.put("photoUrl", "https://scontent.fotp3-2.fna.fbcdn.net/v/t1.0-9/45669376_2031047590289015_5687033769354067968_o.jpg?_nc_cat=106&_nc_ht=scontent.fotp3-2.fna&oh=0269a86d62af533fbc0e8dc1f3e627b5&oe=5C69F883");

                                        users.setValue(map)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                          @Override
                                                                          public void onSuccess(Void aVoid) {
                                                                              Toast.makeText(getApplicationContext(),
                                                                                      "You logged in!", Toast.LENGTH_LONG).show();
                                                                          }
                                                                      });
                                //here we can open a new activity
                            /*Toast.makeText(getApplicationContext(),
                            "Login succesfull", Toast.LENGTH_LONG).show();*/
                            Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect verification code", Toast.LENGTH_LONG).show();
                                // The verification code entered was invalid
                                }
                            }
                        }
                });
    }


    private void sendVerificationCode() {

        String phone = mPhoneNumber.getText().toString();
        if (phone.isEmpty()) {
            mPhoneNumber.setError("Phone number is required");
            mPhoneNumber.requestFocus();
            return;
        }

        if (phone.length() < 10) {
            mPhoneNumber.setError("Please use a valid phone");
            mPhoneNumber.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                codeSent = s;
            }
        };


}

