package com.app.screens.login;


import com.app.model.LoginResponse;


/**
 * This class is for login interactor which will verify the login credentials
 * from the database.After verifying it will allow the user to use the application. */
public class LoginInteract implements LoginContract.Interactor {
    private static final String TAG = LoginInteract.class.getName();
    private LoginPresenter loginPresenter;

     LoginInteract(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    @Override
    public void callLoginNetworkApi(String email, String pwd) {

//        Call<LoginResponse> call = ApiClient.getRequest().login(email, pwd);
//        call.enqueue(new ApiCallback<LoginResponse>() {
//            @Override
//            public void onSucess(LoginResponse loginResponse) {
//
//            }
//
//            @Override
//            public void onError(Error msg) {
//
//            }
//        });
    }

    private void saveUserInfoInPref(LoginResponse loginResponse) {

    }

}
