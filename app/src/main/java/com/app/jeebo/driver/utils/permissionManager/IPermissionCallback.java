package com.app.jeebo.driver.utils.permissionManager;

import java.util.ArrayList;

public interface IPermissionCallback {

    void PermissionGranted(int request_code);
    void PartialPermissionGranted(int request_code, ArrayList<String> denied_permissions, ArrayList<String> allowed_permissions);
    void PermissionDenied(int request_code);
    void NeverAskAgain(int request_code);

}
