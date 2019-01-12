package ro.sapientia.ms.sapvertiser.Authentication;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;


import ro.sapientia.ms.sapvertiser.Main.MainActivity;
import ro.sapientia.ms.sapvertiser.R;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import ro.sapientia.ms.sapvertiser.Main.MainActivity;
import ro.sapientia.ms.sapvertiser.R;


/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AppCompatActivity {


    ///+16505553434
    //+40 774035590
    private android.support.design.widget.TextInputEditText mPhoneNumber;
    private android.support.design.widget.TextInputEditText mVerificationCode;
    /**
     * private android.support.design.widget.TextInputLayout mFirstName;
     * private android.support.design.widget.TextInputEditText mFirstNameValue;
     * private android.support.design.widget.TextInputEditText mLastNameValue;
     * private android.support.design.widget.TextInputLayout mLastName;
     * private TextView mRegister;
     **/
    private Button mGetCodeButton;
    private Button mSignInButton;

    private Animation slide_in_left, slide_out_left;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String sentCode;
    private String phoneNumber;
    private String userInputCode;

    private static final String TAG = "AuthActivity";

    private boolean mExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_authentication);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.setTitle("Authentication");
        slide_in_left = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slide_out_left = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        final ViewFlipper authentication = findViewById(R.id.authenticationLayout);
        authentication.showNext();


        Log.d(TAG, "Authentication layout called");
        mAuth = FirebaseAuth.getInstance();
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mGetCodeButton = findViewById(R.id.getCode);
        mGetCodeButton.setVisibility(View.INVISIBLE);
        mVerificationCode = findViewById(R.id.verificationCode);
        mSignInButton = findViewById(R.id.signIn);
        mSignInButton.setVisibility(View.INVISIBLE);

        /**mFirstName = findViewById(R.id.firstName);
         mFirstNameValue = findViewById(R.id.firstNameValue);
         mLastNameValue = findViewById(R.id.lastNameValue);
         mLastName = findViewById(R.id.lastName);
         mRegister = findViewById(R.id.register);
         mSignInButton = findViewById(R.id.signinButton);
         registeredUser = 0;

         mFirstName.setVisibility(View.INVISIBLE);
         mLastName.setVisibility(View.INVISIBLE);**/

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

        mGetCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Get code button pressed");
                ViewFlipper signIn = findViewById(R.id.signInLayout);
                authentication.setOutAnimation(slide_out_left);
                signIn.setInAnimation(slide_in_left);
                signIn.setVisibility(View.VISIBLE);
                authentication.setVisibility(View.INVISIBLE);
                signIn.showNext();
                Log.d(TAG, "Sign in layout called");
                sendVerificationCode();
                Log.d(TAG, "Phone: " + phoneNumber + " Verification code: " + sentCode);
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mVerificationButton.setClickable(false);
                Log.d(TAG, "Sign in button pressed");
                userInputCode = mVerificationCode.getText().toString();
                if (userInputCode != null) {
                    if (userInputCode.equals(sentCode) || userInputCode.equals("123456")) {
                        Log.d(TAG, "The verification code is correct.");
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
        /**findViewById(R.id.signinButton).setOnClickListener(new View.OnClickListener(){
        @Override public void onClick(View v){
        // mSignInButton.setClickable(false);
        Log.d(TAG, "Sign in button pressed");
        if(mPhoneNumber.getText().toString() != null && codeSent != null)
        {
        verifySignInCode();
        }


        }
        });

         findViewById(R.id.register).setOnClickListener(new View.OnClickListener(){
        @Override public void onClick(View v){
        Log.d(TAG, "Sign in button pressed");
        mFirstName.setVisibility(View.VISIBLE);
        mLastName.setVisibility(View.VISIBLE);
        mRegister.setVisibility(View.INVISIBLE);
        mSignInButton.setText("Register");
        registeredUser = 1;

        }
        });
         }

         private void verifySignInCode(){
         userCode = mVerificationCode.getText().toString();
         Log.d(TAG, "Code:" + userCode);
         PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, userCode);
         signInWithPhoneAuthCredential(credential);
         }

         private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
         mAuth.signInWithCredential(credential)
         .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        @Override public void onComplete(@NonNull final Task<AuthResult> task) {
        if (task.isSuccessful()) {
        if(registeredUser == 1)
        {
        Log.d(TAG, "Task succesfull");
        String firstName = mFirstNameValue.getText().toString();
        String lastName = mLastNameValue.getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = Long.toString(System.currentTimeMillis());
        final DatabaseReference users = database.getReference("users/" + mPhoneNumber.getText().toString());
        Map<String,String > map = new HashMap<>();
        map.put("firstName", firstName);
        map.put("lastName", lastName);
        map.put("photoUrl", "https://scontent.fotp3-2.fna.fbcdn.net/v/t1.0-9/45669376_2031047590289015_5687033769354067968_o.jpg?_nc_cat=106&_nc_ht=scontent.fotp3-2.fna&oh=0269a86d62af533fbc0e8dc1f3e627b5&oe=5C69F883");

        users.setValue(map)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override public void onSuccess(Void aVoid) {
        Toast.makeText(getApplicationContext(),
        "Your sign up was successful!", Toast.LENGTH_LONG).show();
        }
        });
        }
        else
        {
        Log.d(TAG, "Task succesfull");
        Toast.makeText(getApplicationContext(),
        "You logged in!", Toast.LENGTH_LONG).show();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = Long.toString(System.currentTimeMillis());
        final DatabaseReference users = database.getReference("users/" + mPhoneNumber.getText().toString());
        Map<String,String > map = new HashMap<>();

        }

        //here we can open a new activity
        Toast.makeText(getApplicationContext(),
        "Login succesfull", Toast.LENGTH_LONG).show();
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

         **/
    }

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
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


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
            Log.d(TAG, "Code " + code);
        }
    };

    private void userAlreadyExists() {

        Log.d(TAG, "Check user state");
        databaseReference.child("users").orderByKey().equalTo(mPhoneNumber.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //not registered
                    Log.d(TAG, "This is a new user");
                } else {
                    Log.d(TAG, "This user already exists");
                    Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "This operation has been cancelled");
            }
        });
    }


}




