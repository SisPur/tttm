package com.example.orata.ui.Home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud1.model.ResultStatus
import com.example.crud1.network.NetworkConfig
import com.example.orata.DeliveryActivity
import com.example.orata.LoginActivity
import com.example.orata.R
import com.example.orata.data.ModelProduct
import com.example.orata.ui.Sample.model.DataStaff
import com.example.orata.ui.Sample.model.ResultData
import com.example.orata.ui.Sample.model2.DataProduk
import com.example.orata.ui.Sample.model2.ResultProduk
import com.example.orata.utils.auth.AuthSnapshot
import com.example.orata.utils.auth.AuthTableHelper
import retrofit2.Call
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private var authHelper: AuthTableHelper? = null
    private var list: ArrayList<ModelProduct> = arrayListOf()
    lateinit var rvProduct : RecyclerView
    lateinit var svProduct : NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        authHelper = AuthTableHelper(this, null)

        if (!authHelper?.getAuth()!!.getToken().equals("-")) {

            supportActionBar?.title = "Hi, "+authHelper?.getAuth()!!.getName()
        }
        else {
            supportActionBar?.title = "TTTM"
        }

        supportActionBar?.elevation = 0F

        svProduct = findViewById(R.id.scrollview)
        svProduct.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if(scrollY > 0){
                supportActionBar?.elevation = 3F
            }else{
                supportActionBar?.elevation = 0F
            }
        }

        rvProduct = findViewById(R.id.rvProduct)
        rvProduct.setHasFixedSize(true)

        //list.addAll(SourceData2.listData) /*membaca data di data.SourceData*/

        //getStaffList()
        getProdukList()

        /*val adapter = WisataAdapterRecycler(dataImage, dataTitle)
        listWisata.adapter = adapter*/
    }

    fun getProdukList(){
        NetworkConfig.getService().getDataProduk()
            .enqueue(object : retrofit2.Callback<ResultProduk>{
                override fun onFailure(call: Call<ResultProduk>, t: Throwable) {
                    Log.d("Error", "Error Data produk")
                }

                override fun onResponse(call: Call<ResultProduk>, response: Response<ResultProduk>) {
                    if(response.isSuccessful){
                        val list_data: List<DataProduk>? = response.body()?.data;
                        if (list_data != null) {
                            for(x in list_data ) {
                                Log.d("tes","--> "+x.name)
                                val food = ModelProduct(x.name.toString(),x.price.toString(), x.cover.toString(), "note1", x.weight.toString())
                                list.add(food);
                            }

                            rvProduct.layoutManager = LinearLayoutManager(applicationContext)
                            val adapter = AdapterMain(list)
                            rvProduct.adapter = adapter
                        }
                    }
                }

            })
    }

    private fun logout() {
        Log.d("tes", "void logout")
        val view: View = layoutInflater.inflate(R.layout.dialog_logout, null)
        val linearLayout = view.findViewById<View>(R.id.main_layout_logout) as LinearLayout
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(linearLayout)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val buttonYes =
            dialog.findViewById<View>(R.id.button_yes_logout) as Button
        val buttonNo =
            dialog.findViewById<View>(R.id.button_no_logout) as Button
        buttonYes.setOnClickListener {
            authHelper?.logout()
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
            dialog.dismiss()
        }
        buttonNo.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(false)
        dialog.show()
    }

    fun getStaffList(){
        NetworkConfig.getService().getDataStaff()
            .enqueue(object : retrofit2.Callback<ResultData>{
                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    //crudView.onFailedGet(t.localizedMessage)
                    Log.d("Error", "Error Data staff")
                }

                override fun onResponse(call: Call<ResultData>, response: Response<ResultData>) {
                    if(response.isSuccessful){
//                        val status = response.body()?.status
//                        if (status == 200){
//
//                        } else{
//                            Log.d("Error", "Error get data $status");
//                        }

                        //val data =
                        val list_data: List<DataStaff>? = response.body()?.data;
                        if (list_data != null) {
                            for(x in list_data ) {
                                Log.d("tes","--> "+x.staffName.toString())
                                val food = ModelProduct(x.staffName.toString(),x.staffAlamat.toString(), "", "")
                                list.add(food);
                            }
                        }
                    }
                }

            })
    }


    //Add data
    fun addData(name : String, hp : String, alamat : String){
        NetworkConfig.getService()
            .addStaff(name, hp, alamat)
            .enqueue(object : retrofit2.Callback<ResultStatus>{
                override fun onFailure(call: Call<ResultStatus>, t: Throwable) {
                    //crudView.errorAdd(t.localizedMessage)
                    Log.d("Error", "Error Data "+t.localizedMessage)
                }

                override fun onResponse(call: Call<ResultStatus>, response: Response<ResultStatus>) {
                    if (response.isSuccessful && response.body()?.status == 200) {
                        //crudView.successAdd(response.body()?.pesan ?: "")
                    }else {
                        //crudView.errorAdd(response.body()?.pesan ?: "")
                    }
                }

            })
    }


    //Hapus Data
    fun hapusData(id: String?){
        NetworkConfig.getService()
            .deleteStaff(id)
            .enqueue(object : retrofit2.Callback<ResultStatus>{
                override fun onFailure(call: Call<ResultStatus>, t: Throwable) {
                    //crudView.onErrorDelete(t.localizedMessage)
                    Log.d("Error", "Error Data "+t.localizedMessage)
                }

                override fun onResponse(call: Call<ResultStatus>, response: Response<ResultStatus>) {
                    if (response.isSuccessful && response.body()?.status == 200){
                        //crudView.onSuccessDelete(response.body()?.pesan ?: "")
                    } else {
                        //crudView.onErrorDelete(response.body()?.pesan ?: "")
                    }
                }

            })
    }

    //Update Data
    fun updateData(id: String, name: String, hp: String, alamat: String){
        NetworkConfig.getService()
            .updateStaff(id, name, hp, alamat)
            .enqueue(object : retrofit2.Callback<ResultStatus>{
                override fun onFailure(call: Call<ResultStatus>, t: Throwable) {
                    //crudView.onErrorUpdate(t.localizedMessage)
                    Log.d("Error", "Error Data "+t.localizedMessage)
                }

                override fun onResponse(call: Call<ResultStatus>, response: Response<ResultStatus>) {
                    if (response.isSuccessful && response.body()?.status == 200){
                        //crudView.onSuccessUpdate(response.body()?.pesan ?: "")
                    }else{
                        //crudView.onErrorUpdate(response.body()?.pesan ?: "")
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate = menuInflater
        menuInflate.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        intent = Intent(applicationContext, DeliveryActivity::class.java)
        when(item.getItemId()){
            R.id.action_about -> {
                Toast.makeText(applicationContext, "Coming soon ..", Toast.LENGTH_LONG).show()
                logout()
            }
            R.id.action_cart -> {
                if(isLogin()) {
                    startActivity(intent)
                }
                else{
                    intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun isLogin () : Boolean {
        var checkViaAPI = true
        if (authHelper != null) {
            var data : AuthSnapshot? = authHelper?.getAuth()
            if (data != null) {
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
        else {
            Log.d("tes","auth empty");
        }

        if (!checkViaAPI) {
            return true
        }

        return false
    }
}
