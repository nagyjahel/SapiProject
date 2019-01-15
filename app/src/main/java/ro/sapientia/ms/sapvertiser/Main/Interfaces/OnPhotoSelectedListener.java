package ro.sapientia.ms.sapvertiser.Main.Interfaces;

import android.graphics.Bitmap;
import android.net.Uri;

public interface OnPhotoSelectedListener {

    void getImagePath(Uri imagePath);
    void getImageBitMap(Bitmap bitmap);
}
