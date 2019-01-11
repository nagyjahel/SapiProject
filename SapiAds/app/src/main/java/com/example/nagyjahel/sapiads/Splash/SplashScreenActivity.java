package com.example.nagyjahel.sapiads.Splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nagyjahel.sapiads.Authentication.AuthenticationActivity;
import com.example.nagyjahel.sapiads.R;



public class SplashScreenActivity extends AppCompatActivity {


    private static final String TAG = "SplashActivity";
    private ImageView logo;
    private Button startApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        Log.d(TAG, "Splash created");
    }

    private  void initView(){

        logo = findViewById(R.id.logo);

        final int TRANSLATION_Y = logo.getHeight();
        logo.setTranslationY(TRANSLATION_Y);
        logo.setVisibility(View.GONE);
        slideUp(logo);


        startApp = findViewById(R.id.startApplicationButton);
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
    }


    public void slideUp(final View view) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.f);

        if (view.getHeight() > 0) {
            slideUpNow(view);
        } else {
            // wait till height is measured
            view.post(new Runnable() {
                @Override
                public void run() {
                    slideUpNow(view);
                }
            });
        }
    }

    private void slideUpNow(final View view) {
        view.setTranslationY(view.getHeight());
        view.animate()
                .translationY(0)
                .alpha(1.f)
                .setDuration(800)
                .setStartDelay(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.VISIBLE);
                        view.setAlpha(1.f);
                        Intent authentication = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
                        startActivity(authentication);
                        finish();

                    }
                });
    }
}
