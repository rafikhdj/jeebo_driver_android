package com.app.modules.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.app.R
import com.app.base.BaseActivity
import com.app.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.io.File
import java.io.IOException

class SignupActivity : BaseActivity(), IDialogUploadListener {

    private lateinit var mSelectedFile:File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init()
    }

    private fun init(){
        tv_sign_up.setOnClickListener {
            launchActivity(OtpVerificationActivity::class.java)
        }
        iv_profile.setOnClickListener {
            DialogManager.openDialogCameraGallary(false,this,this)
        }
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
}
