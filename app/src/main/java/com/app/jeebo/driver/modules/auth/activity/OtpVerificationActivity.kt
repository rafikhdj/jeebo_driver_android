package com.app.jeebo.driver.modules.auth.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.api.IApiRequest
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.home.activity.HomeActivity
import com.app.jeebo.driver.modules.auth.model.ResendOtpRequest
import com.app.jeebo.driver.modules.auth.model.ResultModel
import com.app.jeebo.driver.modules.auth.model.VerifyOtpResponse
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.OtpTextWatcher
import com.app.jeebo.driver.utils.PreferenceKeeper
import com.app.jeebo.driver.view.CustomEditText
import kotlinx.android.synthetic.main.activity_otp_verification.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : BaseActivity() {

    private var isTimerEnded:Boolean=false;
    private var isVerifyEnabled:Boolean=false;
    private var phoneNum:String?=null;
    private var cameFrom:String?=null;
    private var token:String?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        init()
        startTimer()
    }

    private fun init(){

        phoneNum=intent.extras?.getString(AppConstant.INTENT_EXTRAS.PHONE_NUMBER)
        token=intent.extras?.getString(AppConstant.INTENT_EXTRAS.ACCESS_TOKEN)
        cameFrom=intent.extras?.getString(AppConstant.INTENT_EXTRAS.CAME_FROM)

        tv_resend_otp.setTextColor(resources.getColor(R.color.color_50292929))

        iv_back.setOnClickListener { finish() }

        tv_verify.setOnClickListener {
            if(isVerifyEnabled){
                verifyOtp()
            }
        }

        tv_resend_otp.setOnClickListener {
            if(isTimerEnded){
                resendOtp()
            }
           // resendOtp()
        }

        edt_otp1.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enableDisableVerify(p0.toString())
            }

        })
        edt_otp2.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enableDisableVerify(p0.toString())
            }

        })
        edt_otp3.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enableDisableVerify(p0.toString())
            }

        })
        edt_otp4.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enableDisableVerify(p0.toString())
            }

        })

        val myList = ArrayList<CustomEditText>()

        myList.add(0, edt_otp1)
        myList.add(1, edt_otp2)
        myList.add(2, edt_otp3)
        myList.add(3, edt_otp4)

        //adding text changed listeners..
        edt_otp1.addTextChangedListener(OtpTextWatcher(edt_otp1, myList))
        edt_otp2.addTextChangedListener(OtpTextWatcher(edt_otp2, myList))
        edt_otp3.addTextChangedListener(OtpTextWatcher(edt_otp3, myList))
        edt_otp4.addTextChangedListener(OtpTextWatcher(edt_otp4, myList))

        /*edtOtp4.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                    if(keyCode == KeyEvent.KEYCODE_DEL) {
                        //this is for backspace
                    }
                    return false;
                }
            });*/

        edt_otp4.setOnKeyListener({ view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                if (edt_otp4.text.toString().trim().length == 0) {
                    if (edt_otp3.text.toString().trim().length > 0) {
                        edt_otp3.requestFocus()
                    } else if (edt_otp2.text.toString().trim().length > 0) {
                        edt_otp2.requestFocus()
                    } else {
                        edt_otp1.requestFocus()
                    }
                }

            }
            false
        })
        edt_otp3.setOnKeyListener({ view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                if (edt_otp3.text.toString().trim().length == 0) {
                    if (edt_otp2.text.toString().trim().length > 0) {
                        edt_otp2.requestFocus()
                    } else {
                        edt_otp1.requestFocus()
                    }
                }

            }
            false
        })
        edt_otp2.setOnKeyListener({ view, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_DEL) {
                if (edt_otp2.text.toString().trim().length == 0) {

                    edt_otp1.requestFocus()

                }

            }
            false
        })
    }

    private fun verifyOtp(){
        showProgressBar(this)
        var otp:String = edt_otp1.text.toString().trim()+edt_otp2.text.toString().trim()+edt_otp3.text.toString().trim()+edt_otp4.text.toString().trim()
        val request=ApiClient.getRequest()
        var verifyReq=ResendOtpRequest()
        verifyReq.otp=otp
        verifyReq.phone_number=phoneNum
        val call=request.verifyOtp(verifyReq)
        call.enqueue(object : ApiCallback<VerifyOtpResponse>(){
            override fun onSuccess(t: VerifyOtpResponse?) {
                dismissProgressBar()
                var userModel= t?.result
                //showToast("Success")
                PreferenceKeeper.getInstance().email=userModel?.email
                PreferenceKeeper.getInstance().image=userModel?.driver_image_url
                PreferenceKeeper.getInstance().name=userModel?.name
                PreferenceKeeper.getInstance().accessToken=userModel?.token
                PreferenceKeeper.getInstance().userPhone=userModel?.phone_number
                PreferenceKeeper.getInstance().userId= userModel?.userId.toString()
                if(!TextUtils.isEmpty(cameFrom) && cameFrom.equals("PROFILE"))
                    showToast(getString(R.string.profile_updated_successfully))
                else{
                    PreferenceKeeper.getInstance().isLogin=true
                    launchActivity(HomeActivity::class.java)

                }
                finish()
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                DialogManager.showValidationDialog(this@OtpVerificationActivity,error?.errMsg)
            }


        })
    }


    /*override fun onSuccess(t: ResultModel?) {
        dismissProgressBar()
        showToast("Success")
    }

    override fun onError(error: Error?) {
        dismissProgressBar()
        DialogManager.showValidationDialog(this@OtpVerificationActivity,error?.errMsg)
    }*/

    private fun resendOtp(){
        showProgressBar(this)
        val request:IApiRequest=ApiClient.getRequest();
        var resendOtpReq=ResendOtpRequest()
        resendOtpReq.phone_number=phoneNum
        val call= request.resendOtp(resendOtpReq)
        call.enqueue(object : ApiCallback<ResultModel>(){
            override fun onSuccess(t: ResultModel?) {
                dismissProgressBar()
                DialogManager.showValidationDialog(this@OtpVerificationActivity,t?.result)
                startTimer()
                tv_resend_otp.setTextColor(resources.getColor(R.color.color_50292929))
                isTimerEnded=false
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                DialogManager.showValidationDialog(this@OtpVerificationActivity,error?.errMsg)
            }

        })

    }

    private fun startTimer() {
        isTimerEnded = false
        object : CountDownTimer((60 * 1000).toLong(), 1000) { // adjust the milli seconds here

            override fun onTick(millisUntilFinished: Long) {
                tv_timer.setText("" + String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))))
            }

            override fun onFinish() {
                tv_timer.setText("00:00")
                tv_resend_otp.setTextColor(resources.getColor(R.color.white))
                isTimerEnded = true

            }
        }.start()
    }

    private fun enableDisableVerify(s:String){
        if (!TextUtils.isEmpty(edt_otp1.getText().toString().trim()) && !TextUtils.isEmpty(edt_otp2.getText().toString().trim()) &&
                !TextUtils.isEmpty(edt_otp3.getText().toString().trim()) && !TextUtils.isEmpty(edt_otp4.getText().toString().trim())) {
            if (s.length >= 1) {
                isVerifyEnabled = true
                /*val font = Typeface.createFromAsset(assets, "GlacialIndifference-Bold.otf")
                tvVerify.setTypeface(font)
                tvVerify.setBackground(resources.getDrawable(R.drawable.bg_button))
                tvVerify.setTextColor(resources.getColor(R.color.color_white))*/
            } else {
                isVerifyEnabled = false
                /*val font = Typeface.createFromAsset(assets, "GlacialIndifference-Regular.otf")
                tvVerify.setTypeface(font)
                tvVerify.setBackground(resources.getDrawable(R.drawable.bg_button_unfill))
                tvVerify.setTextColor(resources.getColor(R.color.color_f26222))*/
            }
        } else {
            isVerifyEnabled = false
            /*tvVerify.setBackground(resources.getDrawable(R.drawable.bg_button_unfill))
            tvVerify.setTextColor(resources.getColor(R.color.color_f26222))
            val font = Typeface.createFromAsset(assets, "GlacialIndifference-Regular.otf")
            tvVerify.setTypeface(font)*/
        }
    }
}

