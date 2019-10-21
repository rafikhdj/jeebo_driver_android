package com.app.jeebo.driver.modules.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.app.jeebo.driver.R
import com.app.jeebo.driver.utils.AppUtils

class SplashActivity : Activity() {

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setHandler()
    }

    private fun setHandler() {
        handler = Handler()
        runnable = Runnable {
            if (AppUtils.isNetworkAvailable(this@SplashActivity)) {
                var intent: Intent? = null
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        handler!!.postDelayed(runnable, 3000)
    }
}
