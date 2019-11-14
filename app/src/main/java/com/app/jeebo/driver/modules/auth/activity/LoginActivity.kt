package com.app.jeebo.driver.modules.auth.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.activity.HomeActivity
import com.app.jeebo.driver.modules.auth.model.LoginRequest
import com.app.jeebo.driver.modules.auth.model.SocialLoginReq
import com.app.jeebo.driver.modules.auth.model.UserModel
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.PreferenceKeeper
import com.app.jeebo.driver.utils.Validator
import com.app.jeebo.driver.view.CustomTextView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.Credentials
import java.io.IOException

class LoginActivity : BaseActivity() {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var callbackManager: CallbackManager? = null
    private val GOOGLE_SIGNIN = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init(){

        /*FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener() { object : OnCompleteListener<InstanceIdResult>{
            override fun onComplete(p0: Task<InstanceIdResult>) {
                if(p0.isSuccessful){
                    PreferenceKeeper.getInstance().deviceToken=p0.result?.token
                }
            }

        } }*/

        iv_facebook.setOnClickListener {
            login_button.performClick()
        }

        iv_google.setOnClickListener {
            val signInIntent = mGoogleSignInClient?.getSignInIntent()
            startActivityForResult(signInIntent, GOOGLE_SIGNIN)
        }

        tv_signup.setOnClickListener{
            launchActivity(SignupActivity::class.java)
        }
        tv_forgot_password.setOnClickListener {
            launchActivity(ForgotPasswordActivity::class.java)
        }
        tv_login.setOnClickListener {
            if(checkValidation()){
                showProgressBar(this)
                val authToken = Credentials.basic(et_signin_email.text.toString().trim(), et_signin_password.text.toString().trim())
                val loginRequest = LoginRequest()
                loginRequest.setPlatform(3)
                loginRequest.setDevice_token(PreferenceKeeper.getInstance().deviceToken)
                val request=ApiClient.getRequest()
                val call=request.login(authToken,loginRequest)
                call.enqueue(object : ApiCallback<UserModel>(){
                    override fun onError(error: Error?) {
                        dismissProgressBar()
                        DialogManager.showValidationDialog(this@LoginActivity,error?.errMsg)
                    }

                    override fun onSuccess(t: UserModel?) {
                        dismissProgressBar()
                       if(t!!.isPhoneVerified){
                           //showToast("succes")
                           PreferenceKeeper.getInstance().email=t.email
                           PreferenceKeeper.getInstance().image=t.driver_image_url
                           PreferenceKeeper.getInstance().name=t.name
                           PreferenceKeeper.getInstance().accessToken=t.token
                           PreferenceKeeper.getInstance().userPhone=t.phone_number
                           PreferenceKeeper.getInstance().userId= t.userId.toString()
                           PreferenceKeeper.getInstance().isLogin=true
                           launchActivity(HomeActivity::class.java)
                           finish()
                       }else{
                           val bundle=Bundle()
                           bundle.putString(AppConstant.INTENT_EXTRAS.PHONE_NUMBER,t.phone_number)
                           bundle.putString(AppConstant.INTENT_EXTRAS.ACCESS_TOKEN,t.token)
                           launchActivity(OtpVerificationActivity::class.java,bundle)
                           showToast(t.otp.toString())
                       }
                    }

                })
            }
        }
        initGoogleSignIn()
        initFb()
    }

    private fun checkValidation() : Boolean{
        if(TextUtils.isEmpty(et_signin_email.text.toString().trim())){
            et_signin_email.requestFocus()
           DialogManager.showValidationDialog(this,getString(R.string.email_missing_error))
            return false
        }else if(!Validator.isValidEmail(et_signin_email.text.toString().trim())){
            et_signin_email.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.email_invalid_error))
            return false
        }else if(TextUtils.isEmpty(et_signin_password.text.toString().trim())){
            et_signin_password.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.password_missing_error))
            return false
        }else if(et_signin_password.text.toString().trim().length<6){
            et_signin_password.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.password_invalid_error))
            return false
        }else
            return true
    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this@LoginActivity, gso)
    }

    private fun initFb() {
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("fb", loginResult.toString())
               // callSocialLoginApi("facebook", loginResult.accessToken.token, null)
                callSocialLoginApi(loginResult.accessToken.token)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {
                Log.d("fb", error.toString())
                showToast("Some error occurred, Please try again")
            }
        })

    }

    private fun callSocialLoginApi(token:String){
        showProgressBar(this)
        var socialLoginReq=SocialLoginReq()
        socialLoginReq.platform=3
        socialLoginReq.access_token=token
        socialLoginReq.device_token= PreferenceKeeper.getInstance().deviceToken
        val request=ApiClient.getRequest()
        val call=request.socialLogin(socialLoginReq)
        call.enqueue(object : ApiCallback<UserModel>(){
            override fun onSuccess(t: UserModel?) {
                dismissProgressBar()
                if(t!!.isPhoneVerified) {
                    //showToast("succes")
                    PreferenceKeeper.getInstance().email=t.email
                    PreferenceKeeper.getInstance().image=t.driver_image_url
                    PreferenceKeeper.getInstance().name=t.name
                    PreferenceKeeper.getInstance().accessToken=t.token
                    PreferenceKeeper.getInstance().userPhone=t.phone_number
                    if(t.id != null)
                    PreferenceKeeper.getInstance().userId= t.id.toString()
                    PreferenceKeeper.getInstance().isLogin=true
                    launchActivity(HomeActivity::class.java)
                }
                else if(TextUtils.isEmpty(t!!.phone_number)){
                    val bundle=Bundle()
                    bundle.putSerializable(AppConstant.INTENT_EXTRAS.USER_MODEL,t)
                    launchActivity(SignupActivity::class.java,bundle)
                }else{
                    val bundle=Bundle()
                    bundle.putString(AppConstant.INTENT_EXTRAS.PHONE_NUMBER,t.phone_number)
                    bundle.putString(AppConstant.INTENT_EXTRAS.ACCESS_TOKEN,t.token)
                    launchActivity(OtpVerificationActivity::class.java,bundle)
                    if(t.otp != null)
                    showToast(t.otp.toString())
                }
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                DialogManager.showValidationDialog(this@LoginActivity,error?.errMsg)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGNIN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.signInAccount != null)
            getGoogleAccessToken(result.signInAccount)
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
    }

   /* fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == GOOGLE_SIGNIN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            getGoogleAccessToken(result.signInAccount)
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
            twitterLoginButton.onActivityResult(requestCode, resultCode, data)
        }
    }
*/


    //retreiving token from google account
    @SuppressLint("StaticFieldLeak")
    private fun getGoogleAccessToken(acct: GoogleSignInAccount?) {
        var task: AsyncTask<Void, Void, String>? = null
        task = object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String? {

                var token: String? = null
                try {
                    if (acct != null)
                        token = GoogleAuthUtil.getToken(this@LoginActivity, acct.account, "oauth2:${AppConstant.SCOPES}")
                } catch (transientEx: IOException) {
                    // Network or server error, try later
                    Log.e("google_token", transientEx.toString())
                } catch (e: UserRecoverableAuthException) {
                    // Recover (with e.getIntent())
                    startActivityForResult(e.intent, GOOGLE_SIGNIN)
                    /*Intent recover = e.getIntent();
                        activity.startActivityForResult(recover, GOOGLE_SIGNIN);*/
                } catch (authEx: GoogleAuthException) {
                    // The call is not ever expected to succeed
                    // assuming you have already verified that
                    // Google Play services is installed.
                    Log.e("google_token", authEx.toString())
                }

                return token
            }

            override fun onPostExecute(token: String) {
                Log.i("google_token", "Access token retrieved:$token")
                if (!TextUtils.isEmpty(token))
                   Log.d("",token)
                // googleListener.onGoogleLoginSuccess(token, acct.getId());
            }

        }
        task.execute()
    }

}
