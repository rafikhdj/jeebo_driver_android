package com.app.utils;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.R;
import com.app.base.BaseActivity;

import java.io.ByteArrayOutputStream;

import static android.os.Build.VERSION_CODES.M;

public class GalleryCameraHandlingManager {
    private static final String TAG = GalleryCameraHandlingManager.class.getSimpleName();
    private static GalleryCameraHandlingManager galleryCameraHandlingManager;
    private BaseActivity activity;
    private int mediaState;
    //private IDataListener iDataListener;
    private ImageUriProvider imageUriProvider;
    private Uri mCapturedImageURI;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static GalleryCameraHandlingManager getInstance() {
        if (galleryCameraHandlingManager == null) {
            galleryCameraHandlingManager = new GalleryCameraHandlingManager();
        }
        return galleryCameraHandlingManager;
    }

    public void captureMedia(BaseActivity activity, boolean isCamera, int mediaState, ImageUriProvider imageUriProvider) {
        this.activity = activity;
        this.mediaState = mediaState;
        this.imageUriProvider = imageUriProvider;
        if (Build.VERSION.SDK_INT >= M) {
            if (isCamera)
                checkCameraPerimission(activity, mediaState);
            else
                checkGalleryPerimission(activity, mediaState);
        } else {
            perfomMediaCapture(activity, mediaState);

        }

    }

    private void perfomMediaCapture(BaseActivity activity, int mediaState) {
        switch (mediaState) {
            case AppConstant.MEDIA_STATE.CAPTURE_IMAGE:
                mCapturedImageURI = BitmapUtils.onOpenCameraImage(activity, AppConstant.REQUEST_CODE.CAPTURE_IMAGE);
                break;

            case AppConstant.MEDIA_STATE.GALLARY_IMAGE:
                mCapturedImageURI = BitmapUtils.onOpenGallaryImage(activity, AppConstant.REQUEST_CODE.GALLARY_IMAGE);
                break;
        }
    }

    private void checkCameraPerimission(BaseActivity activity, int mediaState) {
        String[] whatPermission = {AppConstant.PERMISSION.CAMERA, AppConstant.PERMISSION.WRITE_EXTERNAL};
        if (ContextCompat.checkSelfPermission(activity, whatPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    whatPermission,
                    AppConstant.REQUEST_CODE.CAMERA_PERMISSION);
            mCheckPermission(activity);
            //if(!mCheckPermission(activity))
            // Toast.makeText(activity, "Please provide Camera Permission in Setting to access Camera", Toast.LENGTH_SHORT).show();
        } else {
            perfomMediaCapture(activity, mediaState);
        }
    }

    private void checkGalleryPerimission(BaseActivity activity, int mediaState) {
        String whatPermission = AppConstant.PERMISSION.WRITE_EXTERNAL;
        if (ContextCompat.checkSelfPermission(activity, whatPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{whatPermission},
                    AppConstant.REQUEST_CODE.GALLERY_PERMISSION);
            mCheckPermission(activity);
            // Toast.makeText(activity, "Please provide Storage Permission in Setting to access Camera", Toast.LENGTH_SHORT).show();
        } else {
            perfomMediaCapture(activity, mediaState);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // image capture from camera
            case AppConstant.REQUEST_CODE.CAPTURE_IMAGE:
                sendBackImageToFragment(data);
                break;

            // single image capture from gallery
            case AppConstant.REQUEST_CODE.GALLARY_IMAGE:
                sendBackImageToFragment(data);
                break;
            default:
                break;
        }
    }

    private void sendBackImageToFragment(Intent data) {

        if (data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            imageUriProvider.getImagePathWithUri(BitmapUtils.getPathFromURI(activity, imageUri),imageUri);
        } else {
            try{
                imageUriProvider.getImagePathWithUri(BitmapUtils.getPathFromURI(activity, mCapturedImageURI),null);
            }catch (Exception e){
                /*if(activity != null)
                activity.showToast("Some error occured, please try again");*/
            }

        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case AppConstant.REQUEST_CODE.CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    perfomMediaCapture(activity, mediaState);
                }else{
                    Toast.makeText(activity, R.string.camera_permission_msg, Toast.LENGTH_SHORT).show();

                }
                break;
            case AppConstant.REQUEST_CODE.GALLERY_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    perfomMediaCapture(activity, mediaState);
                else
                    Toast.makeText(activity, R.string.gallery_permission_msg, Toast.LENGTH_SHORT).show();

                break;
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private static boolean mCheckPermission(Context context){
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog(context.getString(R.string.external_storage), context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }

    }
    public static void showDialog(String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_necessary);
        alertBuilder.setMessage(msg + context.getString(R.string.permission_is_necessary));
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    public interface ImageUriProvider {
        void getImagePathWithUri(Object imagepath, Uri uri);
    }



}


