package com.example.nagyjahel.sapiads.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nagyjahel.sapiads.Authentication.AuthenticationActivity;
import com.example.nagyjahel.sapiads.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent authenticationWindow = new Intent(this, AuthenticationActivity.class);
        startActivity(authenticationWindow);
    }

}
