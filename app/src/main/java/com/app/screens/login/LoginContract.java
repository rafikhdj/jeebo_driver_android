package com.app.screens.login;

import com.app.base.IBaseContract;
import com.app.model.Error;
import com.app.model.LoginResponse;

/**
 * This class is used to contain all interfaces of login.
 */
public interface LoginContract {

    interface View extends IBaseContract.IView {
        void createPresenter();

        void showLoginSuccess();

        void showProgressDialog();

        void hideProgressDialog();

        void loginSuccessResponse(LoginResponse loginResponse);

        void loginFailureResponse(Error t);

        void showValidationError(String msg);

        void invalidEmail();

        void invalidPAssword();
    }

    interface Router extends IBaseContract.IRouter {
        void navigateToLandingScreen();
        void navigateToHomeScreen();
        void navigateToTutorialScreen();
    }

    interface Presenter extends IBaseContract.IPresentor {
        void validateLoginCred(String email, String pwd);
        void callLogin();
        void successLoginApi(LoginResponse loginResponse);

        void failureLoginApi(Error msg);

        void createInteractor(LoginPresenter loginPresenter);
    }

    interface Interactor extends IBaseContract.IInteractor {
        void callLoginNetworkApi(String email, String pwd);

    }

}
