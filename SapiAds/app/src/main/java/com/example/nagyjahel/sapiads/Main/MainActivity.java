package com.example.nagyjahel.sapiads.Main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.nagyjahel.sapiads.Main.Fragments.AdCreateFragment;
import com.example.nagyjahel.sapiads.Main.Fragments.AdListFragment;
import com.example.nagyjahel.sapiads.Main.Fragments.ProfileFragment;
import com.example.nagyjahel.sapiads.R;

public class MainActivity extends AppCompatActivity {


    private ActionBar mToolbar;
    private static final String TAG = "MainActivity";
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView navigation;
    private static final int REQUEST_CODE = 123;


    /*****************************************************************************************************
     The BottomNavigationView listener
     - Checks if the user clicked on one of the navigation buttons
     - Redirects the user to the corresponding fragment
     *****************************************************************************************************/
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d(TAG, "Home item from the navigation bar selected.");
                    mToolbar.setTitle("News feed");
                    changeFragment(new AdListFragment());
                    return true;
                case R.id.navigation_new_ad:
                    Log.d(TAG, "Plus item from the navigation bar selected.");
                    verifyPermissions();
                    mToolbar.setTitle("Create a new ad");
                    changeFragment(new AdCreateFragment());
                    return true;
                case R.id.navigation_profile:
                    mToolbar.setTitle("My profile page");
                    ProfileFragment profileFragment = new ProfileFragment();
                    fragmentTransaction.replace(R.id.fragment_placeholder, profileFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    Log.d(TAG, "Profile item from the navigation bar selected.");
                    mToolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };


    /*****************************************************************************************************
     The onCreate method of the Main Activity
     - Initiates everything what is necessary for further steps - like database, references, etc.
     - Redirects automatically to the list of the advertisements
     *****************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate method called.");
        initMemberVariables();
        setContentView(R.layout.activity_main);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_placeholder, new AdListFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    /*****************************************************************************************************
     The changeFragment method of the Main Activity
     - Changes the actual fragment with another selected one
     *****************************************************************************************************/
    private void changeFragment(Fragment fragment){
        Log.d(TAG, "changeFragment method called.");
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    /*****************************************************************************************************
     The initMemberVariables method of the Main Activity
     - Initiates all necessary data for further steps, like database, fragmentManager, etc.
     *****************************************************************************************************/
    private void initMemberVariables(){
        Log.d(TAG, "initMemberVariables method called.");
        mToolbar = getSupportActionBar();
        fragmentManager = getSupportFragmentManager();
    }

    /*****************************************************************************************************
     The verifyPermissions method of the Main Activity
     - Asking the user for permissions
     *****************************************************************************************************/
    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions method called.");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED ){
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}
