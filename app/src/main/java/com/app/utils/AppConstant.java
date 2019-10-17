package com.app.utils;

import android.Manifest;

/**
 * Class is used to define constant items
 */

public class AppConstant {
    public static final String DEVICE_OS = "";

    public class PrefsName {

        public static final String USERNAME = "username";
    }

    public interface INTENT_EXTRAS {
        String SMS_RECEIVED = "sms_received";
    }

    public interface REQUEST_CODE {
        int CAPTURE_IMAGE = 0;
        int GALLARY_IMAGE = 1;
        int CAMERA_PERMISSION = 2;
        int GALLERY_PERMISSION = 3;
        int GALLARY_VIDEO = 4;
        int CAPTURE_VIDEO = 5;
        int MULTIPLE_GALLERY_IMAGE = 6;
        int IMAGE_EDITING_DATA = 7;
        int CAPTURE_IMAGE_PROFILE = 8;
        int GALLARY_IMAGE_PROFILE = 9;
        int UPDATE_POST=10;
        int COMMENT_ON_POST=11;
        int CREATE_POST=12;
        int NEW_MESSAGE=13;

    }


    public interface PERMISSION {
        String CAMERA = Manifest.permission.CAMERA;
        String WRITE_EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE;
    }

    public interface MEDIA_STATE {
        int CAPTURE_IMAGE = 0;
        int GALLARY_IMAGE = 1;
        int GALLARY_VIDEO = 2;
        int CAPTURE_VIDEO = 3;
        int CAPTURE_IMAGE_PROFILE = 8;
        int GALLARY_IMAGE_PROFILE = 9;
    }

}
