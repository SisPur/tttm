package com.example.orata

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.orata.ui.Home.HomeActivity
import com.example.orata.utils.auth.AuthSnapshot
import com.example.orata.utils.auth.AuthTableHelper

class MainActivity : AppCompatActivity() {

    private var authHelper: AuthTableHelper? = null
    private var authSnapshot: AuthSnapshot? = null
    private val DELAY_SPLASHSCREEN = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_bawaan)

        authHelper = AuthTableHelper(this, null)

        checkStatusLogin()
    }

    private fun checkStatusLogin() {
        var username = ""
        var token = ""
        var level = ""
        var address = ""
        var phone = ""
        var user_id = ""
        var email = ""
        Log.d("tes", "auth : $authHelper")
        var checkViaAPI = true
        if (authHelper != null) {
            if (authHelper?.getAuth() == null) {
                Log.d("tes", "inserting default user..")
                authHelper?.save()
            }
            Log.d("tes", "get auth : " + authHelper?.getAuth())
            var data : AuthSnapshot? = authHelper?.getAuth()
            if (data != null) {
                token = authHelper?.getAuth()!!.getToken()
                username = authHelper?.getAuth()!!.getName()
                level = authHelper?.getAuth()!!.getLevel()
                user_id = "" + authHelper?.getAuth()!!.getId()
                address = authHelper?.getAuth()!!.getAddress()
                phone = authHelper?.getAuth()!!.getPhone()
                email = authHelper?.getAuth()!!.getEmail()
                Log.d(
                    "tes",
                    authHelper?.getAuth()!!.getId()
                        .toString() + " checkStatusLogin -------> " + authHelper?.getAuth()!!
                        .getToken() + " email " + authHelper?.getAuth()!!.getEmail()
                )
                if (!TextUtils.isEmpty(authHelper?.getAuth()!!.getToken())) {
                    if (!authHelper?.getAuth()!!.getToken().equals("-")) {
                        checkViaAPI = false
                    }
                }
            }
            else {
                Log.d("tes","No user detected");
            }
        }

        if (!checkViaAPI) {
            gotoHomePage(username, token, level, user_id, address, phone, email)
        } else {
            gotoLoginPage()
        }
    }

    private fun gotoLoginPage() {
        Handler().postDelayed({
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }, DELAY_SPLASHSCREEN.toLong())
    }

    private fun gotoHomePage(
        email: String,
        token: String,
        level_: String,
        user_id_: String,
        address_: String,
        phone_: String,
        email_: String
    ) {
        Handler().postDelayed({
            if (level_ == "9") {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                intent.putExtra("username", email)
                intent.putExtra("token", token)
                intent.putExtra("level", level_)
                intent.putExtra("address", address_)
                intent.putExtra("phone", phone_)
                intent.putExtra("email", email_)
                intent.putExtra("user_id", user_id_)
                startActivity(intent)
            }
            finish()
        }, DELAY_SPLASHSCREEN.toLong())
    }

    override fun onResume() {
        super.onResume()
        authHelper = AuthTableHelper(this, null)
    }
}
