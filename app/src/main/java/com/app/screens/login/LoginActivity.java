package com.app.screens.login;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.app.R;
import com.app.base.BaseActivity;
import com.app.model.Error;
import com.app.model.LoginResponse;

/**
 * This class is used for login activity
 */
@SuppressLint("StaticFieldLeak")
public class LoginActivity extends BaseActivity implements LoginContract.View {

    private static final String TAG = LoginActivity.class.getCanonicalName();
    private LoginPresenter loginPresenter;
    String store;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createPresenter();
    }


    @Override
    public void createPresenter() {

    }

    @Override
    public void showLoginSuccess() {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void loginSuccessResponse(LoginResponse loginResponse) {

    }


    @Override
    public void loginFailureResponse(Error t) {

    }

    @Override
    public void showValidationError(String msg) {

    }

    @Override
    public void invalidEmail() {

    }

    @Override
    public void invalidPAssword() {

    }
}
