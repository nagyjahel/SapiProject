package ro.sapientia.ms.sapvertiser.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Fragments.AdvertisementCreateFragment;
import ro.sapientia.ms.sapvertiser.Main.Fragments.AdvertisementListFragment;
import ro.sapientia.ms.sapvertiser.Main.Fragments.ProfileFragment;
import ro.sapientia.ms.sapvertiser.Navigation;
import ro.sapientia.ms.sapvertiser.R;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 123;
    private ActionBar mToolbar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView navigation;
    private AdvertisementCreateFragment selectedFragment;
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
                    Navigation.getNavigationInstance().changeFragment(fragmentManager, new AdvertisementListFragment(), true, null, "AdListFragment");
                    return true;
                case R.id.navigation_new_ad:
                    Log.d(TAG, "Plus item from the navigation bar selected.");
                    verifyPermissions();
                    mToolbar.setTitle("Create a new ad");
                    Navigation.getNavigationInstance().changeFragment(fragmentManager, new AdvertisementCreateFragment(mToolbar), true, null, "AdCreateFragment");
                    return true;
                case R.id.navigation_profile:
                    mToolbar.setTitle("My profile page");
                    Navigation.getNavigationInstance().changeFragment(fragmentManager, new ProfileFragment(), true, null, "ProfileFragment");
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
        Navigation.getNavigationInstance().showFragment(fragmentManager, new AdvertisementListFragment(), false);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    /*****************************************************************************************************
     The initMemberVariables method of the Main Activity
     - Initiates all necessary data for further steps, like database, fragmentManager, etc.
     *****************************************************************************************************/
    private void initMemberVariables() {
        Log.d(TAG, "initMemberVariables method called.");
        mToolbar =  getSupportActionBar();
        fragmentManager = getSupportFragmentManager();
    }

    /*****************************************************************************************************
     The verifyPermissions method of the Main Activity
     - Asking the user for permissions
     *****************************************************************************************************/
    private void verifyPermissions() {
        Log.d(TAG, "verifyPermissions method called.");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}