package com.example.orata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.orata.data.ModelProduct
import com.example.orata.data.SourceData2
import com.example.orata.ui.Home.AdapterMain
import kotlinx.android.synthetic.main.activity_about.*

class DeliveryActivity : AppCompatActivity() {

    private var list: ArrayList<ModelProduct> = arrayListOf()
    lateinit var rvProduct : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)
        //setSupportActionBar(toolbar)
        supportActionBar!!.hide()

//        supportActionBar?.title = ""
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        fab.setOnClickListener {
//            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dicoding.com/users/29295"))
//            if(intent.resolveActivity(packageManager)!=null){
//                startActivity(intent)
//            }
//        }
//
//        rvProduct = findViewById(R.id.rvProduct)
//        rvProduct.setHasFixedSize(true)
//
//        list.addAll(SourceData2.listData)
//
//        rvProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        val adapter = AdapterMain(list)
//        rvProduct.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflate = menuInflater
        menuInflate.inflate(R.menu.menu_about, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()){
            R.id.action_settings -> {
                Toast.makeText(applicationContext, "Feature is locked", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}