package com.example.authenticationpagepractice;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;

public class PhotoUploadNew extends Fragment {

    private static final int TAKE_NEW = 1;
    private static final int FROM_GALLERY = 2;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 3;

    private ImageView chosenImage;
    private Button takeNewButton;
    private Button fromGalleryButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //chosenImage = getView().findViewById(R.id.chosenImage);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_upload_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        takeNewButton = this.getView().findViewById(R.id.takeNewButton);
        fromGalleryButton = this.getView().findViewById(R.id.chooseGalleryButton);

        chosenImage = this.getView().findViewById(R.id.chosenImage);

        takeNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeNew(view);
            }
        });

        fromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromGallery(view);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_NEW: break;
            case FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    System.out.println(data.getDataString());
                    try {
                        InputStream is = getContext().getContentResolver().openInputStream(data.getData());
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        chosenImage.setImageBitmap(bmp);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void takeNew(View view) {

    }

    public void fromGallery(View view) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, FROM_GALLERY);
    }
}
