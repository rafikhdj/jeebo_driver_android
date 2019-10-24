package com.app.jeebo.driver.modules.home.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat

import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.Fragment

import com.app.jeebo.driver.R
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.enums.EScreenType
import com.app.jeebo.driver.fragment.FragmentFactory
import com.app.jeebo.driver.modules.auth.activity.LoginActivity
import com.app.jeebo.driver.modules.profile.activity.ProfileActivity
import com.app.jeebo.driver.utils.AppConstant
import com.app.jeebo.driver.utils.PreferenceKeeper
import com.bumptech.glide.Glide
import java.io.IOException
import java.util.*


class HomeActivity : BaseActivity(), LocationListener {

    var fragment: Fragment? = null
    private val RECORD_REQUEST_CODE = 101
    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        init()
    }

    private fun init() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        slideDrawer()

        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().image)){
            Glide.with(this@HomeActivity).load(PreferenceKeeper.getInstance().image).into(iv_user)
        }

        if(!TextUtils.isEmpty(PreferenceKeeper.getInstance().name)){
            tv_user_name.setText(PreferenceKeeper.getInstance().name)
        }

        tv_logout.setOnClickListener {
            PreferenceKeeper.getInstance().clearData()
            finish()
            launchActivity(LoginActivity::class.java)
        }

        tv_profile.setOnClickListener {
            var bundle=Bundle()
            if(!TextUtils.isEmpty(tv_current_loc.text.toString()))
            bundle.putString(AppConstant.INTENT_EXTRAS.USER_ADDRESS,tv_current_loc.text.toString())
            launchActivity(ProfileActivity::class.java,bundle)
        }

        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
        menu_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                drawerLayout.openDrawer(GravityCompat.START)
                if (Build.VERSION.SDK_INT >= 21) {
                    val window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.statusBarColor = resources.getColor(R.color.color_0b95c7)
                }

            }
        })


        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
               if(tabLayout.selectedTabPosition==0){
                   setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)
               }else if(tabLayout.selectedTabPosition==1){
                   setCurrentFragment(EScreenType.ORDERS_PROCESSED_SCREEN.ordinal)
               }else{
                   setCurrentFragment(EScreenType.TERMINATED_ORDERS_SCREEN.ordinal)
               }

            }

        })
    }


    fun setCurrentFragment(fragmentVal: Int) {
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
                if(slideOffset > 0){
                    if (Build.VERSION.SDK_INT >= 21) {
                        val window = window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        window.statusBarColor = resources.getColor(R.color.color_0b95c7)
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= 21) {
                        val window = window
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                        window.statusBarColor = resources.getColor(com.app.jeebo.driver.R.color.color_0B94C7)
                    }
                }
            }
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

       // drawerLayout.addDrawerListener()

    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.color_0B94C7)
        }
        setupPermissions()
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

}


