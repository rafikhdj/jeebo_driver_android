package com.clientname.androidbase.utils.permissionManager;

import java.util.ArrayList;

/**
 * Created by himanshu on 14/3/18.
 */

public interface IPermissionCallback {

    void PermissionGranted(int request_code);
    void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions);
    void PermissionDenied(int request_code);
    void NeverAskAgain(int request_code);

}
