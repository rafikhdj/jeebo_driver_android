package com.app.modules.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.app.R
import com.app.base.BaseActivity
import com.app.utils.DialogManager
import com.app.utils.Validator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init(){
        tv_signup.setOnClickListener({
            launchActivity(SignupActivity::class.java)
        })
        tv_forgot_password.setOnClickListener {
            launchActivity(ForgotPasswordActivity::class.java)
        }
        tv_login.setOnClickListener {
            if(checkValidation()){
                showToast("Login")
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
