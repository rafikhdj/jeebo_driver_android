package com.app.jeebo.driver.modules.auth.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.app.jeebo.driver.R
import com.app.jeebo.driver.modules.home.activity.HomeActivity
import com.app.jeebo.driver.modules.profile.activity.ProfileActivity
import com.app.jeebo.driver.utils.PreferenceKeeper

class SplashActivity : Activity() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setHandler()
        //AppUtils.getKey(this)
    }

    private fun setHandler() {
        handler = Handler()
        runnable = Runnable {
                var intent: Intent? = null
            if(PreferenceKeeper.getInstance().isLogin){
                intent = Intent(this, HomeActivity::class.java)
            }else{
                intent = Intent(this, LoginActivity::class.java)
            }
                startActivity(intent)
                finish()

        }
        handler!!.postDelayed(runnable, 3000)
    }
}
