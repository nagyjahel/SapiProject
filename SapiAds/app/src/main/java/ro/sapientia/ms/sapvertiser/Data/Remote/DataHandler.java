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
import com.nostra13.universalimageloader.utils.L;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

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
            String price = (String) dataSnapshot.child("price").getValue();
            ArrayList<String> photos = new ArrayList<>();

            for(DataSnapshot dataSnapshot1: dataSnapshot.child("imageUrl").getChildren()){
                photos.add((String)dataSnapshot1.getValue());
            }

            String content = (String) dataSnapshot.child("content").getValue();
            String publishingUserId = (String) dataSnapshot.child("publishingUserId").getValue();
        return new Advertisement(id, title, photos, content, price, publishingUserId, isReported, isVisible, viewed);

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
        firebaseDatabase.getReference("ads").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
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
                Collections.reverse(advertisements);
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

    /*********************************************************
     * Update the database with the current user edited first name.
     *********************************************************/
    @Override
    public void editUserFirstName(final String key, String firstname, final RetrieveDataListener<String> callback) {
        databaseReference.child("users/"+key).child("firstName").setValue(firstname).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSucces(key);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(key);
            }
        });

    }

    /*********************************************************
     * Update the database with the current user edited last name.
     *********************************************************/
    @Override
    public void editUserLastName(final String key, String lastname, final RetrieveDataListener<String> callback) {
        databaseReference.child("users/"+key).child("lastName").setValue(lastname).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSucces(key);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(key);
            }
        });

    }

    @Override
    public void uploadAdvertisement(final long key, final Map<String, String> values, final RetrieveDataListener<String> callback) {
        databaseReference.child("ads/"+key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildren() != null){
                    getAdvertisement(key, new RetrieveDataListener<Advertisement>() {
                        @Override
                        public void onSucces(final Advertisement data) {
                            databaseReference.child("ads/"+key).setValue(values).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(data.getImageUrl().size() == 0){
                                        databaseReference.child("ads/"+key+"/imageUrl").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                    }
                                    else{
                                        for(int i=0; i<  data.getImageUrl().size(); ++i){
                                            databaseReference.child("ads/"+key + "/imageUrl/").child(String.valueOf(i)).setValue( data.getImageUrl().get(i));
                                        }
                                    }

                                    callback.onSucces(String.valueOf(key));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callback.onFailure(String.valueOf(key));
                                }
                            });

                            callback.onSucces(String.valueOf(data.getId()));
                        }


                        @Override
                        public void onFailure(String message) {
                            callback.onFailure("Failure");
                        }
                    });
                }
                else{
                    databaseReference.child("ads/"+key).setValue(values);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }

    @Override
    public void uploadAdvertisementWithPhoto(final long key, Map<String, String> values, final Uri newPhotoUri, final RetrieveDataListener<Advertisement> callback) {
        getAdvertisement(key, new RetrieveDataListener<Advertisement>() {
            @Override
            public void onSucces(Advertisement data) {
                data.getImageUrl().add(newPhotoUri.toString());
                for(int i=0; i<  data.getImageUrl().size(); ++i){
                    databaseReference.child("ads/"+key + "/imageUrl/").child(String.valueOf(i)).setValue( data.getImageUrl().get(i));
                }
                callback.onSucces(data);
            }


            @Override
            public void onFailure(String message) {
                callback.onFailure("Failure");
            }
        });
    }

    @Override
    public void updateAdvertisementPhotos(long advertisementId, ArrayList<String> photos, RetrieveDataListener<String> callback) {
        if(photos.size()!=0){

        }
        databaseReference.child( "ads/" + advertisementId + "/imageUrl").setValue(photos);
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
