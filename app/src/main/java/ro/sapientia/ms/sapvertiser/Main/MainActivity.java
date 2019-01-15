package ro.sapientia.ms.sapvertiser.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.sapientia.ms.sapvertiser.Authentication.AuthenticationActivity;
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
    private Menu menu;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BottomNavigationView navigation;
    private AdvertisementCreateFragment selectedFragment;
    private FirebaseUser loggedUser;
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
                    mToolbar.setTitle("Advertisements");
                    Navigation.getNavigationInstance().changeFragment(fragmentManager, new AdvertisementListFragment(), true, null, "AdListFragment");
                    return true;
                case R.id.navigation_new_ad:
                    Log.d(TAG, "Plus item from the navigation bar selected.");
                    verifyPermissions();
                    mToolbar.setTitle("New advertisement");
                    Navigation.getNavigationInstance().changeFragment(fragmentManager, new AdvertisementCreateFragment(mToolbar, loggedUser), true, null, "AdCreateFragment");
                    return true;
                case R.id.navigation_profile:
                    mToolbar.setTitle("My profile");
                    Navigation.getNavigationInstance().changeFragment(fragmentManager, new ProfileFragment(), true, null, "ProfileFragment");
                    Log.d(TAG, "Profile item from the navigation bar selected.");
                    mToolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tollbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOutPopUp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOutPopUp() {
        AlertDialog dialog;
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        alertDialog.setTitle("Log out");
        alertDialog.setMessage("Are you sure you want to log out?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //code to log out user
                FirebaseAuth.getInstance().signOut();
                Intent startApp = new Intent(MainActivity.this, AuthenticationActivity.class);
                startActivity(startApp);
                finish();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = alertDialog.create();
        dialog.show();
    }

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
        loggedUser = FirebaseAuth.getInstance().getCurrentUser();

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