package com.example.orata

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.example.orata.utils.MyUtilities
import com.example.orata.utils.NetworkUtilities
import com.squareup.picasso.Picasso
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    //var dbHelper: SqlOperations? = null
    var idEditText: EditText? = null
    var usernameEditText: EditText? = null
    var fullnameEditText: EditText? = null
    var addressEditText: EditText? = null
    var emailEditText: EditText? = null
    var phoneEditText: EditText? = null
    var genderEditText: EditText? = null
    var dayEditText: EditText? = null
    var monthEditText:EditText? = null
    var yearEditText:EditText? = null
    var passwordEditText: EditText? = null
    var pictureEditText: EditText? = null

    var saveButton: Button? = null
    var buttonLayout: LinearLayout? = null
    var row_id: RelativeLayout? = null
    var editButton: Button? = null
    var deleteButton:android.widget.Button? = null

    var userID = 0
    var progressDialog: ProgressDialog? = null
    var selected_level: String? = null
    var current_password:kotlin.String? = null

    var token = ""
    var myusername:kotlin.String? = ""
    var username:kotlin.String? = ""
    var latest = 0
//    val jobList: MutableList<job> = ArrayList<job>()

    var row_email: RelativeLayout? = null
    var row_phone:RelativeLayout? = null
    var row_fullname:RelativeLayout? = null
    var spinner_job: Spinner? = null

    val CAMERA_REQUEST = 99
    var img_picture: ImageView? = null

    var new_image_name = ""
    var old_image:kotlin.String? = ""
    var file_picture: File? = null

    var imageUri: Uri? = null

    val spinner_type: Spinner? = null
    var spinner_brand:Spinner? = null
    val GALLERY_REQUEST_CODE = 90
    val CAMERA_REQUEST_CODE = 80
    var cameraFilePath: String? = null

    @Throws(IOException::class)
    open fun createImageFile(): File? {
        // Create an image file name
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        //This is the directory in which the file will be created. This is the default location of Camera photos
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ), "Camera"
        )
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for using again
        cameraFilePath = "file://" + image.absolutePath
        return image
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = getIntent().getIntExtra(MyUtilities.KEY_EXTRA_CONTACT_ID, 0)
        token = getIntent().getStringExtra("token")
        myusername = getIntent().getStringExtra("myusername")
        latest = getIntent().getIntExtra("latest", 0)
        setContentView(R.layout.activity_manage_user)
        row_id = findViewById<View>(R.id.row_id) as RelativeLayout?
        row_email = findViewById<View>(R.id.row_email) as RelativeLayout?
        row_phone = findViewById<View>(R.id.row_phone) as RelativeLayout
        row_fullname = findViewById<View>(R.id.row_name) as RelativeLayout
        idEditText = findViewById<View>(R.id.editTextID) as EditText?
        usernameEditText = findViewById<View>(R.id.editTextUsername) as EditText?
        fullnameEditText = findViewById<View>(R.id.editTextFullame) as EditText?
        addressEditText = findViewById<View>(R.id.editTextAddress) as EditText?
        emailEditText = findViewById<View>(R.id.editTextEmail) as EditText?
        phoneEditText = findViewById<View>(R.id.editTextPhone) as EditText?
        genderEditText = findViewById<View>(R.id.editTextGender) as EditText?
        dayEditText = findViewById<View>(R.id.editTextDay) as EditText?
        monthEditText = findViewById<View>(R.id.editTextMonth) as EditText
        yearEditText = findViewById<View>(R.id.editTextYear) as EditText
        passwordEditText = findViewById<View>(R.id.editTextPassword) as EditText?
        pictureEditText = findViewById<View>(R.id.editTextPicture) as EditText?

        saveButton = findViewById<View>(R.id.saveButton) as Button?
        saveButton?.setOnClickListener {

        }
        buttonLayout = findViewById<View>(R.id.buttonLayout) as LinearLayout?
        editButton = findViewById<View>(R.id.editButton) as Button?
        editButton?.setOnClickListener {

        }
        deleteButton = findViewById<View>(R.id.deleteButton) as Button
        deleteButton?.setOnClickListener {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val toolbar = findViewById<View>(R.id.toolbar_detail) as Toolbar
            toolbar.setNavigationOnClickListener { finish() }
        }

        progressDialog = ProgressDialog(this, R.style.AppTheme_Dark_Dialog)
        progressDialog!!.isIndeterminate = true
//        dbHelper = SqlOperations(this)
//        dbHelper.open()
        img_picture = findViewById<View>(R.id.img_picture) as ImageView?
        img_picture!!.setOnClickListener { showSourceOption() }
//        spinner_job = findViewById<View>(R.id.spinner_user_type) as Spinner?
//        try {
//            val data: JSONArray = getJobList(token)
//            if (data != null) {
//                val items = arrayOfNulls<String>(data.length())
//                for (a in 0 until data.length()) {
//                    val n_job = job()
//                    n_job.id = "" + data.getJSONObject(a).getInt("id")
//                    n_job.name_job = data.getJSONObject(a).getString("job")
//                    n_job.privilege = data.getJSONObject(a).getString("privilege")
//                    jobList.add(n_job)
//                    items[a] = n_job.name_job
//                }
//                val spinnerArrayAdapter =
//                    ArrayAdapter(this, R.layout.custom_textview_to_spinner, items)
//                spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_textview_to_spinner)
//                spinner_job!!.adapter = spinnerArrayAdapter
//                spinner_job!!.onItemSelectedListener = object : OnItemSelectedListener {
//                    override fun onItemSelected(
//                        parent: AdapterView<*>,
//                        view: View,
//                        position: Int,
//                        id: Long
//                    ) {
//                        val name =
//                            parent.getItemAtPosition(position) as String
//                        for (a in jobList.indices) {
//                            Log.d("tes", "$name << >> $selected_level")
//                            if (jobList[a].name_job.equals(name)) {
//                                selected_level = jobList[a].id
//                                Log.d("tes", "selected job id >> $selected_level")
//                            }
//                        }
//                    }
//
//                    override fun onNothingSelected(parent: AdapterView<*>?) {
//                        // TODO Auto-generated method stub
//                    }
//                }
//            } else {
//                Log.d("tes", "Data job kosong")
//                showMessage("Data job kosong")
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//            Log.d("tes", "error store number ----> $e")
//        }
        Log.d("tes", "userID ----> $userID")
        if (userID > 0) {
            saveButton!!.visibility = View.GONE
            buttonLayout!!.visibility = View.VISIBLE
            val id = "" + userID
            username = getIntent().getStringExtra("username")
            val password: String = getIntent().getStringExtra("password")
            val fullname: String = getIntent().getStringExtra("fullname")
            val address: String = getIntent().getStringExtra("address")
            val phone: String = getIntent().getStringExtra("phone")
            val gender: String = getIntent().getStringExtra("gender")
            val birthdate: String = getIntent().getStringExtra("birthdate")
            val type: String = getIntent().getStringExtra("type")
            val image: String = getIntent().getStringExtra("image")
            if (type == "Level 1") {
                spinner_job!!.setSelection(0)
            } else if (type == "Level 2") {
                spinner_job!!.setSelection(1)
            } else if (type == "Level 3") {
                spinner_job!!.setSelection(2)
            }
            old_image = image
            img_picture?.isEnabled = false
            Picasso.with(this).load("https://babelacis.xyz/images/stocksImg/$image")
                .placeholder(R.mipmap.ic_launcher).into(img_picture)
            idEditText?.setText(id)
            idEditText?.isFocusable = false
            idEditText?.isClickable = false
            usernameEditText?.setText(username)
            usernameEditText?.isFocusable = false
            usernameEditText?.isClickable = false
            current_password = password
            emailEditText?.setText(fullname)
            emailEditText?.isFocusable = false
            emailEditText?.isClickable = false
            emailEditText?.isEnabled = false
            addressEditText?.setText(address)
            addressEditText?.isFocusable = false
            addressEditText?.isClickable = false
            phoneEditText?.setText(phone)
            phoneEditText?.isFocusable = false
            phoneEditText?.isClickable = false
            genderEditText?.setText(gender)
            genderEditText?.isFocusable = false
            genderEditText?.isClickable = false
            if (birthdate != null) {
                if (birthdate != "") {
                    val x = birthdate.split("-").toTypedArray()
                    if (x.size > 2) {
                        dayEditText?.setText(x[2])
                        monthEditText?.setText(x[1])
                        yearEditText?.setText(x[0])
                    }
                }
            }
            dayEditText?.isFocusable = false
            dayEditText?.isClickable = false
            monthEditText?.setFocusable(false)
            monthEditText?.setClickable(false)
            yearEditText?.setFocusable(false)
            yearEditText?.setClickable(false)
            passwordEditText?.visibility = View.GONE
            passwordEditText?.isFocusable = false
            passwordEditText?.isClickable = false
            pictureEditText?.setText(image)
            pictureEditText?.isFocusable = false
            pictureEditText?.isClickable = false
            row_id?.visibility = View.GONE
            row_fullname?.setVisibility(View.GONE)
            spinner_job?.isEnabled = false
        } else {
            row_id?.visibility = View.VISIBLE
            idEditText?.setText("" + (latest + 1))
            idEditText?.isEnabled = false
            row_fullname?.setVisibility(View.GONE)
            img_picture?.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    fun showSourceOption() {
        val builderSingle =
            AlertDialog.Builder(this)
        builderSingle.setIcon(R.drawable.pr_karkas2)
        builderSingle.setTitle("Select One source:-")
        val arrayAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.select_dialog_singlechoice
        )
        arrayAdapter.add("Galery")
        arrayAdapter.add("Camera")
        builderSingle.setNegativeButton(
            "cancel"
        ) { dialog, which -> dialog.dismiss() }
        builderSingle.setAdapter(
            arrayAdapter
        ) { dialog, which ->
            val strName = arrayAdapter.getItem(which)
            if (strName == "Camera") {
                //captureFromCamera();
                takeImageFromCamera()
                dialog.dismiss()
            } else {
                pickFromGallery()
                dialog.dismiss()
            }
        }
        builderSingle.show()
    }

    open fun pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        val intent = Intent(Intent.ACTION_PICK)
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.type = "image/*"
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        val mimeTypes =
            arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    //method untuk mengcapture gambar menggunakan kamera bisa di letakan saat onClick
    fun takeImageFromCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = getContentResolver().insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST)
    }

    fun getRealPathFromURI(contentUri: Uri?): String {
        val proj = arrayOf(
            MediaStore.Images.Media.DATA
        )
        val cursor: Cursor = managedQuery(contentUri, proj, null, null, null)
        val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                //Bitmap mphoto = (Bitmap) data.getExtras().get("data");
                //panggil method uploadImage
                try {
                    val thumbnail =
                        MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri)
                    img_picture?.setImageBitmap(thumbnail)
                    val imageurl = getRealPathFromURI(imageUri)
                    Log.d("tes", "URL image ---> $imageurl")
                    val timeStamp =
                        SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                    new_image_name = "profile_camera$timeStamp.jpg"
                    file_picture = File(imageurl) //createTempFile(mphoto);
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {
                    //data.getData returns the content URI for the selected Image
                    val selectedImage = data?.data
                    //imageView.setImageURI(selectedImage);
                    val filePathColumn = arrayOf(
                        MediaStore.Images.Media.DATA
                    )
                    // Get the cursor
                    val cursor: Cursor? = selectedImage?.let { getContentResolver().query(it, filePathColumn, null, null, null) }
                    // Move to first row
                    cursor?.moveToFirst()
                    //Get the column index of MediaStore.Images.Media.DATA
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    //Gets the String value in the column
                    val imgDecodableString = columnIndex?.let { cursor?.getString(it) }
                    cursor?.close()
                    // Set the Image in ImageView after decoding the String
                    img_picture?.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString))
                    Log.d("tes", "Connecting ftp ..")
                    val timeStamp =
                        SimpleDateFormat("yyyyMMddHHmmss").format(Date())
                    new_image_name = "profile_galery$timeStamp.png"
                    file_picture = createTempFile(BitmapFactory.decodeFile(imgDecodableString))
                }
                CAMERA_REQUEST_CODE -> img_picture?.setImageURI(Uri.parse(cameraFilePath))
            }
        }
    }



    /*
    TODO mengconvert Bitmap menjadi file dikarenakan retrofit hanya mengenali tipe file untuk upload gambarnya sekaligus mengcompressnya menjadi WEBP dikarenakan size bisa sangat kecil dan kualitasnya pun setara dengan PNG.
    */
    open fun createTempFile(bitmap: Bitmap): File? {
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            , System.currentTimeMillis().toString() + "_image.png"
        )
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
        val bitmapdata = bos.toByteArray()
        //write the bytes in file
        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun getJobList(token: String): JSONArray? {
        try {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d("tes", "Getting job list .. $token")
            val url = URL("https://babelacbackend.com/api/job/")
            val conn =
                url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $token")
            val response = conn.inputStream
            val jsonReply: String = convertStreamToString(response)
            Log.d("tes", "Response : $jsonReply")
            val data = JSONArray(jsonReply)
            if (data != null) {
                if (data.length() > 0) {
                    //showMessage("Item ditemukan");
                    return data
                }
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("tes", "error get job list : $e")
        }
        return null
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.saveButton -> {
                persistUser()
                return
            }
            R.id.editButton -> {
                saveButton?.visibility = View.VISIBLE
                buttonLayout?.visibility = View.GONE
                fullnameEditText?.isEnabled = true
                fullnameEditText?.isFocusableInTouchMode = true
                fullnameEditText?.isClickable = true
                addressEditText?.isEnabled = true
                addressEditText?.isFocusableInTouchMode = true
                addressEditText?.isClickable = true
                phoneEditText?.isEnabled = true
                phoneEditText?.isFocusableInTouchMode = true
                phoneEditText?.isClickable = true
                genderEditText?.isEnabled = true
                genderEditText?.isFocusableInTouchMode = true
                genderEditText?.isClickable = true
                dayEditText?.isEnabled = true
                dayEditText?.isFocusableInTouchMode = true
                dayEditText?.isClickable = true
                monthEditText?.setEnabled(true)
                monthEditText?.setFocusableInTouchMode(true)
                monthEditText?.setClickable(true)
                yearEditText?.setEnabled(true)
                yearEditText?.setFocusableInTouchMode(true)
                yearEditText?.setClickable(true)
                passwordEditText?.isEnabled = true
                passwordEditText?.isFocusableInTouchMode = true
                passwordEditText?.isClickable = true
                usernameEditText?.isEnabled = true
                usernameEditText?.isFocusableInTouchMode = true
                usernameEditText?.isClickable = true
                pictureEditText?.isEnabled = true
                pictureEditText?.isFocusableInTouchMode = true
                pictureEditText?.isClickable = true
                spinner_job?.isEnabled = true
                img_picture?.isEnabled = true
                Log.d("tes", myusername + " ---- " + username)
                if (myusername != username) {
                    usernameEditText?.isEnabled = false
                    fullnameEditText?.isEnabled = false
                    addressEditText?.isEnabled = false
                    phoneEditText?.isEnabled = false
                    genderEditText?.isEnabled = false
                    dayEditText?.isEnabled = false
                    monthEditText?.setEnabled(false)
                    yearEditText?.setEnabled(false)
                    img_picture?.isEnabled = false
                }
                return
            }
            R.id.deleteButton -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Delete")
                    .setPositiveButton(
                        "Yes"
                    ) { dialog, id ->
                        if (deleteUser(
                                idEditText?.text.toString(),
                                usernameEditText?.text.toString()
                            )
                        ) {
                            Toast.makeText(
                                getApplicationContext(),
                                "user deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                getApplicationContext(),
                                "user not deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    }
                    .setNegativeButton(
                        "No"
                    ) { dialog, id ->
                        // User cancelled the dialog
                    }
                val d = builder.create()
                d.setTitle("Delete user $userID ?")
                d.show()
                return
            }
        }
    }

    fun persistUser() {
        if (usernameEditText?.text.toString() != "" &&
            selected_level != "" &&
            emailEditText?.text.toString() != "" &&
            phoneEditText?.text.toString() != "null" &&
            phoneEditText?.text.toString() != "" &&
            genderEditText?.text.toString() != "" &&
            addressEditText?.text.toString() != "" &&
            dayEditText?.text.toString() != "null" &&
            dayEditText?.text.toString() != "" &&
            monthEditText?.getText().toString() != "null" &&
            monthEditText?.getText().toString() != "" &&
            yearEditText?.getText().toString() != "null" &&
            yearEditText?.getText().toString() != "" ||
            myusername != username
        ) {
            if (new_image_name != "" || old_image != "") {
                if (userID > 0) {
                    if (myusername == username) {
                        if (new_image_name == "") {
                            if (passwordEditText?.text.toString() != "") {
                                current_password = passwordEditText?.text.toString()
                            }
                            if (updateUser(
                                    usernameEditText?.text.toString()
                                    ,
                                    selected_level
                                    ,
                                    emailEditText?.text.toString()
                                    ,
                                    passwordEditText?.text.toString()
                                    ,
                                    genderEditText?.text.toString()
                                    ,
                                    addressEditText?.text.toString()
                                    ,
                                    old_image
                                    ,
                                    yearEditText?.getText()
                                        .toString() + "-" + monthEditText?.getText()
                                        .toString() + "-" + dayEditText?.text.toString()
                                    ,
                                    phoneEditText?.text.toString()
                                )
                            ) {
                                Toast.makeText(
                                    getApplicationContext(),
                                    "user updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    getApplicationContext(),
                                    "user not update",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            finish()
                        } else {
                            if (sendFileViaFTP(file_picture)) {
                                if (passwordEditText?.text.toString() != "") {
                                    current_password = passwordEditText?.text.toString()
                                }
                                if (updateUser(
                                        usernameEditText?.text.toString()
                                        ,
                                        selected_level
                                        ,
                                        emailEditText?.text.toString()
                                        ,
                                        passwordEditText?.text.toString()
                                        ,
                                        genderEditText?.text.toString()
                                        ,
                                        addressEditText?.text.toString()
                                        ,
                                        new_image_name
                                        ,
                                        yearEditText?.getText()
                                            .toString() + "-" + monthEditText?.getText()
                                            .toString() + "-" + dayEditText?.text.toString()
                                        ,
                                        phoneEditText?.text.toString()
                                    )
                                ) {
                                    Toast.makeText(
                                        getApplicationContext(),
                                        "user updated",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        getApplicationContext(),
                                        "user not update",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                finish()
                            }
                        }
                    } else {
                        if (updateUserJob(usernameEditText?.text.toString(), selected_level)) {
                            showMessage("User job updated")
                        } else {
                            showMessage("Fail update user job")
                        }
                    }
                } else {
                    if (sendFileViaFTP(file_picture)) {
                        if (addUser(
                                usernameEditText?.text.toString()
                                , selected_level
                                , emailEditText?.text.toString()
                                , passwordEditText?.text.toString()
                                , genderEditText?.text.toString()
                                , addressEditText?.text.toString()
                                , pictureEditText?.text.toString()
                                , dayEditText?.text.toString()
                            )
                        ) {
                            Toast.makeText(
                                getApplicationContext(),
                                "new user Inserted",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                getApplicationContext(),
                                "Could not Insert user",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    } else {
                        Toast.makeText(
                            getApplicationContext(),
                            "Upload image failed. Operation aborted",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    getApplicationContext(),
                    "Foto profil belum dipilih",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(getApplicationContext(), "Silahkan lengkapi", Toast.LENGTH_SHORT).show()
        }
    }

    fun sendFileViaFTP(file: File?): Boolean {
        var ftpClient: FTPClient? = null
        try {
            ftpClient = FTPClient()
            ftpClient.connect(InetAddress.getByName("ftp.babelacbackend.com"))
            if (ftpClient.login("backend_img182@babelacbackend.com", "xhdG86Whw+pq")) {
                ftpClient.enterLocalPassiveMode() // important!
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                val Location = Environment.getExternalStorageDirectory()
                    .toString()
                //String data = Location + File.separator + "FileToSend.txt";
                //FileInputStream in = new FileInputStream(new File(data));
                val `in` = FileInputStream(file)
                val result: Boolean = ftpClient.storeFile(new_image_name, `in`)
                `in`.close()
                if (result) Log.v("upload result", "succeeded")
                ftpClient.logout()
                ftpClient.disconnect()
                return true
            }
        } catch (e: Exception) {
            Log.v("count", "error")
            e.printStackTrace()
        }
        return false
    }

    fun deleteUser(id_user: String, username: String): Boolean {
        try {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d(
                "tes",
                "Deleting user --> $id_user / $username / $token"
            )
            val url =
                URL("https://babelacbackend.com/api/users/deleteUser/$username")
            val conn =
                url.openConnection() as HttpURLConnection
            conn.requestMethod = "DELETE"
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("content-type", "application/json;  charset=utf-8")
            val parent = JSONObject()
            Log.d("tes", "JSON submit : $parent")
            val writer: Writer =
                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8"))
            //writer.write(parent.toString());
            writer.close()
            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("tes", "Response : FAIL " + conn.responseCode)
            } else {
                Log.d("tes", "Response : OK")
                return true
            }
            val response = conn.inputStream
            val jsonReply: String = convertStreamToString(response)
            Log.d("tes", "" + jsonReply)
            if (jsonReply.length > 1) {
                if (jsonReply.contains("OK")) {
                    return true
                }
            } else {
                return false
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun addUser(
        name: String,
        job_id: String?,
        email: String?,
        password: String?,
        gender: String?,
        address: String?,
        profilePicture: String?,
        dateOfBirth: String?
    ): Boolean {
        try {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d("tes", "Submting new user --> $name / $token")
            val url =
                URL("https://babelacbackend.com/api/users/registerUser")
            val conn =
                url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("content-type", "application/json;  charset=utf-8")
            val parent = JSONObject()
            parent.put("name", name)
            parent.put("job_id", job_id)
            parent.put("email", email)
            parent.put("password", password)
            parent.put("gender", gender)
            parent.put("address", address)
            parent.put("profilePicture", profilePicture)
            parent.put("dateOfBirth", dateOfBirth)
            Log.d("tes", "JSON submit : $parent")
            val writer: Writer =
                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8"))
            writer.write(parent.toString())
            writer.close()
            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("tes", "Response : FAIL " + conn.responseCode)
            } else {
                Log.d("tes", "Response : OK")
                return true
            }
            val response = conn.inputStream
            val jsonReply: String = convertStreamToString(response)
            Log.d("tes", "" + jsonReply)
            if (jsonReply.length > 1) {
                if (jsonReply.contains("OK")) {
                    return true
                }
            } else {
                return false
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun updateUser(
        name: String,
        job_id: String?,
        email: String?,
        password: String?,
        gender: String?,
        address: String?,
        profilePicture: String?,
        dateOfBirth: String?,
        phone: String?
    ): Boolean {
        try {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d("tes", "Updating user --> $name / $token")
            val url =
                URL("https://babelacbackend.com/api/users/updateProfile")
            val conn =
                url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("content-type", "application/json;  charset=utf-8")
            val parent = JSONObject()
            parent.put("name", name)
            parent.put("job_id", job_id)
            //parent.put("email", email);
            //parent.put("password", password);
            parent.put("gender", gender)
            parent.put("address", address)
            parent.put("mobilePhone", Integer.valueOf(phone!!))
            parent.put("profilePicture", profilePicture)
            parent.put("dateOfBirth", dateOfBirth)
            Log.d("tes", "JSON submit : $parent")
            val writer: Writer =
                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8"))
            writer.write(parent.toString())
            writer.close()
            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("tes", "Response : FAIL " + conn.responseCode)
            } else {
                Log.d("tes", "Response : OK")
                return true
            }
            val response = conn.inputStream
            val jsonReply: String = convertStreamToString(response)
            Log.d("tes", "" + jsonReply)
            if (jsonReply.length > 1) {
                if (jsonReply.contains("OK")) {
                    return true
                }
            } else {
                return false
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun updateUserJob(name: String, job_id: String?): Boolean {
        try {
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            Log.d("tes", "Updating user --> $name / $token")
            val url =
                URL("https://babelacbackend.com/api/users/updateUserJob/$name")
            val conn =
                url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.setRequestProperty("Accept", "application/json")
            conn.setRequestProperty("content-type", "application/json;  charset=utf-8")
            val parent = JSONObject()
            //parent.put("name", name);
            parent.put("job_id", job_id)
            Log.d("tes", "JSON submit : $parent")
            val writer: Writer =
                BufferedWriter(OutputStreamWriter(conn.outputStream, "UTF-8"))
            writer.write(parent.toString())
            writer.close()
            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d("tes", "Response : FAIL " + conn.responseCode)
            } else {
                Log.d("tes", "Response : OK")
                return true
            }
            val response = conn.inputStream
            val jsonReply: String = convertStreamToString(response)
            Log.d("tes", "" + jsonReply)
            if (jsonReply.length > 1) {
                if (jsonReply.contains("OK")) {
                    return true
                }
            } else {
                return false
            }
            conn.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    open fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(
                    """
                        $line
                        
                        """.trimIndent()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

    fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}