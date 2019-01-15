package ro.sapientia.ms.sapvertiser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Navigation {

    private static volatile Navigation navigation = new Navigation();
    private Navigation(){}

    public static Navigation getNavigationInstance(){
        return navigation;
    }

    public void changeFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackStack, Bundle bundle, String tag){
            if(bundle != null){
                fragment.setArguments(bundle);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_placeholder, fragment, tag);
            if(addToBackStack){
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
    }

    public void showFragment(FragmentManager fragmentManager, Fragment fragment, boolean addToBackStack){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_placeholder, fragment);
        if(addToBackStack){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

}
