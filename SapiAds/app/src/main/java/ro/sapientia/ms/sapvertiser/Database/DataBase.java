package ro.sapientia.ms.sapvertiser.Database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class DataBase {
    private static FirebaseDatabase database;

    public DataBase(){
        database = FirebaseDatabase.getInstance();
    }
    public DatabaseReference usersReference(){
        return database.getReference().child("users");
    }

    public DatabaseReference userReference(String key){
        return database.getReference().child("users/" + key);
    }

    public DatabaseReference advertisementsReference(){
        return database.getReference().child("ads");
    }

    public DatabaseReference advertisementReference(String key){
        return database.getReference().child("ads/" + key);
    }
}
