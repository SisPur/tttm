package com.example.orata.ui.Detail

import com.example.orata.R
import com.example.orata.ui.About.AboutActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.orata.DeliveryActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    lateinit var photo: ImageView
    lateinit var name: TextView
    lateinit var price: TextView
    lateinit var weight: TextView
    lateinit var expired: TextView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        photo = findViewById(R.id.product_photo)
        name = findViewById(R.id.product_name)
        price = findViewById(R.id.product_price)
        weight = findViewById(R.id.txt_berat)
        expired = findViewById(R.id.txt_expired_date)
        button = findViewById(R.id.btnBuy)

        name.text = intent.getStringExtra("name")
        price.text = intent.getStringExtra("price")
        weight.text = "Berat Produk "+intent.getStringExtra("weight")+" Kg"
        expired.text = "Kadaluarsa "+intent.getStringExtra("expired")

        Picasso.with(this).load("http://tttm.tiketantrian.com/public/storage/" + intent.getStringExtra("photo")).placeholder(R.mipmap.ic_launcher).into(photo)

//        Glide.with(this)
//            .load(intent.getIntExtra("photo", 0))
//            .into(photo)

        val jmlbeli = jml_beli.text

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Berhasil Menyukai Produk", Snackbar.LENGTH_LONG).show()
        }

        button.setOnClickListener{
                view -> Snackbar.make(view, "Berhasil memasukan $jmlbeli "+intent.getStringExtra("name")+" ke Keranjang", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
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
            }
            R.id.action_cart -> {
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
