package com.app.jeebo.driver.modules.home.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_home.*
import android.R.attr.fragment


import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.FrameLayout
import androidx.fragment.app.Fragment

import android.R.attr.fragment


import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T

import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.app.jeebo.driver.R
import com.app.jeebo.driver.base.BaseActivity
import com.app.jeebo.driver.enums.EScreenType
import com.app.jeebo.driver.fragment.FragmentFactory


class HomeActivity : BaseActivity() {
    var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        slideDrawer()

        inIt()

    }

    private fun inIt() {
        setCurrentFragment(EScreenType.PENDING_ORDERS_SCREEN.ordinal)

        menu_icon.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                drawerLayout.openDrawer(GravityCompat.START)
                if (Build.VERSION.SDK_INT >= 21) {
                    val window = window
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.statusBarColor = resources.getColor(com.app.jeebo.driver.R.color.color_0095a4)
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
            }
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)

    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= 21) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = resources.getColor(R.color.color_85c441)
        }

    }

}


