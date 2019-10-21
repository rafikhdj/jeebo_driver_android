package com.app.jeebo.driver.modules.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.BaseResponse
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.model.SignupRequest
import com.app.jeebo.driver.modules.model.UserModel
import com.app.jeebo.driver.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.sql.Timestamp

class SignupActivity : BaseActivity(), IDialogUploadListener {

    private lateinit var mSelectedFile:File
    private var filePath:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init()
    }

    private fun init(){
        tv_sign_up.setOnClickListener {
          /* val bundle=Bundle()
            bundle.putString(AppConstant.INTENT_EXTRAS.PHONE_NUMBER,"+213 "+et_signup_phone.text.toString().trim())
            launchActivity(OtpVerificationActivity::class.java,bundle)*/
            if(checkValidation()){
                callSignupApi()
            }
        }
        iv_profile.setOnClickListener {
            DialogManager.openDialogCameraGallary(false,this,this)
        }
    }

    private fun callSignupApi(){
        showProgressBar(this)
        var signUpRequest=SignupRequest()
        signUpRequest.name=et_signup_name.text.toString().trim()+" "+et_sur_name.text.toString().trim()
        signUpRequest.image_url= filePath.toString()
        signUpRequest.phone_number="+213 "+et_signup_phone.text.toString().trim()
        signUpRequest.email=et_signup_email.text.toString().trim()
        signUpRequest.password=et_signup_password.text.toString().trim()
        val request = ApiClient.getRequest()
        val call=request.signup(signUpRequest)
        call.enqueue(object : ApiCallback<BaseResponse<UserModel>>(){
            override fun onSuccess(t: BaseResponse<UserModel>?) {
                dismissProgressBar()
                var userModel : UserModel=t!!.result
                showToast(userModel.otp.toString())
                //PreferenceKeeper.getInstance().accessToken=userModel.token
                val bundle=Bundle()
                bundle.putString(AppConstant.INTENT_EXTRAS.PHONE_NUMBER,"+213 "+et_signup_phone.text.toString().trim())
                bundle.putString(AppConstant.INTENT_EXTRAS.ACCESS_TOKEN,userModel.token)
                launchActivity(OtpVerificationActivity::class.java,bundle)

            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                DialogManager.showValidationDialog(this@SignupActivity,error?.errMsg)
            }
        })
    }

    override fun onClick(iscamera: Boolean) {
        val mediaState: Int
        if (iscamera)
            mediaState = AppConstant.MEDIA_STATE.CAPTURE_IMAGE
        else
            mediaState = AppConstant.MEDIA_STATE.GALLARY_IMAGE

        GalleryCameraHandlingManager.getInstance().captureMedia(this,iscamera,mediaState, { imagepath, uri ->
            if(imagepath != null){
                mSelectedFile=File(imagepath.toString())
                var size: Long =mSelectedFile.length()/1024
                if(size>0){
                    Glide.with(this@SignupActivity).load(imagepath).apply(RequestOptions.circleCropTransform()).into(iv_profile)
                    try {
                        mSelectedFile = Compressor(this@SignupActivity).compressToFile(mSelectedFile)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    var file: MultipartBody.Part? = null

                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), mSelectedFile)
                    file = MultipartBody.Part.createFormData("file", "file" + Timestamp(System.currentTimeMillis()).toString() + ".jpeg", requestFile)

                    if (file != null)
                        mGetImageURL(file)

                }
            }
        })
    }
    override fun onRemove() {
        mSelectedFile = File("")
        Glide.with(this@SignupActivity).load(R.drawable.user_placeholder).apply(RequestOptions.circleCropTransform()).into(iv_profile)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == AppConstant.REQUEST_CODE.CAMERA_PERMISSION || requestCode == AppConstant.REQUEST_CODE.GALLERY_PERMISSION) {
            GalleryCameraHandlingManager.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppConstant.REQUEST_CODE.CAPTURE_IMAGE || requestCode == AppConstant.REQUEST_CODE.GALLARY_IMAGE) {
            GalleryCameraHandlingManager.getInstance().onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkValidation() : Boolean{
        if(TextUtils.isEmpty(et_signup_name.text.toString().trim())){
            et_signup_name.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.name_missing_error))
            return false
        }else if(TextUtils.isEmpty(et_sur_name.text.toString().trim())){
            et_sur_name.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.surname_missing_error))
            return false
        }else if(TextUtils.isEmpty(et_signup_phone.text.toString().trim())){
            et_signup_phone.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.phone_missing_error))
            return false
        }else if(et_signup_phone.text.toString().trim().length<9){
            et_signup_phone.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.phone_invalid_error))
            return false
        }else if(TextUtils.isEmpty(et_signup_email.text.toString().trim())){
            et_signup_email.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.email_missing_error))
            return false
        }else if(!Validator.isValidEmail(et_signup_email.text.toString().trim())){
            et_signup_email.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.email_invalid_error))
            return false
        }else if(TextUtils.isEmpty(et_signup_password.text.toString().trim()) || et_signup_password.text.toString().trim().length<5){
            et_signup_password.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.password_invalid_error))
            return false
        }else if(TextUtils.isEmpty(et_signup_confirm_password.text.toString().trim()) || et_signup_confirm_password.text.toString().trim().length<5){
            et_signup_password.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.confirm_password_invalid_error))
            return false
        }else if(!et_signup_confirm_password.text.toString().trim().equals(et_signup_confirm_password.text.toString().trim()) ){
            et_signup_password.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.password_mismatch_error))
            return false
        }else
            return true
    }

    private fun mGetImageURL(file: MultipartBody.Part) {
        showProgressBar(this)
        val request = ApiClient.getRequest()
        val call = request.getImageURL(file)
        call.enqueue(object : ApiCallback<UserModel>() {
            override fun onSuccess(t: UserModel?) {
                dismissProgressBar()
                filePath=t?.file_url;
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                DialogManager.showValidationDialog(this@SignupActivity,error?.errMsg)
            }

        })
    }

}
