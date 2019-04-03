package com.app.screens.login;


import com.app.base.BaseActivity;

/**
 * This class is used to navigate to the other screens.
 */
public class LoginRouter implements LoginContract.Router {

    private final BaseActivity baseActivity;

    public LoginRouter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Override
    public void navigateToLandingScreen() {

    }


    @Override
    public void navigateToHomeScreen() {

    }

    @Override
    public void navigateToTutorialScreen() {

    }
}
