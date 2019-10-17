package com.app.modules.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.app.R
import com.app.utils.AppUtils

class SplashActivity : AppCompatActivity() {

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
            }
        }
        handler!!.postDelayed(runnable, 3000)
    }
}
