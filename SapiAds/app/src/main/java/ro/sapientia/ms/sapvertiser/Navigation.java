package ro.sapientia.ms.sapvertiser;

import ro.sapientia.ms.sapvertiser.Database.Remote.DataHandler;

public class Navigation {

    private static volatile Navigation navigation = new Navigation();
    private Navigation(){};

    public Navigation getNavigationInstance(){
        return navigation;
    }
}
