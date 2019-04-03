package com.app.screens.login;

import android.text.TextUtils;

import com.app.model.Error;
import com.app.model.LoginResponse;

/*
This class is used to contain login presenter.
The presenter is responsible to act as the middleman between view and model.
It retrieves data from the model and returns it formatted to the view
*/
public class LoginPresenter implements LoginContract.Presenter {

    private final LoginRouter loginRouter;
    private LoginInteract loginInteract;
    private LoginContract.View loginView;

    LoginPresenter(LoginContract.View view, LoginRouter loginRouter) {
        this.loginView = view;
        this.loginRouter = loginRouter;
        createInteractor(this);
    }

    @Override
    public void validateLoginCred(String email, String pwd) {

        //first we have to check username and password
        if (TextUtils.isEmpty(email)) {
            loginView.invalidEmail();
        } else if (pwd.isEmpty()) {
            loginView.invalidPAssword();
        } else {
            loginView.showProgressDialog();
            loginInteract.callLoginNetworkApi(email, pwd);
        }
    }


    @Override
    public void callLogin() {

    }

    @Override
    public void successLoginApi(LoginResponse object) {
        loginView.hideProgressDialog();
        loginView.showLoginSuccess();
        loginView.loginSuccessResponse(object);
    }

    @Override
    public void failureLoginApi(Error msg) {
        loginView.hideProgressDialog();
        loginView.showValidationError(msg.getErrMsg());
    }

    @Override
    public void createInteractor(LoginPresenter loginPresenter) {
        loginInteract = new LoginInteract(loginPresenter);
    }
}
