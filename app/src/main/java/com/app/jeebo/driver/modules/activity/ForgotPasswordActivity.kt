package com.app.jeebo.driver.modules.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.model.ForgotPassRequest
import com.app.jeebo.driver.modules.model.ResultModel
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.Validator
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        init()
    }

    private fun init(){
        tv_send.setOnClickListener {
            if(TextUtils.isEmpty(et_email.text.toString().trim())){
                DialogManager.showValidationDialog(this,getString(R.string.email_missing_error))
            }else if(!Validator.isValidEmail(et_email.text.toString().trim())){
                DialogManager.showValidationDialog(this,getString(R.string.email_invalid_error))
            }else{
                showProgressBar(this)
                var forgotPassRequest=ForgotPassRequest()
                forgotPassRequest.email=et_email.text.toString().trim()
                val request=ApiClient.getRequest()
                val call=request.forgotPassword(forgotPassRequest)
                call.enqueue(object : ApiCallback<ResultModel>(){
                    override fun onSuccess(t: ResultModel?) {
                        dismissProgressBar()
                        ll_send_mail.visibility = View.GONE
                        iv_back.visibility = View.GONE
                        ll_sent.visibility = View.VISIBLE
                    }

                    override fun onError(error: Error?) {
                        dismissProgressBar()
                        DialogManager.showValidationDialog(this@ForgotPasswordActivity,error?.errMsg)
                    }

                })
            }
        }
    }
}
