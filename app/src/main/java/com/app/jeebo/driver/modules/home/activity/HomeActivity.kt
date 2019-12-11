package com.app.jeebo.driver.modules.home.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat

import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.Fragment

import com.app.jeebo.driver.R
import com.app.jeebo.driver.api.ApiCallback
import com.app.jeebo.driver.api.ApiClient
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.enums.EScreenType
import com.app.jeebo.driver.fragment.FragmentFactory
import com.app.jeebo.driver.model.Error
import com.app.jeebo.driver.modules.auth.activity.LoginActivity
import com.app.jeebo.driver.modules.home.model.AcceptOrderResponse
import com.app.jeebo.driver.modules.home.model.ChangeDriverStatusReq
import com.app.jeebo.driver.modules.home.model.UserTokenRequest
import com.app.jeebo.driver.modules.profile.activity.ProfileActivity
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.DialogManager
import com.app.jeebo.driver.utils.MyIntentService
import com.app.jeebo.driver.utils.PreferenceKeeper
import com.app.jeebo.driver.view.CustomTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import java.io.IOException
import java.util.*


class HomeActivity : BaseActivity(), LocationListener {

    var fragment: Fragment? = null
    private val REQUEST_PHONE_CALL = 1000
    private val RECORD_REQUEST_CODE = 101
    private var locationManager: LocationManager? = null
    private var tvTabSelected:CustomTextView?=null
    private var tvTabUnselected:CustomTextView?=null
    private var tvTabUnselected2:CustomTextView?=null
    private var loadFragment:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if(!PreferenceKeeper.getInstance().isLogin){
            PreferenceKeeper.getInstance().clearData()
            finish()
            launchActivity(LoginActivity::class.java)
        }
        init()
    }

    private fun init() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        setUpTabs()
      //  loadFragment= intent.extras?.getString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE)

        setupPermissions()
        slideDrawer()
        setUserToken()
        tv_logout.setOnClickListener {
            showWarningDialog(0)
        }

        switch_driver.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                if(p1)
               changeDriverStatus(1)
                else
                    changeDriverStatus(3)
            }
        })

        tv_stats.setOnClickListener {
            launchActivity(StatsActivity::class.java)
        }

        tv_profile.setOnClickListener {
            var bundle=Bundle()
            if(!TextUtils.isEmpty(tv_current_loc.text.toString()))
            bundle.putString(AppConstant.INTENT_EXTRAS.USER_ADDRESS,tv_current_loc.text.toString())
            launchActivity(ProfileActivity::class.java,bundle)
        }

        tv_order_ends.setOnClickListener {
            launchActivity(CancelledOrdersActivity::class.java)
        }

        tv_contact.setOnClickListener {
            var browserIntent =  Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.CONTACT_URL));
            startActivity(browserIntent);
        }
        /*setUpTabs()
        if(TextUtils.isEmpty(loadFragment) || loadFragment.equals(AppConstant.INTENT_EXTRAS.PENDING))
        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
        else{
            tab_layout.getTabAt(1)?.select()
            setCurrentFragment(EScreenType.ORDERS_PROCESSED_SCREEN.ordinal)
        }*/
        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)

        menu_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                drawerLayout.openDrawer(GravityCompat.START)
                /*if (Build.VERSION.SDK_INT >= 21) {
                    val window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.statusBarColor = resources.getColor(R.color.color_0b95c7)
                }*/

            }
        })

    }

    private fun changeDriverStatus(status:Int){
        showProgressBar(this)
        val request=ApiClient.getRequest()
        var changeDriverStatusReq=ChangeDriverStatusReq()
        changeDriverStatusReq.status=status
        val call=request.changeDriverStatus(changeDriverStatusReq)
        call.enqueue(object:ApiCallback<AcceptOrderResponse>(){
            override fun onSuccess(t: AcceptOrderResponse?) {
                dismissProgressBar()
                PreferenceKeeper.getInstance().driverStatus=status
               /*if(t != null && !TextUtils.isEmpty(t.result))
                   DialogManager.showValidationDialog(this@HomeActivity,t.result)*/
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(this@HomeActivity,error.errMsg)
            }

        })
    }


    public fun setCurrentFragment(fragmentVal: Int) {
        val eScreenType = EScreenType.values()[fragmentVal]
        val currentFragment = FragmentFactory.getInstance().getFragment(eScreenType)
        replaceFragment(R.id.frame_layout, currentFragment, false)
    }


    fun slideDrawer() {
        val actionBarDrawerToggle = object : ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            private val scaleFactor = 100f

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                val slideX = drawerView.width * slideOffset
                content.translationX = slideX
                content.scaleX = 1 - slideOffset / scaleFactor
                content.scaleY = 1 - slideOffset / scaleFactor
            }
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

       // drawerLayout.addDrawerListener()

    }

    override fun onResume() {
        super.onResume()
        /*if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.color_0B94C7)
        }*/
        loadFragment=""
        loadFragment= intent.extras?.getString(AppConstant.INTENT_EXTRAS.FRAGMENT_TYPE,AppConstant.INTENT_EXTRAS.PENDING)

        if(TextUtils.isEmpty(loadFragment) || loadFragment.equals(AppConstant.INTENT_EXTRAS.PENDING)){
            tab_layout.getTabAt(0)?.select()
            //setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
        }else if(loadFragment.equals(AppConstant.INTENT_EXTRAS.COMPLETED)){
            tab_layout.getTabAt(2)?.select()
        }
        else{
            tab_layout.getTabAt(1)?.select()
           // setCurrentFragment(EScreenType.ORDERS_PROCESSED_SCREEN.ordinal)
        }

        setData()
        registerBroadCastReceiver()
    }

    private fun setUpTabs() {
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.pending_orders)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.orders_in_process)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.terminated_orders)))
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL)
        tab_layout.setClickable(true)

        tvTabSelected = LayoutInflater.from(this).inflate(R.layout.tab_layout_selected, null) as CustomTextView
        tvTabUnselected = LayoutInflater.from(this).inflate(R.layout.tab_layout_unselected, null) as CustomTextView
        tvTabUnselected2 = LayoutInflater.from(this).inflate(R.layout.tab_unselected, null) as CustomTextView
        tab_layout.getTabAt(0)?.setCustomView(tvTabSelected)
        tab_layout.getTabAt(1)?.setCustomView(tvTabUnselected)
        tab_layout.getTabAt(2)?.setCustomView(tvTabUnselected2)

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(tab: TabLayout.Tab?) {
               // tab?.setCustomView(tvTabUnselected)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
               // tab?.setCustomView(tvTabSelected)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.setCustomView(tvTabSelected)
                if(tab_layout.selectedTabPosition==0){
                    tab_layout.getTabAt(1)?.setCustomView(tvTabUnselected)
                    tab_layout.getTabAt(2)?.setCustomView(tvTabUnselected2)
                    setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
                }else if(tab_layout.selectedTabPosition==1){
                    tab_layout.getTabAt(0)?.setCustomView(tvTabUnselected)
                    tab_layout.getTabAt(2)?.setCustomView(tvTabUnselected2)
                    setCurrentFragment(EScreenType.ORDERS_PROCESSED_SCREEN.ordinal)
                }else{
                    tab_layout.getTabAt(1)?.setCustomView(tvTabUnselected2)
                    tab_layout.getTabAt(0)?.setCustomView(tvTabUnselected)
                    setCurrentFragment(EScreenType.TERMINATED_ORDERS_SCREEN.ordinal)
                }

            }

        })


        /*tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener() {
            fun onTabSelected(tab: TabLayout.Tab) {
                tab.setCustomView(tvTabSelected)
                if (tab_layout.getSelectedTabPosition() === 0) {
                    setCurrentFragment(EScreenType.FRIEND_LIST_SCREEN.ordinal())
                } else {
                    setCurrentFragment(EScreenType.FRIEND_REQUEST_SCREEN.ordinal())
                }

            }

            fun onTabUnselected(tab: TabLayout.Tab) {
                tab.setCustomView(tvTabUnselected)
                if (tab_layout.getSelectedTabPosition() === 0) {
                    setCurrentFragment(EScreenType.FRIEND_LIST_SCREEN.ordinal())
                } else {
                    setCurrentFragment(EScreenType.FRIEND_REQUEST_SCREEN.ordinal())
                }
                // setCurrentFragment(EScreenType.FRIEND_REQUEST_SCREEN.ordinal());
            }

            fun onTabReselected(tab: TabLayout.Tab) {
                tab.setCustomView(tvTabSelected)
            }
        })*/

    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }else{
            askGps()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                RECORD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast(getString(R.string.location_permission_alert))
                    //Log.i("LOCATION_PERMISSION", "Permission has been denied by user")
                } else {
                    //Log.i("LOCATION_PERMISSION", "Permission has been granted by user")
                    if(isGpsEnabled()){
                        try{
                            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                                locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5.0f, this)
                            }else{
                                ActivityCompat.requestPermissions(this,
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                        RECORD_REQUEST_CODE)
                            }
                        }catch (e: Exception){

                        }
                    }else{
                        /*if(isGpsEnabled()){
                            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5.0f, this)
                        }else{
                            AlertDialog.Builder(this)
                                    .setTitle(getString(R.string.app_name))
                                    .setMessage(getString(R.string.enable_gps))
                                    .setPositiveButton(R.string.ok, { dialogInterface, i ->
                                        //Prompt the user once explanation has been shown
                                        val gpsOptionsIntent = Intent(
                                                Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                        startActivity(gpsOptionsIntent)
                                    })
                                    .create()
                                    .show()
                        }*/
                        askGps()

                    }
                }
            }
            REQUEST_PHONE_CALL->{
            FragmentFactory.getInstance().currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        }
    }

    @SuppressLint("MissingPermission")
    private fun askGps(){
        if(isGpsEnabled()){
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5.0f, this)
        }else{
            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.enable_gps))
                    .setPositiveButton(R.string.ok, { dialogInterface, i ->
                        //Prompt the user once explanation has been shown
                        val gpsOptionsIntent = Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(gpsOptionsIntent)
                    })
                    .create()
                    .show()
        }
    }

    private fun isGpsEnabled():Boolean{
        if(locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
            return true
        else
            return false
    }
    override fun onLocationChanged(location: Location?) {
        val geocoder = Geocoder(this, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(location!!.getLatitude(), location.getLongitude(), 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var userAddress = ""
        if (addresses != null) {

            userAddress = addresses[0].getAddressLine(0)
        }
        if(!TextUtils.isEmpty(userAddress))
            tv_current_loc.setText(userAddress)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }


    private fun setData(){
        var requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.placeholder)
        requestOptions.error(R.drawable.placeholder)
        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().image)){
            Glide.with(this@HomeActivity).setDefaultRequestOptions(requestOptions).load(PreferenceKeeper.getInstance().image).into(iv_user)
        }

        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().name)){
            tv_user_name.setText(PreferenceKeeper.getInstance().name)
        }
        if(PreferenceKeeper.getInstance().driverStatus==1)
        switch_driver.isChecked=true

    }

    override fun onBackPressed() {
       // super.onBackPressed()

        if(tab_layout.selectedTabPosition!=0){
            setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
            tab_layout.getTabAt(0)?.select()

        }

        else{
            showWarningDialog(1)
        }
    }
    private fun registerBroadCastReceiver() {
        val intentFilter1 = IntentFilter(MyIntentService.CUSTOM_ACTION)

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
               // setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
              //  tab_layout.getTabAt(0)?.select()
                if(intent.hasExtra(AppConstant.INTENT_EXTRAS.NOTIFICATION_TYPE))
                showCustomToast(intent.getStringExtra(AppConstant.INTENT_EXTRAS.NOTIFICATION_TYPE))
                var type=intent.getStringExtra("type")
                if(tab_layout != null){
                    if(tab_layout.selectedTabPosition==0 && !TextUtils.isEmpty(type) && type.equals("order placed"))
                        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
                    else if(tab_layout.selectedTabPosition==1 && !TextUtils.isEmpty(type) && type.equals("order cancel"))
                        setCurrentFragment(EScreenType.ORDERS_PROCESSED_SCREEN.ordinal)
                    else{

                    }
                    /*if(tab_layout.selectedTabPosition==0 )
                        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
                    else if(tab_layout.selectedTabPosition==1 )
                        setCurrentFragment(EScreenType.ORDERS_PROCESSED_SCREEN.ordinal)
                    else{

                    }*/
                }
            }
        }
        registerReceiver(broadcastReceiver, intentFilter1)
    }

    private fun showWarningDialog(type:Int){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.inflater_dialog)
        if (!dialog.isShowing)
            dialog.show()

        val tvMsg: CustomTextView = dialog.findViewById<View>(R.id.tv_message) as CustomTextView
        val tvOk = dialog.findViewById<View>(R.id.tv_ok)
        val tvCancel = dialog.findViewById<View>(R.id.tv_cancel)
        tvCancel.visibility = View.VISIBLE
        if(type==0)
        tvMsg.setText(getString(R.string.logout_warning))
        else
            tvMsg.setText(getString(R.string.exit_warning))
        tvOk.setOnClickListener{view->
            run {
                dialog.dismiss()
                if(type==0){
                    logoutUser()
                }else{
                    finish()
                }

            }
        }
        tvCancel.setOnClickListener { view -> dialog.dismiss() }
    }

    private fun setUserToken(){
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
            if (task.isSuccessful){
                PreferenceKeeper.getInstance().deviceToken = task.result!!.token
                val request=ApiClient.getRequest()
                var userTokenReq=UserTokenRequest()
                userTokenReq.device_token=task.result?.token
                val call=request.setUserToken(userTokenReq)
                call.enqueue(object:ApiCallback<AcceptOrderResponse>(){
                    override fun onSuccess(t: AcceptOrderResponse?) {
                        Log.d("DEVICE_TOKEN",PreferenceKeeper.getInstance().deviceToken)
                    }

                    override fun onError(error: Error?) {
                        if(error != null && !TextUtils.isEmpty(error.errMsg))
                            Log.d("DEVICE_TOKEN_ERROR",error.errMsg)
                    }
                })
            }
        }
    }

    private fun logoutUser(){
        showProgressBar(this@HomeActivity)
        var userTokenReq=UserTokenRequest()
        userTokenReq.device_token=PreferenceKeeper.getInstance().deviceToken
        val req=ApiClient.getRequest()
        val call=req.logout(userTokenReq)
        call.enqueue(object : ApiCallback<AcceptOrderResponse>(){
            override fun onSuccess(t: AcceptOrderResponse?) {
                PreferenceKeeper.getInstance().clearData()
                finish()
                launchActivity(LoginActivity::class.java)
                dismissProgressBar()
            }

            override fun onError(error: Error?) {
                dismissProgressBar()
                if(error != null && !TextUtils.isEmpty(error.errMsg))
                    DialogManager.showValidationDialog(this@HomeActivity,error.errMsg)
            }

        })

    }

}


