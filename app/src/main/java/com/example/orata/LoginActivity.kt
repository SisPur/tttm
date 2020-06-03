package com.example.orata

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.example.orata.ui.Home.HomeActivity
import com.example.orata.utils.auth.AuthSnapshot
import com.example.orata.utils.auth.AuthTableHelper
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private val REQUEST_SIGNUP = 0

    private var token_access = ""
    private  var address:String = ""
    private  var username:String = ""
    private  var _email:String = ""
    private  var user_id:String = ""
    private  var fullname:String = ""
    private  var level:String = ""
    private  var mobile_no:String = ""

    //@BindView(R.id.input_ip_address)
    lateinit var _ipAddressText: EditText

    //@BindView(R.id.input_email)
    lateinit var _usernameText: EditText

    //@BindView(R.id.input_password)
    lateinit var _passwordText: EditText

    //@BindView(R.id.btn_login)
    lateinit var _loginButton: Button

    //@BindView(R.id.link_signup)
    lateinit var _signupLink: TextView

    private var authHelper: AuthTableHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //ButterKnife.bind(this)

        _ipAddressText = findViewById(R.id.input_ip_address)
        _usernameText = findViewById(R.id.input_email)
        _passwordText = findViewById(R.id.input_password)
        _loginButton = findViewById(R.id.btn_login)
        _signupLink = findViewById(R.id.link_signup)

        if (Build.VERSION.SDK_INT > 9) {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        authHelper = AuthTableHelper(this, null)

        _loginButton?.setOnClickListener {
            login()
        }
        _signupLink?.setOnClickListener {
            gotoRegister()
        }
        _ipAddressText?.setText("babelacbackend.com")
        _usernameText?.setText("irham83@gmail.com")
        _passwordText?.setText("123456")
    }

    fun login() {
        Log.d(TAG, "Login")
        if (!validate()) {
            onLoginFailed()
            return
        }
        _loginButton!!.isEnabled = false
        val progressDialog =
            ProgressDialog(this@LoginActivity, R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()
        val ip_address = _ipAddressText!!.text.toString()
        val email = _usernameText!!.text.toString()
        val password = _passwordText!!.text.toString()

        // TODO: Implement your own authentication logic here.
        Handler().postDelayed(
            {
                if (loginToAPI(ip_address, email, password)) {
                    onLoginSuccess()
                    progressDialog.dismiss()
                } else {
                    onLoginFailed()
                    progressDialog.dismiss()
                }
            }, 3000
        )
    }

    fun gotoRegister() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun gotoHome() {
        username = _usernameText!!.text.toString()
        insertIntoDB(
            Integer.valueOf(user_id),
            username,
            token_access,
            level,
            address,
            mobile_no,
            _email
        )

        if (level == "9") {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("username", username)
            intent.putExtra("token", token_access)
            intent.putExtra("user_id", user_id)
            intent.putExtra("address", address)
            intent.putExtra("phone", mobile_no)
            intent.putExtra("email", _email)
            startActivity(intent)
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    )

    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    fun onLoginSuccess() {
        _loginButton!!.isEnabled = true
        gotoHome()
        finish()
    }

    fun onLoginFailed() {
        _loginButton!!.isEnabled = true
    }

    fun loginToAPI(
        ip_address: String?,
        email: String,
        password: String
    ): Boolean {
        if (Build.VERSION.SDK_INT > 9) {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

//        try {
//            // POST Request
//            JSONObject params = new JSONObject();
//            params.put("auth_username", email);
//            params.put("auth_password", password);
//
//            response = RequestHandler.sendPost("http://172.104.170.76/api/main/login", params);
//            Log.d("login sukses:", response);
//
//            JSONObject data_login = new JSONObject(response);
//
//            address = data_login.getString("refresh_token");
//            token_access = data_login.getString("access_token");
//
//            return true;
//        } catch (Exception e) {
//            Log.d("login gagal:", e.toString());
//
//            return false;
//        }
        try {
            val params =
                HashMap<String, Any>()
            Log.d("tes", "Connecting login .. ")
            val url = URL("http://tttm.tiketantrian.com/public/api/v01/login")
            params["email"] = email
            params["password"] = password
            val postData = StringBuilder()
            for ((key, value) in params) {
                if (postData.length != 0) postData.append('&')
                postData.append(URLEncoder.encode(key, "UTF-8"))
                postData.append('=')
                postData.append(URLEncoder.encode(value.toString(), "UTF-8"))
            }
            val postDataBytes = postData.toString().toByteArray(charset("UTF-8"))
            val conn =
                url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Content-Length", postDataBytes.size.toString())
            conn.doOutput = true
            conn.outputStream.write(postDataBytes)
            val `in`: Reader =
                BufferedReader(InputStreamReader(conn.inputStream, "UTF-8"))
            val sb = StringBuilder()
            var c: Int
            while (`in`.read().also { c = it } >= 0) {
                sb.append(c.toChar())
            }
            val status = conn.responseCode
            if (status == HttpURLConnection.HTTP_OK) {
                Log.d("tes", "Login Success !")
                val response = sb.toString()
                Log.d("tes", "----> $response")
                val data_login = JSONObject(response)
                if (data_login.has("token")) {
                    if (data_login.getString("token") != "") {
                        Toast.makeText(this, "Login success", Toast.LENGTH_LONG).show()
                        return false
                    }
                }
                token_access = data_login.getJSONObject("meta").getString("token")
                user_id = data_login.getJSONObject("data").getString("id")
                fullname = data_login.getJSONObject("data").getString("name")
                address = "address" //data_login.getJSONObject("data").getString("address");
                mobile_no = "08xxxxxxxx" //data_login.getJSONObject("data").getString("mobile_no");
                _email = data_login.getJSONObject("data").getString("email")
                level = "9" //data_login.getJSONObject("data").getString("level");

                if (address == null) {
                    address = "Not set"
                } else {
                    if (address == "") {
                        address = "Not set"
                    }
                    if (address == "null") {
                        address = "Not set"
                    }
                }
                if (mobile_no == "") {
                    mobile_no = "Not set"
                }
                Toast.makeText(this, "Logged in as $_email", Toast.LENGTH_LONG).show()
                return true
            } else if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Log.d("tes", "Login failed: HttpURLConnection.HTTP_UNAUTHORIZED")
                onLoginFailed()
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onLoginFailed()
            //Toast.makeText(this, "Login Gagal : "+e.toString(), Toast.LENGTH_LONG).show();
            showMessage("Email / sandi Anda salah")
            return false
        }
        return false
    }

    fun validate(): Boolean {
        var valid = true
        val username = _usernameText!!.text.toString()
        val password = _passwordText!!.text.toString()
        if (username.isEmpty()) { // || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            _usernameText!!.error = "enter a valid _email address"
            valid = false
        } else {
            _usernameText!!.error = null
        }
        if (password.isEmpty() || password.length < 4) {
            _passwordText!!.error = "minimum 4 characters"
            valid = false
        } else {
            _passwordText!!.error = null
        }
        return valid
    }

    private fun insertIntoDB(
        id_: Int,
        name_: String,
        token_: String,
        level_: String,
        address_: String,
        phone_: String,
        email_: String
    ): Int?

    {
        val snapshot = AuthSnapshot()
        snapshot.setId(id_)
        snapshot.setName(name_)
        snapshot.setToken(token_)
        snapshot.setIsLogin(1)
        snapshot.setLevel(level_)
        snapshot.setAddress(address_)
        snapshot.setPhone(phone_)
        snapshot.setEmail(email_)
        Log.d(TAG, "inserting TOKEN : $token_")

        var result : Int
        result = authHelper?.update(snapshot)!!

        Log.d(TAG, "onResponse: update data TO_login : "+result)
        return 0
    }

    fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}