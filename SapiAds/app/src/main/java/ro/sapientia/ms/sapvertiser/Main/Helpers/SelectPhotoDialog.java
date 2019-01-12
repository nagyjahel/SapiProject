package ro.sapientia.ms.sapvertiser.Main.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ro.sapientia.ms.sapvertiser.Main.Interfaces.OnPhotoSelectedListener;
import ro.sapientia.ms.sapvertiser.R;

public class SelectPhotoDialog extends DialogFragment {


    private static final String TAG = "SelectPhotoDialog";
    private static final int PICK_FILE_REQUEST_CODE = 1234;
    private static final int TAKE_PHOTO_REQUEST_CODE = 4321;
    private OnPhotoSelectedListener onPhotoSelectedListener;

    private TextView selectPhoto;
    private TextView takePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_photo, container,false);
        selectPhoto = view.findViewById(R.id.photo_from_gallery);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "photo from the phones memory will be uploaded");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
            }
        });

        takePhoto = view.findViewById(R.id.take_photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "new photo will be taken with camera");
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FILE_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Uri selectedImageUri = data.getData();
            Log.d(TAG,"onActivityResult: selectedImageUri " + selectedImageUri);
            onPhotoSelectedListener.getImagePath(selectedImageUri);
            getDialog().dismiss();
        }
        else if(requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Log.d(TAG,"onActivityResult: done taking photo.");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            onPhotoSelectedListener.getImageBitMap(bitmap);
            getDialog().dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {

        try {
            onPhotoSelectedListener = (OnPhotoSelectedListener) getTargetFragment();
        }
        catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
        super.onAttach(context);
    }
}
