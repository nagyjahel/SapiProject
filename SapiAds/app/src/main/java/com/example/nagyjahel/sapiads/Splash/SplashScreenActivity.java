package com.example.nagyjahel.sapiads.Splash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nagyjahel.sapiads.Authentication.AuthenticationActivity;
import com.example.nagyjahel.sapiads.R;



public class SplashScreenActivity extends AppCompatActivity {


    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

<<<<<<< HEAD

=======
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
        Log.d(TAG, "Splash created");
        Button startApp = findViewById(R.id.startApplicationButton);
        startApp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Click detected");
                Intent authentication = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
                startActivity(authentication);
                finish();
                Log.d(TAG, "End app");
            }



        });
<<<<<<< HEAD

=======
>>>>>>> 7e00523b6ba2aba9bbe7f18942de49544600c8dc
    }



}
