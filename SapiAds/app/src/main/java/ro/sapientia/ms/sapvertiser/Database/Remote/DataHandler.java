package ro.sapientia.ms.sapvertiser.Database.Remote;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ro.sapientia.ms.sapvertiser.Database.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Database.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;

public class DataHandler implements IDataHandler {

    private static volatile DataHandler dataHandlerInstance = new DataHandler();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private DataHandler() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DataHandler getDataHandlerInstance() {
        return dataHandlerInstance;
    }

    @Override
    public void getUsers(final RetrieveDataListener<ArrayList<User>> callback) {
        firebaseDatabase.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if (userSnapshot.exists()) {
                        User user = getUserFromSnapshot(userSnapshot);
                        if (user != null) {
                            users.add(user);
                        }
                    }
                }
                callback.onSucces(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Getting the collection of the users failed.");
            }
        });
    }

    @Override
    public void getUser(final String userId, final RetrieveDataListener<User> callback) {
        firebaseDatabase.getReference("users/" + userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onSucces(getUserFromSnapshot(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Getting the user with id " + userId + " failed.");
            }
        });
    }

    private static User getUserFromSnapshot(DataSnapshot dataSnapshot) {
        String telephone = dataSnapshot.getKey();
        String firstName = (String) dataSnapshot.child("firstName").getValue();
        String lastName = (String) dataSnapshot.child("lastName").getValue();
        String photoUrl = (String) dataSnapshot.child("photoUrl").getValue();

        if (allRequiredDataExist(telephone, firstName, lastName, photoUrl)) {
            return new User(telephone, firstName, lastName, photoUrl);
        }

        return null;

    }

    private static boolean allRequiredDataExist(String telephone, String firstName, String lastName, String photoUrl) {
        return telephone != "" && firstName != "" && lastName != "" && photoUrl != "";
    }

    @Override
    public void getAdvertisements(final RetrieveDataListener<ArrayList<Advertisement>> callback) {
        firebaseDatabase.getReference("ads").addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<Advertisement> advertisements = new ArrayList<>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot adSnapshot : dataSnapshot.getChildren()) {
                    if (adSnapshot.exists()) {
                        if (hasToBeShown(adSnapshot)) {
                            advertisements.add(getAdvertisementFromSnapshot(adSnapshot));
                        }
                    }
                }
                callback.onSucces(advertisements);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void getAdvertisement(final long advertisementId, final RetrieveDataListener<Advertisement> callback) {
        firebaseDatabase.getReference("ads/" + advertisementId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callback.onSucces(getAdvertisementFromSnapshot(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Getting the advertisement with id " + advertisementId + " failed.");
            }
        });
    }

    private static Advertisement getAdvertisementFromSnapshot(DataSnapshot dataSnapshot) {
        long id = Long.parseLong(dataSnapshot.getKey());
        int viewed = Integer.parseInt((String) dataSnapshot.child("viewed").getValue());
        int isReported = Integer.parseInt((String) dataSnapshot.child("isReported").getValue());
        int isVisible = Integer.parseInt((String) dataSnapshot.child("isVisible").getValue());
        String title = (String) dataSnapshot.child("title").getValue();
        String photoUrl = (String) dataSnapshot.child("imageUrl").getValue();
        String content = (String) dataSnapshot.child("content").getValue();
        String publishingUserId = (String) dataSnapshot.child("publishingUserId").getValue();
        return new Advertisement(id, title, photoUrl, content, publishingUserId, isReported, isVisible, viewed);
    }

    private boolean hasToBeShown(DataSnapshot dataSnapshot) {
        if (Integer.parseInt((String) dataSnapshot.child("isReported").getValue()) == 0 && Integer.parseInt((String) dataSnapshot.child("isVisible").getValue()) == 1) {
            return true;
        }
        return false;
    }
}
