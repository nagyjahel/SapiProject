package com.example.nagyjahel.sapiads.Main.Fragments;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nagyjahel.sapiads.Main.Helpers.SelectPhotoDialog;
import com.example.nagyjahel.sapiads.Main.Interfaces.OnPhotoSelectedListener;
import com.example.nagyjahel.sapiads.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class AdCreateFragment extends Fragment implements OnPhotoSelectedListener {

    private static final String TAG = "AdCreateFragment";
    private FirebaseAuth loggedUser;
    private FirebaseDatabase database;
    private TextInputEditText adTitle;
    private TextInputEditText adContent;
    private ImageView adImage;
    private Button addButton;
    private DatabaseReference advertisement;
    private String newKey;
    private Bitmap selectedImageBitmap;
    private Uri selectedUri;
    private byte[] uploadedBytes;
    private double progress = 0;
    private String photoToUpload;

    /*****************************************************************************************************
    The constructor of the Advertisement create fragment
     *****************************************************************************************************/
    public AdCreateFragment() {
        Log.d(TAG, "Constructor called.");
        database = FirebaseDatabase.getInstance();
        newKey = Long.toString(System.currentTimeMillis());
        advertisement = database.getReference("ads/" + newKey);
        loggedUser = FirebaseAuth.getInstance();
    }


    /*****************************************************************************************************
     The onCreate method of the Advertisement create fragment
     *****************************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate method called.");
        super.onCreate(savedInstanceState);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    /*****************************************************************************************************
     The onCreateView method of the Advertisement create fragment
     - The initiation of the view
     - The declaration of various listeners
     *****************************************************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreate method called.");
        final View view = inflater.inflate(R.layout.fragment_ad_create, container, false);
        initView(view);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allRequiredDataExist()) {

                    if(selectedImageBitmap != null && selectedUri == null){
                        uploadPhoto(selectedImageBitmap);
                    }
                    else if(selectedImageBitmap == null && selectedUri != null){
                        uploadPhoto(selectedUri);
                    }

                }
                else {
                    Toast toast = Toast.makeText(view.getContext(), "Please, fill all the fields!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        adImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "opening dialog to choose new photo");
                SelectPhotoDialog selectPhotoDialog = new SelectPhotoDialog();
                selectPhotoDialog.show(getFragmentManager(),getString(R.string.dialog_select_photo ));
                selectPhotoDialog.setTargetFragment(AdCreateFragment.this,1);
            }
        });

        return view;
    }

    /*****************************************************************************************************
     The initView method of the Advertisement create fragment
     - Setting the corresponding layout elements to the member variables of this class.
     *****************************************************************************************************/
    private void initView(View view) {
        Log.d(TAG, "initView method called.");
        adTitle = view.findViewById(R.id.new_ad_title);
        adContent = view.findViewById(R.id.new_ad_content);
        addButton = view.findViewById(R.id.new_ad_button);
        adImage = view.findViewById(R.id.new_ad_image);
    }

    /*****************************************************************************************************
     The allRequiredDataExist method of the Advertisement create fragment
     - Verifies if al the required data is set, thus avoiding errors when adding them to database
     *****************************************************************************************************/
    private boolean allRequiredDataExist() {
        Log.d(TAG, "allRequiredDataExist method called.");
        return !isEmpty(adTitle.getText().toString()) && !isEmpty(adContent.getText().toString());
    }


    /*****************************************************************************************************
     The changeFragment method of the Advertisement create fragment
     - Changes the actual fragment to another selected one.
     *****************************************************************************************************/
    private void changeFragment(){
        Log.d(TAG, "changeFragment method called.");
        AdListFragment listFragment = new AdListFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_placeholder, listFragment);
        fragmentTransaction.commit();
    }

    /*****************************************************************************************************
     The prepareData method of the Advertisement create fragment
     - Prepares the entered data to be added into the database
     *****************************************************************************************************/
    private Map<String,String> prepareData(){
        Log.d(TAG, "prepareData method called.");
        Map<String, String> map = new HashMap<>();
        map.put("title", adTitle.getText().toString());
        map.put("content", adContent.getText().toString());
        map.put("imageUrl", photoToUpload);
        map.put("isReported", "0");
        map.put("publishingUserId", loggedUser.getCurrentUser().getPhoneNumber());
        map.put("viewed", "1");
        return map;
    }

    @Override
    public void getImagePath(Uri imagePath) {
        adImage.setImageURI(null);
        adImage.setImageURI(imagePath);
        selectedUri = imagePath;
        selectedImageBitmap = null;
    }

    @Override
    public void getImageBitMap(Bitmap bitmap) {
        adImage.setImageBitmap(bitmap);
        selectedUri = null;
        selectedImageBitmap = bitmap;
    }

    private void uploadPhoto(Bitmap bitmap){
        Log.d(TAG, "Upload new image bitmap to storage");
        BackgroundImageResize resize = new BackgroundImageResize(bitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    private void uploadPhoto(Uri imagePath){

        Log.d(TAG, "Upload new photo uri to storage");
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(imagePath);
    }

    private void executeUploadTask(){
        Toast.makeText(getActivity(), "Uploading image",Toast.LENGTH_SHORT).show();
        final StorageReference storageReference  = FirebaseStorage.getInstance().getReference().child("ads/"+ newKey + "/imageUrl");
        UploadTask uploadTask = storageReference.putBytes(uploadedBytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Image successfully uploaded!", Toast.LENGTH_SHORT).show();
                        Uri firebaseUri = taskSnapshot.getUploadSessionUri();
                        photoToUpload = firebaseUri.toString();
                        Log.d(TAG, "OnSucces: firebase download url: " + firebaseUri.toString());


                        advertisement.setValue(prepareData())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "advertisement uploaded.");
                                        Toast toast = Toast.makeText(getActivity(), "Your advertisement has been successfully uploaded!", Toast.LENGTH_LONG);
                                        toast.show();
                                        changeFragment();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "failure during advertisement uploading.");
                                        Toast toast = Toast.makeText(getActivity(), "An error occured during the upload of your advertisement. Please try again later!", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });

                        resetFields();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Could not upload photo", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double currentProgess = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        if(currentProgess > (progress + 15)){
                            progress = currentProgess;
                            Log.d(TAG, "OnProgress: upload is: " + progress + "% done");
                        }
                    }
                });
    }

    public void resetFields(){
        adTitle.setText("");
        adContent.setText("");
    }

    private class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        private static final String TAG = "BackgroundImageResize";
        private Bitmap bitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if(bitmap!= null){
                this.bitmap = bitmap;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"Compressing image", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.d(TAG, "DoInBackground: started");
            if(bitmap == null){
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uris[0]);
                }catch (IOException e){
                    Log.e(TAG, "DoInBackground: IOException: " + e.getMessage());
                }
            }

            byte[] bytes = null;
            bytes = getBytesFromBitmap(bitmap,100);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            uploadedBytes = bytes;
            executeUploadTask();

        }
    }
}