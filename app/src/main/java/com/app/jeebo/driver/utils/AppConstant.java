package com.app.jeebo.driver.utils;

import android.Manifest;

/**
 * Class is used to define constant items
 */

public class AppConstant {
    public static final String DEVICE_OS = "ANDROID";
    public static final String APPID = "pFgqwuroTq";
    public static final String SCOPES = "https://www.googleapis.com/auth/plus.login "
            + "https://www.googleapis.com/auth/drive.file";

    public static final String PENDING_ORDER = "pending";
    public static final String CANCELLED_ORDER = "cancelled";
    public static final String COMPLETED_ORDER = "completed";
    public static final String IN_PROCESS_ORDER = "in_process";
    public static final String CONTACT_URL = "https://www.jeeboapp.dz/consumer/contactus";

    public class PrefsName {

        public static final String USERNAME = "username";
        public static final String ACCESS_TOKEN = "access_token";
        public static final String DRIVER_STATUS = "driver_status";
        public static final String DEVICE_TOKEN = "device_token";
        public static final String NAME = "name";
        public static final String IMAGE = "image";
        public static final String EMAIL = "email";
        public static final String USER_ID = "user_id";
        public static final String USER_PHONE = "user_phone";
        public static final String IS_LOGIN = "is_login";
        public static final String IS_CURRENT_ORDER = "is_current_order";
        public static final String CURRENT_ORDER_COUNT = "current_order_count";
        public static final String DEVICE_LANG = "device_lang";
    }

    public interface INTENT_EXTRAS {
        String SMS_RECEIVED = "sms_received";
        String PHONE_NUMBER = "phone_number";
        String ORDER_ID = "order_id";
        String ORDER_POSITION = "order_position";
        String FRAGMENT_TYPE = "fragment_type";
        String ACCESS_TOKEN = "access_token";
        String CAME_FROM = "came_from";
        String PENDING = "pending";
        String COMPLETED = "completed";
        String CANCELLED = "cancelled";
        String IN_PROCESS = "in_process";
        String USER_MODEL = "user_model";
        String USER_ADDRESS = "user_address";
        String NOTIFICATION_TYPE = "notification_type";
    }

    public interface Languages {
        String ENGLISH="English";
        String ENGLISH_CODE="en";
        String FRENCH="français";
        String FRENCH_CODE="fr";
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
