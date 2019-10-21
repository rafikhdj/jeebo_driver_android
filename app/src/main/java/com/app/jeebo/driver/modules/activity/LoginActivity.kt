package com.app.jeebo.driver.modules.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.model.LoginRequest
import com.app.jeebo.driver.modules.model.UserModel
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.Validator
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.Credentials

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init(){
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
                loginRequest.setPlatform(2)
                loginRequest.setDevice_token("sfsfsfsf")
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
                           showToast("succes")
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
}
