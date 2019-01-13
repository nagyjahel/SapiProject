package ro.sapientia.ms.sapvertiser.Data.Remote;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;

public class DataHandler implements IDataHandler {

    private static volatile DataHandler dataHandlerInstance = new DataHandler();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private DataHandler() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static DataHandler getDataHandlerInstance() {
        return dataHandlerInstance;
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

    @Override
    public void userExists(RetrieveDataListener<ArrayList<User>> callback) {

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
                Log.d("DataHandler", "Advertisement id: " + advertisementId);
                callback.onSucces(getAdvertisementFromSnapshot(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure("Getting the advertisement with id " + advertisementId + " failed.");
            }
        });
    }

    @Override
    public void uploadAdvertisement(Map<String, String> values, RetrieveDataListener<Advertisement> callback) {

    }

    @Override
    public void uploadAdvertisementWithPhotos(final Map<String, String> values, final RetrieveDataListener<Advertisement> callback, byte[] bytes, String path) {
        storageReference.putBytes(bytes)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        uploadAdvertisement(values, new RetrieveDataListener<Advertisement>() {
                                            @Override
                                            public void onSucces(Advertisement data) {
                                                callback.onSucces(data);
                                            }

                                            @Override
                                            public void onFailure(String message) {
                                                callback.onFailure(message);
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callback.onFailure("Error uploading the advertisement");
                                    }
                                });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //callback.onProgress();
                    }
                });
    }

    @Override
    public void reportAdvertisement(final long advertisementId, final RetrieveDataListener<String> callback) {
        firebaseDatabase.getReference("ads/" + advertisementId + "/isReported").setValue("1")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSucces(Long.toString(advertisementId));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void deleteAdvertisement(final long advertisementId, final RetrieveDataListener<String> callback) {
        firebaseDatabase.getReference("ads/" + advertisementId + "/isVisible").setValue("0")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.onSucces(Long.toString(advertisementId));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }

    @Override
    public void incrementViewedNumberOnAd(long advertisementId, int actualNumber, final RetrieveDataListener<String> callback) {
        firebaseDatabase.getReference("ads/" + advertisementId + "/viewed").setValue(String.valueOf(actualNumber + 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSucces("Number of views successfully incremented");
            }
        });
    }


    private boolean hasToBeShown(DataSnapshot dataSnapshot) {
        if (hasAllTheProperties(dataSnapshot)) {
            if (Integer.parseInt((String) dataSnapshot.child("isReported").getValue()) == 0 && Integer.parseInt((String) dataSnapshot.child("isVisible").getValue()) == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean hasAllTheProperties(DataSnapshot dataSnapshot) {
        if (dataSnapshot.child("content") != null && dataSnapshot.child("isReported") != null && dataSnapshot.child("isVisible") != null && dataSnapshot.child("publishingUser") != null && dataSnapshot.child("viewed") != null) {
            return true;
        }
        return false;
    }


}
