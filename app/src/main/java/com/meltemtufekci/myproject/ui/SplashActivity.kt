package com.meltemtufekci.myproject.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.meltemtufekci.myproject.R
import com.meltemtufekci.myproject.utils.UiHelper

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (UiHelper.hasInternetConnection(this)) {
            loadSplashScreen()
        } else {
            Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSplashScreen() {
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }
}