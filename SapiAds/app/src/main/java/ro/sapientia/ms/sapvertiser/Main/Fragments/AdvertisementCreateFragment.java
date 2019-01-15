package ro.sapientia.ms.sapvertiser.Main.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ro.sapientia.ms.sapvertiser.Data.Models.Advertisement;
import ro.sapientia.ms.sapvertiser.Data.Models.User;
import ro.sapientia.ms.sapvertiser.Data.Remote.DataHandler;
import ro.sapientia.ms.sapvertiser.Main.Helpers.ImageAdapter;
import ro.sapientia.ms.sapvertiser.Main.Helpers.SelectPhotoDialog;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.OnPhotoSelectedListener;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.RetrieveDataListener;
import ro.sapientia.ms.sapvertiser.Navigation;
import ro.sapientia.ms.sapvertiser.R;

import static android.view.View.GONE;

public class AdvertisementCreateFragment extends DialogFragment implements OnPhotoSelectedListener {

    private static final String TAG = "AdCreateFragment";
    private FirebaseAuth auth;
    private FirebaseUser loggedUser;
    private FirebaseDatabase database;
    private TextInputEditText adTitle;
    private TextInputEditText adContent;
    private TextInputEditText adPrice;
    private ImageView adImage;
    private TextView adImageText;
    private ImageView deleteImage;
    private TextView deleteImageText;
    private Button addButton;
    private Button cancelButton;
    private DatabaseReference advertisement;
    private String newKey;
    private Bitmap selectedImageBitmap;
    private Uri selectedUri = null;
    private ArrayList<Uri> selectedUris = new ArrayList<>();
    private ArrayList<Bitmap> selectedImageBitmaps = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Integer> imagesToDelete = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Uri downloadUrl;
    private String photoToUpload;
    private Advertisement selectedAd;
    private User publisher;
    private ActionBar toolbar;
    private Long advertisementId;
    private ViewPager images;
    private ImageAdapter imageAdapter;
    public AdvertisementCreateFragment() {

    }

    /*****************************************************************************************************
     The constructor of the Advertisement create fragment
     *****************************************************************************************************/
    @SuppressLint("ValidFragment")
    public AdvertisementCreateFragment(ActionBar toolbar) {
        Log.d(TAG, "Constructor called.");
        database = FirebaseDatabase.getInstance();
        newKey = Long.toString(System.currentTimeMillis());
        advertisement = database.getReference("ads/" + newKey);
        auth = FirebaseAuth.getInstance();
        loggedUser = auth.getCurrentUser();
        this.toolbar = toolbar;

    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }

    /*****************************************************************************************************
     The onCreate method of the Advertisement create fragment
     *****************************************************************************************************/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate method called.");
        progressDialog = new ProgressDialog(getActivity());
        getActivity().setTitle("New advertisement");
        super.onCreate(savedInstanceState);
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
        Log.d(TAG, "onCreateView method called.");
        Bundle args = getArguments();
        initView(view, args);
        return view;
    }

    /*****************************************************************************************************
     The initView method of the Advertisement create fragment
     - Setting the corresponding layout elements to the member variables of this class.
     *****************************************************************************************************/
    private void initView(final View view, Bundle args) {
        Log.d(TAG, "initView method called.");
        adTitle = view.findViewById(R.id.new_ad_title);
        adContent = view.findViewById(R.id.new_ad_content);
        adPrice = view.findViewById(R.id.new_ad_price);
        addButton = view.findViewById(R.id.new_ad_button);
        adImage = view.findViewById(R.id.new_ad_image);
        adImageText = view.findViewById(R.id.new_ad_image_text);
        cancelButton = view.findViewById(R.id.cancel);
        addButton.setVisibility(View.INVISIBLE);
        images = view.findViewById(R.id.ad_images);
        imageAdapter = new ImageAdapter(getActivity(),imageUrls);
        images.setAdapter(imageAdapter);

        if (args != null && !args.isEmpty()) {
            advertisementId = args.getLong("adId");
            fillWithData(advertisementId, view);
            addButton.setText("Update");
        } else {
            advertisementId= Long.valueOf(newKey);
            images.setVisibility(GONE);
            addButton.setText("Save");
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAdvertisement(String.valueOf(advertisementId), view);

            }
        });

        adImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "opening dialog to choose new photo");
                images.setVisibility(View.VISIBLE);
                SelectPhotoDialog selectPhotoDialog = new SelectPhotoDialog();
                selectPhotoDialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                selectPhotoDialog.setTargetFragment(AdvertisementCreateFragment.this, 1);
            }
        });

        adImageText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                images.setVisibility(View.VISIBLE);
                Log.d(TAG, "opening dialog to choose new photo");
                SelectPhotoDialog selectPhotoDialog = new SelectPhotoDialog();
                selectPhotoDialog.show(getFragmentManager(), getString(R.string.dialog_select_photo));
                selectPhotoDialog.setTargetFragment(AdvertisementCreateFragment.this, 1);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.getNavigationInstance().changeFragment(getFragmentManager(), new AdvertisementListFragment(), false, null, "AdListFragment");
            }
        });

        adTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!adContent.getText().toString().matches("") && !adPrice.getText().toString().matches("")) {
                    addButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!adTitle.getText().toString().matches("") && !adPrice.getText().toString().matches("")) {
                    addButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        adPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!adTitle.getText().toString().matches("") && !adContent.getText().toString().matches("")) {
                    addButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void uploadAdvertisement(String key, View view) {
        if (allRequiredDataExist()) {


            if (selectedUris.size() != 0 || selectedImageBitmaps.size()!=0) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Uploading your advertisement ... ");
                progressDialog.show();
                DataHandler.getDataHandlerInstance().uploadAdvertisement(Long.valueOf(key), prepareData(), new RetrieveDataListener<String>() {
                    @Override
                    public void onSucces(String data) {
                    }

                    @Override
                    public void onFailure(String message) {
                    }
                });

                uploadPhoto();
            }
            else{
                DataHandler.getDataHandlerInstance().uploadAdvertisement(Long.valueOf(key), prepareData(), new RetrieveDataListener<String>() {
                    @Override
                    public void onSucces(String data) {
                        Toast.makeText(getContext(), "Your advertisement had been uploaded", Toast.LENGTH_SHORT).show();
                        Navigation.getNavigationInstance().changeFragment(getFragmentManager(), new AdvertisementListFragment(), false, null, "AdListFragment");


                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(getContext(), "Something went wrong. Please, try again a bit later!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else {
            Toast toast = Toast.makeText(view.getContext(), "Please, fill all the fields!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /*****************************************************************************************************
     The allRequiredDataExist method of the Advertisement create fragment
     - Verifies if al the required data is set, thus avoiding errors when adding them to database
     *****************************************************************************************************/
    private boolean allRequiredDataExist() {
        Log.d(TAG, "allRequiredDataExist method called.");
        return true;
    }


    /*****************************************************************************************************
     The prepareData method of the Advertisement create fragment
     - Prepares the entered data to be added into the database
     *****************************************************************************************************/
    private Map<String, String> prepareData() {

        Log.d(TAG, "prepareData method called.");
        Log.d("Preparedata", "Title: " + adTitle.getText().toString() + " Content: " + adContent.getText().toString());
        Map<String, String> map = new HashMap<>();
        map.put("title", adTitle.getText().toString());
        map.put("content", adContent.getText().toString());
        map.put("price", adPrice.getText().toString());
        map.put("isReported", "0");
        map.put("isVisible", "1");
        map.put("publishingUserId", loggedUser.getPhoneNumber());
        map.put("viewed", "1");

        return map;
    }


    /*****************************************************************************************************
     The getImagePath method of the Advertisement create fragment
     - Gets the uri of the selected photo, applies it to the imageview
     *****************************************************************************************************/
    @Override
    public void getImagePath(Uri imagePath) {
        imageUrls.add(imagePath.toString());
        imageAdapter.notifyDataSetChanged();
        selectedUris.add(imagePath);
    }


    /*****************************************************************************************************
     The getImageBitMap method of the Advertisement create fragment
     - Displays the taken photo in the imageview
     *****************************************************************************************************/
    @Override
    public void getImageBitMap(Bitmap bitmap) {
        adImage.setImageBitmap(bitmap);
        imageUrls.add(getImageUri(getActivity(),bitmap).toString());
        imageAdapter.notifyDataSetChanged();
        selectedImageBitmaps.add(bitmap);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /*****************************************************************************************************
     The uploadPhoto method of the Advertisement create fragment
     - Will start the upload of the photo - even if it was selected, or taken with camera
     *****************************************************************************************************/
    private void uploadPhoto() {
        progressDialog.incrementProgressBy(10);
        Log.d(TAG, "Upload new image bitmap to storage");
        for(Bitmap bitmap1:selectedImageBitmaps){
            BackgroundImageResize resize = new BackgroundImageResize(bitmap1);
            Uri uri = null;
            resize.execute(uri);
        }


        for(Uri uri: selectedUris){
            BackgroundImageResize resize = new BackgroundImageResize(null);
            resize.execute(uri);
        }

    }

    /*****************************************************************************************************
     The executeUploadTask method of the Advertisement create fragment
     - Will upload the advertisement to the database
     *****************************************************************************************************/
    private void executeUploadTask(byte[] bytes) {
        progressDialog.incrementProgressBy(15);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("ads/" + advertisementId + "/imageUrl"+Long.toString(System.currentTimeMillis()));
        UploadTask uploadTask = storageReference.putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadUrl = uri;
                        DataHandler.getDataHandlerInstance().uploadAdvertisementWithPhoto(advertisementId, prepareData(), uri, new RetrieveDataListener<Advertisement>() {
                            @Override
                            public void onSucces(Advertisement data) {
                                Log.d(TAG, "Successful update: images: " +data.getImageUrl().size());
                                //imageAdapter.notifyDataSetChanged();
                                Navigation.getNavigationInstance().changeFragment(getFragmentManager(), new AdvertisementListFragment(), false, null, "AdListFragment");

                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Could not upload photo", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgess = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.d(TAG, "Current progress: " + currentProgess);
                progressDialog.incrementProgressBy((int) currentProgess);
            }
        });


    }

    public void fillWithData(long selectedAdId, final View view) {
        DataHandler.getDataHandlerInstance().getAdvertisement(selectedAdId, new RetrieveDataListener<Advertisement>() {
            @Override
            public void onSucces(Advertisement advertisement) {
                adTitle.setText(advertisement.getTitle());
                adContent.setText(advertisement.getContent());
                adPrice.setText(advertisement.getPrice());
                addButton.setText("Update");
                imageUrls.addAll(advertisement.getImageUrl());
                imageAdapter = new ImageAdapter(getActivity(), imageUrls);
                images.setAdapter(imageAdapter);
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }

    /*****************************************************************************************************
     The BackgroundImageResize inner class of the Advertisement create fragment
     - Will compress the image in the background
     *****************************************************************************************************/
    private class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]> {

        private static final String TAG = "BackgroundImageResize";
        private Bitmap bitmap;

        public BackgroundImageResize(Bitmap bitmap) {
            if (bitmap != null) {
                this.bitmap = bitmap;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.incrementProgressBy(5);
            Toast.makeText(getActivity(), "Compressing image", Toast.LENGTH_SHORT).show();

        }


        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.d(TAG, "DoInBackground: started");
            if (bitmap == null) {
                try {
                    progressDialog.incrementProgressBy(10);
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uris[0]);
                } catch (IOException e) {
                    Log.e(TAG, "DoInBackground: IOException: " + e.getMessage());
                }
            }

            byte[] bytes = null;
            bytes = getBytesFromBitmap(bitmap, 100);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            progressDialog.incrementProgressBy(10);
            executeUploadTask(bytes);

        }
    }

}