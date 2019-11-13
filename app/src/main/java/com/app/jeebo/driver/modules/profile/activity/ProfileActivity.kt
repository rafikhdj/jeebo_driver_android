package com.app.jeebo.driver.modules.profile.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.auth.activity.OtpVerificationActivity
import com.app.jeebo.driver.modules.auth.model.UserModel
import com.app.jeebo.driver.modules.profile.model.EditProfileReq
import com.app.jeebo.driver.modules.profile.model.UserResultModel
import com.app.jeebo.driver.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.et_sur_name
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.sql.Timestamp

class ProfileActivity : BaseActivity(), IDialogUploadListener {
    private lateinit var mSelectedFile:File
    private var filePath:String?=null

    private var isEditProfile: Boolean? =false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setUserDetails()
        init()
    }

    private fun init(){

        var address= intent.extras?.getString(AppConstant.INTENT_EXTRAS.USER_ADDRESS)
        if(!TextUtils.isEmpty(address))
            tv_address.setText(address)

        iv_user.setOnClickListener {
            if(isEditProfile!!)
            DialogManager.openDialogCameraGallary(false,this,this)
        }

        iv_back.setOnClickListener { finish() }

        tv_edit.setOnClickListener {
            if(isEditProfile!!){
                isEditProfile=false
                iv_cam.visibility=View.GONE
                et_name.visibility=View.GONE
                et_sur_name.visibility=View.GONE
                et_email.visibility=View.GONE
                et_phone.visibility=View.GONE

                tv_name_value.visibility=View.VISIBLE
                tv_surname_value.visibility=View.VISIBLE
                tv_phone_value.visibility=View.VISIBLE
                tv_email_value.visibility=View.VISIBLE
                callEditProfileApi()

            }else{
                isEditProfile=true
                iv_cam.visibility=View.VISIBLE
                et_name.visibility=View.VISIBLE
                et_sur_name.visibility=View.VISIBLE
                et_email.visibility=View.VISIBLE
                et_phone.visibility=View.VISIBLE

                tv_name_value.visibility=View.GONE
                tv_surname_value.visibility=View.GONE
                tv_phone_value.visibility=View.GONE
                tv_email_value.visibility=View.GONE
                tv_edit.text=getString(R.string.save)
            }
        }
    }

    private fun callEditProfileApi(){
        if(validate()){
            showProgressBar(this)
            var editProfileReq=EditProfileReq()
            editProfileReq.email=et_email.text.toString().trim()
            if(et_phone.text.toString().trim().contains("+213"))
                editProfileReq.phone=et_phone.text.toString().trim()
            else
                editProfileReq.phone="+213 "+et_phone.text.toString().trim()

            editProfileReq.name=et_name.text.toString().trim()+" "+et_sur_name.text.toString().trim()
            if(!TextUtils.isEmpty(filePath))
            editProfileReq.image_url=filePath
            val request=ApiClient.getRequest()
            val call=request.editProfile(editProfileReq)
            call.enqueue(object : ApiCallback<UserResultModel>(){
                override fun onSuccess(userResult: UserResultModel?) {
                    var t=userResult?.result
                    PreferenceKeeper.getInstance().email=t?.email
                    PreferenceKeeper.getInstance().image=t?.driver_image_url
                    PreferenceKeeper.getInstance().name=t?.name
                    PreferenceKeeper.getInstance().accessToken=t?.token
                    dismissProgressBar()
                    if(t!!.isPhoneVerified){
                        showToast(getString(R.string.profile_updated_successfully))
                        finish()
                    }else{
                        showToast(t.otp.toString())
                        val bundle=Bundle()
                        bundle.putString(AppConstant.INTENT_EXTRAS.PHONE_NUMBER,"+213 "+et_phone.text.toString().trim())
                        bundle.putString(AppConstant.INTENT_EXTRAS.CAME_FROM,"PROFILE")
                        bundle.putString(AppConstant.INTENT_EXTRAS.ACCESS_TOKEN,t.token)
                        launchActivity(OtpVerificationActivity::class.java,bundle)
                        finish()
                    }

                }

                override fun onError(error: Error?) {
                    dismissProgressBar()
                    tv_edit.setText(getString(R.string.edit_profile))
                }

            })

        }
    }

    private fun validate():Boolean{
        if(TextUtils.isEmpty(et_name.text.toString().trim())){
            et_name.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.name_missing_error))
            return false
        }else if(TextUtils.isEmpty(et_sur_name.text.toString().trim())){
            et_sur_name.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.surname_missing_error))
            return false
        }else if(TextUtils.isEmpty(et_email.text.toString().trim())){
            et_email.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.email_missing_error))
            return false
        }else if(!Validator.isValidEmail(et_email.text.toString().trim())){
            et_email.requestFocus()
            DialogManager.showValidationDialog(this,getString(R.string.email_invalid_error))
            return false
        }else
            return true
    }

    override fun onClick(iscamera: Boolean) {
        val mediaState: Int
        if (iscamera)
            mediaState = AppConstant.MEDIA_STATE.CAPTURE_IMAGE
        else
            mediaState = AppConstant.MEDIA_STATE.GALLARY_IMAGE

        GalleryCameraHandlingManager.getInstance().captureMedia(this,iscamera,mediaState, { imagepath, uri ->
            if(imagepath != null){
                mSelectedFile= File(imagepath.toString())
                var size: Long =mSelectedFile.length()/1024
                if(size>0){
                    /*var requestOptions = RequestOptions()
                    requestOptions.placeholder(R.drawable.placeholder)
                    requestOptions.error(R.drawable.placeholder)*/
                    Glide.with(this@ProfileActivity).load(imagepath).apply(RequestOptions.circleCropTransform()).into(iv_user)
                    try {
                        mSelectedFile = Compressor(this@ProfileActivity).compressToFile(mSelectedFile)
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
                DialogManager.showValidationDialog(this@ProfileActivity,error?.errMsg)
            }

        })
    }

    override fun onRemove() {
        mSelectedFile = File("")
        Glide.with(this@ProfileActivity).load(R.drawable.user_placeholder).apply(RequestOptions.circleCropTransform()).into(iv_profile)
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

    private fun setUserDetails(){
        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().image)){
            var requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.placeholder)
            requestOptions.error(R.drawable.placeholder)
            Glide.with(this@ProfileActivity).setDefaultRequestOptions(requestOptions).load(PreferenceKeeper.getInstance().image).into(iv_user)
        }

        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().name)){
            tv_name_head.setText(PreferenceKeeper.getInstance().name)
            if(PreferenceKeeper.getInstance().name.contains(" ")){
                var name=PreferenceKeeper.getInstance().name.split(" ")
                tv_name_value.setText(name[0])
                et_name.setText(name[0])
                tv_surname_value.setText(name[1])
                et_sur_name.setText(name[1])
            }else{
                tv_name_value.setText(PreferenceKeeper.getInstance().name)
                et_name.setText(PreferenceKeeper.getInstance().name)
            }

        }

        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().email)){
            tv_email_value.setText(PreferenceKeeper.getInstance().email)
            et_email.setText(PreferenceKeeper.getInstance().email)
        }

        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().userPhone)){
            tv_phone_value.setText(PreferenceKeeper.getInstance().userPhone)
            et_phone.setText(PreferenceKeeper.getInstance().userPhone)
        }

    }
}
