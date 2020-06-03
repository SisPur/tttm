package com.example.orata.ui.Splash

import com.example.orata.R
import com.example.orata.ui.Home.HomeActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.orata.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        val intent = Intent(this, MainActivity::class.java)
        handler.postDelayed({
            startActivity(intent)
            finish()
        }, 2000L)
    }


}
